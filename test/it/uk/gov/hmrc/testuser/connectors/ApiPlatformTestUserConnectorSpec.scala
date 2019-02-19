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

package uk.gov.hmrc.testuser.connectors

import com.github.tomakehurst.wiremock.client.WireMock._
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.json.Json.toJson
import play.api.{Configuration, Environment}
import uk.gov.hmrc.domain._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.{Service, TestIndividual, TestOrganisation, UserTypes}
import uk.gov.hmrc.testuser.models.ServiceNames._
import uk.gov.hmrc.testuser.models.UserTypes.INDIVIDUAL


class ApiPlatformTestUserConnectorSpec extends UnitSpec with WiremockSugar with WithFakeApplication {

  trait Setup {
    implicit val hc = HeaderCarrier()

    val underTest = new ApiPlatformTestUserConnector(
      fakeApplication.injector.instanceOf[ProxiedHttpClient],
      fakeApplication.injector.instanceOf[Configuration],
      fakeApplication.injector.instanceOf[Environment]
    ) {
      override val serviceUrl: String = wireMockUrl
    }
  }

  "createIndividual" should {
    "return a generated individual" in new Setup {
      val testIndividual = TestIndividual("user", "password", SaUtr("1555369052"), Nino("CC333333C"))

      val requestPayload = """{ "serviceNames": [ "national-insurance", "self-assessment", "mtd-income-tax" ] }"""

      stubFor(post(urlEqualTo("/individuals")).withRequestBody(equalToJson(requestPayload))
        .willReturn(aResponse().withStatus(CREATED).withBody(toJson(testIndividual).toString())))

      val result = await(underTest.createIndividual(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX)))

      result shouldBe testIndividual
    }

    "fail when api-platform-test-user returns a response that is not 201 CREATED" in new Setup {

      stubFor(post(urlEqualTo("/individuals")).willReturn(aResponse().withStatus(OK)))

      intercept[RuntimeException](await(underTest.createIndividual(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX))))
    }
  }

  "createOrganisation" should {
    "return a generated organisation" in new Setup {
      val testOrganisation = TestOrganisation("user", "password", SaUtr("1555369052"), EmpRef("555", "EIA000"),
        CtUtr("1555369053"), Vrn("999902541"))

      val requestPayload =
        s"""{
           |  "serviceNames": [
           |    "national-insurance",
           |    "self-assessment",
           |    "mtd-income-tax",
           |    "corporation-tax",
           |    "paye-for-employers",
           |    "submit-vat-returns"
           |  ]
           |}""".stripMargin

      stubFor(post(urlEqualTo("/organisations")).withRequestBody(equalToJson(requestPayload))
        .willReturn(aResponse().withStatus(CREATED).withBody(toJson(testOrganisation).toString())))

      val result = await(underTest.createOrganisation(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX,
            CORPORATION_TAX, PAYE_FOR_EMPLOYERS, SUBMIT_VAT_RETURNS)))

      result shouldBe testOrganisation
    }

    "fail when api-platform-test-user returns a response that is not 201 CREATED" in new Setup {

      stubFor(post(urlEqualTo("/organisations")).willReturn(aResponse().withStatus(OK)))

      intercept[RuntimeException](await(underTest.createOrganisation(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX,
            CORPORATION_TAX, PAYE_FOR_EMPLOYERS, SUBMIT_VAT_RETURNS))))
    }
  }

  "getServices" when {
    "api-platform-test-user returns a 200 OK response" should {
      "return the services from api-platform-test-user" in new Setup {
        val services = Seq(Service("service-1", "Service One", Seq(UserTypes.INDIVIDUAL)))
        stubFor(get(urlEqualTo("/services")).willReturn(aResponse().withBody(Json.toJson(services).toString()).withStatus(OK)))

        val result = await(underTest.getServices())

        result shouldBe services
      }
    }

    "api-platform-test-user returns a response other than 200 OK" should {
      "throw runtime exception" in new Setup {
        stubFor(get(urlEqualTo("/services")).willReturn(aResponse().withStatus(CREATED)))

        intercept[RuntimeException](await(underTest.getServices()))
      }
    }
  }
}
