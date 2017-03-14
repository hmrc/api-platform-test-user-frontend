/*
 * Copyright 2017 HM Revenue & Customs
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

import javax.inject.Inject

import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Result, AnyContent, Request, Action}
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.play.http.BadRequestException
import uk.gov.hmrc.testuser.models.{NavLink, UserType}
import uk.gov.hmrc.testuser.services.{NavigationService, TestUserServiceImpl, TestUserService}

import scala.concurrent.Future

trait TestUserController extends FrontendController with I18nSupport {

  val testUserService: TestUserService
  val navigationService: NavigationService

  def showCreateUserPage() = headerNavigation { implicit request => navLinks =>
    Future.successful(Ok(uk.gov.hmrc.testuser.views.html.create_test_user(navLinks)))
  }

  def createUser() = headerNavigation { implicit request => navLinks =>
    val userType = for {
      form <- request.body.asFormUrlEncoded
      uType <- form.get("type").flatMap(_.headOption)
      userType <- UserType.from(uType)
    } yield userType

    userType match {
      case Some(uType) => testUserService.createUser(uType) map (user => Ok(uk.gov.hmrc.testuser.views.html.test_user(navLinks, user)))
      case _ => Future.failed(new BadRequestException("Invalid request"))
    }
  }

  private def headerNavigation(f: Request[AnyContent] => Seq[NavLink] => Future[Result]): Action[AnyContent] = {
    Action.async { implicit request =>
      // We use a non-standard cookie which doesn't get propagated in the header carrier
      val newHc = request.headers.get(COOKIE).fold(hc) { cookie => hc.withExtraHeaders(COOKIE -> cookie) }
      navigationService.headerNavigation()(newHc) flatMap { navLinks =>
        f(request)(navLinks)
      } recoverWith {
        case ex =>
          Logger.error("User navigation links can not be rendered due to service call failure", ex)
          f(request)(Seq.empty)
      }
    }
  }
}

class TestUserControllerImpl @Inject()(override val messagesApi: MessagesApi, override val testUserService: TestUserServiceImpl, override val navigationService: NavigationService) extends TestUserController
