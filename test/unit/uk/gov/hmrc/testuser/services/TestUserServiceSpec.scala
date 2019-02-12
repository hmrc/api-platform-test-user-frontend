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

package uk.gov.hmrc.testuser.services

import org.mockito.BDDMockito.given
import org.scalatest.mockito.MockitoSugar
import uk.gov.hmrc.domain._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.connectors.ApiPlatformTestUserConnector
import uk.gov.hmrc.testuser.models.UserType.{INDIVIDUAL, ORGANISATION}
import uk.gov.hmrc.testuser.models.{TestIndividual, TestOrganisation}

class TestUserServiceSpec extends UnitSpec with MockitoSugar {

  trait Setup {
    implicit val hc = HeaderCarrier()
    val mockApiPlatformTestUserConnector = mock[ApiPlatformTestUserConnector]
    val underTest = new TestUserService(mockApiPlatformTestUserConnector)
  }

  "createUser" should {
    "return a generated individual when type is INDIVIDUAL" in new Setup {

      val individual = TestIndividual("user", "password", SaUtr("1555369052"), Nino("CC333333C"))
      given(mockApiPlatformTestUserConnector.createIndividual()).willReturn(individual)

      val result = await(underTest.createUser(INDIVIDUAL))

      result shouldBe individual
    }

    "return a generated organisation when type is ORGANISATION" in new Setup {

      val organisation = TestOrganisation("user", "password", SaUtr("1555369052"), EmpRef("555", "EIA000"),
        CtUtr("1555369053"), Vrn("999902541"))
      given(mockApiPlatformTestUserConnector.createOrganisation()).willReturn(organisation)

      val result = await(underTest.createUser(ORGANISATION))

      result shouldBe organisation
    }
  }

}
