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

import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.testuser.connectors.ApiPlatformTestUserConnector
import uk.gov.hmrc.testuser.models.UserTypes._
import uk.gov.hmrc.testuser.models.{Field, Service, TestIndividual, TestOrganisation}
import uk.gov.hmrc.test.utils.AsyncHmrcSpec

import scala.concurrent.Future.successful
import scala.concurrent.ExecutionContext.Implicits.global

class TestUserServiceSpec extends AsyncHmrcSpec {

  private val service1 = "service1"
  private val service2 = "service2"
  private val service3 = "service3"
  private val service4 = "service4"

  private val services = Seq(
    Service(service1, "Service 1", Seq(INDIVIDUAL)),
    Service(service2, "Service 2", Seq(INDIVIDUAL, ORGANISATION)),
    Service(service3, "Service 3", Seq(ORGANISATION)),
    Service(service4, "Service 4", Seq(AGENT))
  )

  trait Setup {
    implicit val hc                      = HeaderCarrier()
    val mockApiPlatformTestUserConnector = mock[ApiPlatformTestUserConnector]
    val underTest                        = new TestUserService(mockApiPlatformTestUserConnector)

    when(mockApiPlatformTestUserConnector.getServices()(*)).thenReturn(successful(services))
  }

  "createUser" should {
    "return a generated individual when type is INDIVIDUAL" in new Setup {
      private val fields = Seq(Field("saUtr", "Self Assessment UTR", "1555369052"), Field("nino", "", "CC333333C"), Field("vrn", "", "999902541"))
      val individual     = TestIndividual("user", "password", fields)
      when(mockApiPlatformTestUserConnector.createIndividual(eqTo(Seq(service1, service2)))(*)).thenReturn(successful(individual))

      val result = await(underTest.createUser(INDIVIDUAL))

      result shouldBe individual
    }

    "return a generated organisation when type is ORGANISATION" in new Setup {
      val organisation = TestOrganisation("org-user", "org-password", Seq(Field("saUtr", "Self Assessment UTR", "1555369053")))

      when(mockApiPlatformTestUserConnector.createOrganisation(eqTo(Seq(service2, service3)))(*)).thenReturn(successful(organisation))

      val result = await(underTest.createUser(ORGANISATION))

      result shouldBe organisation
    }
  }

}
