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

package uk.gov.hmrc.testuser

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.openqa.selenium.WebDriver
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Mode}
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import uk.gov.hmrc.testuser.helpers.NavigationSugar
import uk.gov.hmrc.testuser.helpers.Env
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, TestData}
import org.scalatest.matchers.should.Matchers

trait BaseSpec extends AnyFeatureSpec with BeforeAndAfterAll with BeforeAndAfterEach with Matchers with NavigationSugar with GuiceOneServerPerTest {

  val stubPort = 11111
  val stubHost = "localhost"

  implicit val webDriver: WebDriver = Env.driver

  val wireMockServer = new WireMockServer(wireMockConfig()
    .port(stubPort))

  override def newAppForTest(testData: TestData): Application = {
    GuiceApplicationBuilder()
      .configure(
        "run.mode" -> "Stub",
        "microservice.services.api-platform-test-user.port" -> stubPort,
        "microservice.services.third-party-developer-frontend.port" -> stubPort
      )
      .in(Mode.Prod)
      .build()
  }

  override def beforeAll() = {
    wireMockServer.start()
    WireMock.configureFor(stubHost, stubPort)
  }

  override def afterAll() = {
    wireMockServer.stop()
  }

  override def beforeEach() = {
    webDriver.manage().deleteAllCookies()
    WireMock.reset()
  }
}
