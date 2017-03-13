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

package unit.uk.gov.hmrc.testuser.connectors

import com.github.tomakehurst.wiremock.client.WireMock._
import play.api.libs.json.Json.toJson
import uk.gov.hmrc.domain._
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.test.{WithFakeApplication, UnitSpec}
import uk.gov.hmrc.testuser.config.WSHttp
import uk.gov.hmrc.testuser.connectors.ApiPlatformTestUserConnector
import uk.gov.hmrc.testuser.models.{TestOrganisation, TestIndividual}
import uk.gov.hmrc.testuser.models.JsonFormatters._

class ApiPlatformTestUserConnectorSpec extends UnitSpec with WiremockSugar with WithFakeApplication {

  trait Setup {
    implicit val hc = HeaderCarrier()

    val underTest = new ApiPlatformTestUserConnector {
      override val serviceUrl: String = wireMockUrl
      override val http = WSHttp
    }
  }

  "createIndividual" should {
    "return a generated individual" in new Setup {
      val testIndividual = TestIndividual("user", "password", SaUtr("1555369052"), Nino("CC333333C"))

      stubFor(post(urlEqualTo("/individual")).willReturn(aResponse().withStatus(201).withBody(toJson(testIndividual).toString())))

      val result = await(underTest.createIndividual())

      result shouldBe testIndividual
    }

    "fail when api-platform-test-user return an error" in new Setup {

      stubFor(post(urlEqualTo("/individual")).willReturn(aResponse().withStatus(500)))

      intercept[RuntimeException](await(underTest.createIndividual()))
    }
  }

  "createOrganisation" should {
    "return a generated organisation" in new Setup {
      val testOrganisation = TestOrganisation("user", "password", SaUtr("1555369052"), EmpRef("555","EIA000"),
        CtUtr("1555369053"), Vrn("999902541"))

      stubFor(post(urlEqualTo("/organisation")).willReturn(aResponse().withStatus(201).withBody(toJson(testOrganisation).toString())))

      val result = await(underTest.createOrganisation())

      result shouldBe testOrganisation
    }

    "fail when api-platform-test-user return an error" in new Setup {

      stubFor(post(urlEqualTo("/organisation")).willReturn(aResponse().withStatus(500)))

      intercept[RuntimeException](await(underTest.createOrganisation()))
    }
  }
}
