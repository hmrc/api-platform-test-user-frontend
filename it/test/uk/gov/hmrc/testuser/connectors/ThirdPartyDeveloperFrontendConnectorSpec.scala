/*
 * Copyright 2023 HM Revenue & Customs
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

import scala.concurrent.ExecutionContext.Implicits.global

import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json.{stringify, toJson}
import play.api.{Application, Configuration, Environment}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, UpstreamErrorResponse}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.test.utils.AsyncHmrcSpec

import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.NavLink

class ThirdPartyDeveloperFrontendConnectorSpec extends AsyncHmrcSpec with WiremockSugar with GuiceOneAppPerSuite {

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .configure(("metrics.jvm", false))
      .build()

  trait Setup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    val underTest = new ThirdPartyDeveloperFrontendConnector(
      app.injector.instanceOf[HttpClientV2],
      app.injector.instanceOf[Configuration],
      app.injector.instanceOf[Environment],
      app.injector.instanceOf[ServicesConfig]
    ) {
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
      stubFor(get(urlEqualTo("/developer/user-navlinks")).willReturn(aResponse().withStatus(INTERNAL_SERVER_ERROR)))

      intercept[UpstreamErrorResponse](
        await(underTest.fetchNavLinks())
      ).statusCode shouldBe INTERNAL_SERVER_ERROR
    }
  }
}
