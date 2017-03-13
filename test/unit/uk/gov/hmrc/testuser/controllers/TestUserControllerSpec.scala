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

package unit.uk.gov.hmrc.testuser.controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.mockito.BDDMockito.given
import org.mockito.Matchers.{refEq, any}
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.OneAppPerTest
import play.api.i18n.MessagesApi
import play.api.mvc.{AnyContentAsFormUrlEncoded, AnyContent, Action}
import play.api.test.FakeRequest
import uk.gov.hmrc.domain._
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.controllers.TestUserController
import uk.gov.hmrc.testuser.models.UserType.{ORGANISATION, INDIVIDUAL}
import uk.gov.hmrc.testuser.models.{TestOrganisation, TestIndividual, UserType}
import uk.gov.hmrc.testuser.services.TestUserService

class TestUserControllerSpec extends UnitSpec with MockitoSugar with OneAppPerTest {

  val individual = TestIndividual("ind-user", "ind-password", SaUtr("1555369052"), Nino("CC333333C"))
  val organisation = TestOrganisation("org-user", "org-password", SaUtr("1555369053"), EmpRef("555","EIA000"),
    CtUtr("1555369053"), Vrn("999902541"))

  trait Setup {
    implicit val materializer = ActorMaterializer.create(ActorSystem.create())
    private val csrfAddToken = app.injector.instanceOf[play.filters.csrf.CSRFAddToken]

    val underTest = new TestUserController {
      override def messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
      override val testUserService: TestUserService = mock[TestUserService]
    }

    given(underTest.testUserService.createUser(refEq(INDIVIDUAL))(any[HeaderCarrier]())).willReturn(individual)
    given(underTest.testUserService.createUser(refEq(ORGANISATION))(any[HeaderCarrier]())).willReturn(organisation)

    def execute[T <: play.api.mvc.AnyContent](action: Action[AnyContent], request: FakeRequest[T] = FakeRequest()) = await(csrfAddToken(action)(request))
  }

  "showCreateTestUser" should {
    "display the Create test user page" in new Setup {

      val result = execute(underTest.showCreateUserPage())

      bodyOf(result) should include ("Create test user")
    }
  }

  "createUser" should {
    "create an individual when type is INDIVIDUAL" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("type", "INDIVIDUAL"))

      val result = execute(underTest.createUser(), request)

      bodyOf(result) should include (individual.username)
    }

    "create an organisation when type is ORGANISATION" in new Setup {
      val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest().withFormUrlEncodedBody(("type", "ORGANISATION"))

      val result = execute(underTest.createUser(), request)

      bodyOf(result) should include (organisation.username)
    }

  }

}
