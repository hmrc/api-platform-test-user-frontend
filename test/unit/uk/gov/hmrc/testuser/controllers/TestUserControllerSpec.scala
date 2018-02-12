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

package unit.uk.gov.hmrc.testuser.controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import common.LogSuppressing
import org.mockito.BDDMockito.given
import org.mockito.Matchers.{any, refEq}
import org.scalatest.mockito.MockitoSugar
import play.api.Logger
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import uk.gov.hmrc.domain._
import uk.gov.hmrc.http.{HeaderCarrier, Upstream5xxResponse}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.controllers.TestUserController
import uk.gov.hmrc.testuser.models.UserType.{INDIVIDUAL, ORGANISATION}
import uk.gov.hmrc.testuser.models.{NavLink, TestIndividual, TestOrganisation}
import uk.gov.hmrc.testuser.services.{NavigationService, TestUserService}
import org.scalatestplus.play.guice.GuiceOneAppPerTest

import scala.concurrent.Future.failed

class TestUserControllerSpec extends UnitSpec with MockitoSugar with GuiceOneAppPerTest with LogSuppressing {

  val individual = TestIndividual("ind-user", "ind-password", SaUtr("1555369052"), Nino("CC333333C"))
  val organisation = TestOrganisation("org-user", "org-password", SaUtr("1555369053"), EmpRef("555","EIA000"),
    CtUtr("1555369053"), Vrn("999902541"))

  val individualRequest = FakeRequest().withFormUrlEncodedBody(("type", "INDIVIDUAL"))

  trait Setup {
    implicit val materializer = ActorMaterializer.create(ActorSystem.create())
    private val csrfAddToken = app.injector.instanceOf[play.filters.csrf.CSRFAddToken]
    val navLinks = Seq(NavLink("sign-in", "http://sign-in"))

    val underTest = new TestUserController {
      override def messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
      override val testUserService: TestUserService = mock[TestUserService]
      override val navigationService: NavigationService = mock[NavigationService]
    }

    given(underTest.testUserService.createUser(refEq(INDIVIDUAL))(any[HeaderCarrier]())).willReturn(individual)
    given(underTest.testUserService.createUser(refEq(ORGANISATION))(any[HeaderCarrier]())).willReturn(organisation)
    given(underTest.navigationService.headerNavigation()(any[HeaderCarrier]())).willReturn(navLinks)

    def execute[T <: play.api.mvc.AnyContent](action: Action[AnyContent], request: FakeRequest[T] = FakeRequest()) = await(csrfAddToken(action)(request))
  }

  "showCreateTestUser" should {
    "display the Create test user page" in new Setup {

      val result = execute(underTest.showCreateUserPage())

      bodyOf(result) should include ("Create test user")
    }

    "display the logged in navigation links" in new Setup {

      val result = execute(underTest.showCreateUserPage())

      bodyOf(result) should include (navLinks.head.label)
    }

    "displays the page without the links when retrieving the links fail" in new Setup {
      withSuppressedLoggingFrom(Logger, "expected test error") { suppressedLogs =>
        given(underTest.navigationService.headerNavigation()(any[HeaderCarrier]()))
          .willReturn(failed(Upstream5xxResponse("expected test error", 500, 500)))

        val result = execute(underTest.showCreateUserPage())

        bodyOf(result) should (include("Create test user") and not include navLinks.head.label)
      }
    }
  }

  "createUser" should {
    "create an individual when type is INDIVIDUAL" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("type", "INDIVIDUAL"))

      val result = execute(underTest.createUser(), request)

      bodyOf(result) should include (individual.userId)
    }

    "create an organisation when type is ORGANISATION" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("type", "ORGANISATION"))

      val result = execute(underTest.createUser(), request)

      bodyOf(result) should include (organisation.userId)
    }

    "display the logged in navigation links" in new Setup {
      val result = execute(underTest.createUser(), individualRequest)

      bodyOf(result) should include (navLinks.head.label)
    }

    "displays the page without the links when retrieving the links fail" in new Setup {
      withSuppressedLoggingFrom(Logger, "expected test error") { suppressedLogs =>
        given(underTest.navigationService.headerNavigation()(any[HeaderCarrier]()))
          .willReturn(failed(Upstream5xxResponse("expected test error", 500, 500)))

        val result = execute(underTest.createUser(), individualRequest)

        bodyOf(result) should (include(individual.userId) and not include navLinks.head.label)
      }
    }
  }
}
