/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.testuser.controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.http.BadRequestException
import uk.gov.hmrc.play.bootstrap.controller.WithUnsafeDefaultFormBinding
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.testuser.ApplicationLogger
import uk.gov.hmrc.testuser.config.ApplicationConfig
import uk.gov.hmrc.testuser.connectors.ApiPlatformTestUserConnector
import uk.gov.hmrc.testuser.models.{NavLink, UserTypes}
import uk.gov.hmrc.testuser.services.{NavigationService, TestUserService}
import uk.gov.hmrc.testuser.views.html.{CreateTestUserView, TestUserView}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TestUserController @Inject() (
    override val messagesApi: MessagesApi,
    testUserService: TestUserService,
    navigationService: NavigationService,
    apiPlatformTestUserConnector: ApiPlatformTestUserConnector,
    messagesControllerComponents: MessagesControllerComponents,
    createTestUser: CreateTestUserView,
    testUser: TestUserView
)(implicit val ec: ExecutionContext, config: ApplicationConfig)
    extends FrontendController(messagesControllerComponents)
    with I18nSupport
    with ApplicationLogger
    with WithUnsafeDefaultFormBinding {

  def showCreateUserPage() = headerNavigation { implicit request => navLinks =>
    Future.successful(Ok(createTestUser(navLinks, CreateUserForm.form)))
  }

  def createUser() = headerNavigation { implicit request => navLinks =>
    def validForm(form: CreateUserForm) = {
      UserTypes.from(form.userType.getOrElse("")) match {
        case Some(uType) => testUserService.createUser(uType) map (user => Ok(testUser(navLinks, user)))
        case _           => Future.failed(new BadRequestException("Invalid request"))
      }
    }

    def invalidForm(invalidForm: Form[CreateUserForm]) = {
      Future.successful(BadRequest(createTestUser(navLinks, invalidForm)))
    }

    CreateUserForm.form.bindFromRequest().fold(invalidForm, validForm)
  }

  private def headerNavigation(f: Request[AnyContent] => Seq[NavLink] => Future[Result]): Action[AnyContent] = {
    Action.async { implicit request =>
      // We use a non-standard cookie which doesn't get propagated in the header carrier
      val newHc = request.headers.get(COOKIE).fold(hc) { cookie => hc.withExtraHeaders(COOKIE -> cookie) }
      navigationService.headerNavigation()(newHc) flatMap { navLinks =>
        f(request)(navLinks)
      } recoverWith { case ex =>
        logger.error("User navigation links can not be rendered due to service call failure", ex)
        f(request)(Seq.empty)
      }
    }
  }
}

case class CreateUserForm(userType: Option[String])

object CreateUserForm {

  val form: Form[CreateUserForm] = Form(
    mapping(
      "userType" -> optional(text).verifying(FormKeys.createUserTypeNoChoiceKey, s => s.isDefined)
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  )
}
