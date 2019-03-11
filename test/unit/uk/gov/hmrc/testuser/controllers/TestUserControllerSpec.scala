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

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.jsoup.Jsoup
import org.mockito.BDDMockito.given
import org.mockito.Matchers.{any, refEq}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Logger
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import uk.gov.hmrc.http.{HeaderCarrier, Upstream5xxResponse}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.common.LogSuppressing
import uk.gov.hmrc.testuser.config.AppConfig
import uk.gov.hmrc.testuser.models.UserTypes.{INDIVIDUAL, ORGANISATION}
import uk.gov.hmrc.testuser.models.{Field, NavLink, TestIndividual, TestOrganisation}
import uk.gov.hmrc.testuser.services.{NavigationService, TestUserService}

import scala.concurrent.Future.failed

class TestUserControllerSpec extends UnitSpec with MockitoSugar with GuiceOneAppPerTest with LogSuppressing {

  private val individualFields = Seq(Field("saUtr", "Self Assessment UTR", "1555369052"), Field("nino", "","CC333333C"), Field("vrn", "", "999902541"))
  val individual = TestIndividual("ind-user", "ind-password", individualFields)

  private val organisationFields = Seq(
    Field("saUtr", "Self Assessment UTR", "1555369053"),
    Field("empRef", "Employer Ref", "555/EIA000"),
    Field("ctUtr", "CT UTR", "1555369054"),
    Field("vrn", "", "999902541"))
  val organisation = TestOrganisation("org-user", "org-password", organisationFields)

  val individualRequest = FakeRequest().withFormUrlEncodedBody(("userType", "INDIVIDUAL"))

  trait Setup {
    implicit val materializer = ActorMaterializer.create(ActorSystem.create())
    private val csrfAddToken = app.injector.instanceOf[play.filters.csrf.CSRFAddToken]
    val navLinks = Seq(NavLink("sign-in", "http://sign-in"))

    val mockTestUserService = mock[TestUserService]
    val mockNavigationService = mock[NavigationService]
    val underTest = new TestUserController(
      app.injector.instanceOf[MessagesApi],
      mockTestUserService,
      mockNavigationService,
      app.injector.instanceOf[AppConfig]
    )

    given(mockTestUserService.createUser(refEq(INDIVIDUAL))(any[HeaderCarrier]())).willReturn(individual)
    given(mockTestUserService.createUser(refEq(ORGANISATION))(any[HeaderCarrier]())).willReturn(organisation)
    given(mockNavigationService.headerNavigation()(any[HeaderCarrier]())).willReturn(navLinks)

    def execute[T <: play.api.mvc.AnyContent](action: Action[AnyContent], request: FakeRequest[T] = FakeRequest()) = await(csrfAddToken(action)(request))
  }

  "showCreateTestUser" should {
    "display the Create test user page" in new Setup {

      val result = execute(underTest.showCreateUserPage())
      val page = bodyOf(result)

      page should include("Create test user")

      val document = Jsoup.parse(page)

      document.getElementById("Individual").hasAttr("checked") shouldBe false
      document.getElementById("Organisation").hasAttr("checked") shouldBe false
    }

    "display the logged in navigation links" in new Setup {

      val result = execute(underTest.showCreateUserPage())

      bodyOf(result) should include(navLinks.head.label)
    }

    "displays the page without the links when retrieving the links fail" in new Setup {
      withSuppressedLoggingFrom(Logger, "expected test error") { suppressedLogs =>
        given(mockNavigationService.headerNavigation()(any[HeaderCarrier]()))
          .willReturn(failed(Upstream5xxResponse("expected test error", 500, 500)))

        val result = execute(underTest.showCreateUserPage())

        bodyOf(result) should (include("Create test user") and not include navLinks.head.label)
      }
    }
  }

  "createUser" should {
    "create an individual when the user type is INDIVIDUAL" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("userType", "INDIVIDUAL"))

      val result = execute(underTest.createUser(), request)

      bodyOf(result) should include(individual.userId)
    }

    "create an organisation when the user type is ORGANISATION" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("userType", "ORGANISATION"))

      val result = execute(underTest.createUser(), request)

      bodyOf(result) should include(organisation.userId)
    }

    "display an error message when the user type is not defined" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("userType", ""))

      val result = execute(underTest.createUser(), request)

      bodyOf(result) should include(underTest.messagesApi(FormKeys.createUserTypeNoChoiceKey))
    }

    "display the logged in navigation links" in new Setup {
      val result = execute(underTest.createUser(), individualRequest)

      bodyOf(result) should include(navLinks.head.label)
    }

    "display the page without the links when retrieving the links fail" in new Setup {
      withSuppressedLoggingFrom(Logger, "expected test error") { suppressedLogs =>
        given(mockNavigationService.headerNavigation()(any[HeaderCarrier]()))
          .willReturn(failed(Upstream5xxResponse("expected test error", 500, 500)))

        val result = execute(underTest.createUser(), individualRequest)

        bodyOf(result) should (include(individual.userId) and not include navLinks.head.label)
      }
    }
  }
}
