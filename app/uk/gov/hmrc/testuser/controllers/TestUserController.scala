/*
 * Copyright 2016 HM Revenue & Customs
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

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Action
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.play.http.BadRequestException
import uk.gov.hmrc.testuser.models.UserType
import uk.gov.hmrc.testuser.models.UserType.{ORGANISATION, INDIVIDUAL}
import uk.gov.hmrc.testuser.services.{TestUserServiceImpl, TestUserService}

import scala.concurrent.Future

trait TestUserController extends FrontendController with I18nSupport {

  val testUserService: TestUserService

  def showCreateUserPage() = Action.async { implicit request =>
    Future.successful(Ok(uk.gov.hmrc.testuser.views.html.create_test_user()))
  }

  def createUser() = Action.async { implicit request =>
    val userType = for {
      form <- request.body.asFormUrlEncoded
      uType <- form.get("type").flatMap(_.headOption)
      userType <- UserType.from(uType)
    } yield userType

    userType match {
      case Some(uType) => testUserService.createUser(uType) map (user => Ok(uk.gov.hmrc.testuser.views.html.test_user(user)))
      case _ => Future.failed(new BadRequestException("Invalid request"))
    }
  }
}

class TestUserControllerImpl @Inject()(override val messagesApi: MessagesApi, override val testUserService: TestUserServiceImpl) extends TestUserController
