/*
 * Copyright 2018 HM Revenue & Customs
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

import org.jsoup.Jsoup
import play.api.i18n.{Lang, MessagesApi}
import play.api.mvc.{Action, AnyContent, AnyContentAsFormUrlEncoded, MessagesControllerComponents}
import play.api.test.FakeRequest
import uk.gov.hmrc.testuser.common.LogSuppressing
import uk.gov.hmrc.testuser.connectors.ApiPlatformTestUserConnector
import uk.gov.hmrc.testuser.models.UserTypes.{INDIVIDUAL, ORGANISATION}
import uk.gov.hmrc.testuser.models._
import uk.gov.hmrc.testuser.services.{NavigationService, TestUserService}
import uk.gov.hmrc.play.views.html.helpers.ReportAProblemLink
import play.api.test.Helpers._

import scala.concurrent.Future.{successful,failed}
import uk.gov.hmrc.test.utils.AsyncHmrcSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import akka.stream.Materializer
import uk.gov.hmrc.testuser.views.html.TestUserView
import uk.gov.hmrc.testuser.views.html.CreateTestUserView

import scala.concurrent.ExecutionContext.Implicits.global
import uk.gov.hmrc.http.UpstreamErrorResponse
import play.api.Logger

class TestUserControllerSpec
  extends AsyncHmrcSpec with GuiceOneAppPerSuite with LogSuppressing {

  private val individualFields = Seq(Field("saUtr", "Self Assessment UTR", "1555369052"), Field("nino", "","CC333333C"), Field("vrn", "", "999902541"))
  val individual = TestIndividual("ind-user", "ind-password", individualFields)

  private val organisationFields = Seq(
    Field("saUtr", "Self Assessment UTR", "1555369053"),
    Field("empRef", "Employer Ref", "555/EIA000"),
    Field("ctUtr", "CT UTR", "1555369054"),
    Field("vrn", "", "999902541"))
  val organisation = TestOrganisation("org-user", "org-password", organisationFields)

  trait Setup {
    implicit val materializer = app.injector.instanceOf[Materializer]
    private val csrfAddToken = app.injector.instanceOf[play.filters.csrf.CSRFAddToken]
    
    val navLinks = Seq(NavLink("sign-in", "http://sign-in"))
    val fieldDefinitions = Seq(FieldDefinition("fieldDef1", "Field Def 1", Seq(INDIVIDUAL, ORGANISATION)))

    val mcc = app.injector.instanceOf[MessagesControllerComponents]
    val createTestUserView = app.injector.instanceOf[CreateTestUserView]
    val testUserView = app.injector.instanceOf[TestUserView]

    val mockTestUserService = mock[TestUserService]
    val mockNavigationService = mock[NavigationService]
    val mockApiPlatformTestUserConnector = mock[ApiPlatformTestUserConnector]

    val underTest = new TestUserController(
      app.injector.instanceOf[MessagesApi],
      mockTestUserService,
      mockNavigationService,
      mockApiPlatformTestUserConnector,
      mcc,
      app.injector.instanceOf[ReportAProblemLink],
      createTestUserView,
      testUserView
    )

    when(mockTestUserService.createUser(eqTo(INDIVIDUAL))(*)).thenReturn(successful(individual))
    when(mockTestUserService.createUser(eqTo(ORGANISATION))(*)).thenReturn(successful(organisation))
    when(mockNavigationService.headerNavigation()(*)).thenReturn(successful(navLinks))

    def execute[T <: play.api.mvc.AnyContent](action: Action[AnyContent], request: FakeRequest[T] = FakeRequest()) = csrfAddToken(action)(request)
  }

  "showCreateTestUser" should {
    "display the Create test user page" in new Setup {
      val result = execute(underTest.showCreateUserPage())
      val page = contentAsString(result)

      page should include("Create test user")

      val document = Jsoup.parse(page)

      document.getElementById("Individual").hasAttr("checked") shouldBe false
      document.getElementById("Organisation").hasAttr("checked") shouldBe false
    }

    "display the logged in navigation links" in new Setup {
      val result = execute(underTest.showCreateUserPage())

      contentAsString(result) should include(navLinks.head.label)
    }

    "displays the page without the links when retrieving the links fail" in new Setup {
    withSuppressedLoggingFrom(Logger, "expected test error") { suppressedLogs =>
        when(mockNavigationService.headerNavigation()(*))
          .thenReturn(failed(UpstreamErrorResponse("expected test error", 500)))

        val result = execute(underTest.showCreateUserPage())

        contentAsString(result) should (include("Create test user") and not include navLinks.head.label)
      }
    }
  }

  "createUser" should {
    "create an individual when the user type is INDIVIDUAL" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("userType", "INDIVIDUAL"))

      val result = execute(underTest.createUser(), request)

      contentAsString(result) should include(individual.userId)
    }

    "create an organisation when the user type is ORGANISATION" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("userType", "ORGANISATION"))

      val result = execute(underTest.createUser(), request)

      contentAsString(result) should include(organisation.userId)
    }

    "display an error message when the user type is not defined" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("userType", ""))
      
      val result = execute(underTest.createUser(), request)

      contentAsString(result) should include(underTest.messagesApi(FormKeys.createUserTypeNoChoiceKey)(Lang.defaultLang))
    }

    "display the logged in navigation links" in new Setup {
      val individualRequest = FakeRequest().withFormUrlEncodedBody(("userType", "INDIVIDUAL"))

      val result = execute(underTest.createUser(), individualRequest)

      contentAsString(result) should include(navLinks.head.label)
    }

    "display the page without the links when retrieving the links fail" in new Setup {
      val individualRequest = FakeRequest().withFormUrlEncodedBody(("userType", "INDIVIDUAL"))

      withSuppressedLoggingFrom(Logger, "expected test error") { suppressedLogs =>
        when(mockNavigationService.headerNavigation()(*))
          .thenReturn(failed(UpstreamErrorResponse("expected test error", 500)))

        val result = execute(underTest.createUser(), individualRequest)

        contentAsString(result) should (include(individual.userId) and not include navLinks.head.label)
      }
    }
  }
}
