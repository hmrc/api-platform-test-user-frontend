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
import play.api.http.Status.OK
import play.api.libs.json.Json.{stringify, toJson}
import uk.gov.hmrc.http.{HeaderCarrier, Upstream5xxResponse}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.NavLink

class ThirdPartyDeveloperFrontendConnectorSpec extends UnitSpec with WiremockSugar with WithFakeApplication {

  trait Setup {
    implicit val hc = HeaderCarrier()

    val underTest = new ThirdPartyDeveloperFrontendConnector {
      override lazy val serviceUrl = wireMockUrl
    }
  }

  "fetchNavLinks" should {
    "return the list of navigation links" in new Setup {
      val links = Seq(NavLink("Sign in", "/sign-in", truncate = true))

      stubFor(get(urlEqualTo("/developer/user-navlinks")).willReturn(aResponse().withStatus(OK).withBody(stringify(toJson(links)))))

      val result = await(underTest.fetchNavLinks())

      result shouldBe links
    }

    "fail when third-party-developer-frontend return an error" in new Setup {

      stubFor(get(urlEqualTo("/developer/user-navlinks")).willReturn(aResponse().withStatus(500)))

      intercept[Upstream5xxResponse](await(underTest.fetchNavLinks()))
    }
  }
}
