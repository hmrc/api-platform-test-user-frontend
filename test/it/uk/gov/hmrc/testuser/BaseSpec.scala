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

package it.uk.gov.hmrc.testuser

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import it.uk.gov.hmrc.testuser.helpers.{NavigationSugar, Env}
import it.uk.gov.hmrc.testuser.stubs.ApiPlatformTestUserStub
import org.openqa.selenium.WebDriver
import org.scalatest._
import org.scalatestplus.play.OneServerPerSuite
import play.api.inject.guice.GuiceApplicationBuilder

trait BaseSpec extends FeatureSpec with BeforeAndAfterAll with BeforeAndAfterEach with Matchers with OneServerPerSuite
with GivenWhenThen with NavigationSugar {

  override lazy val port = 9000
  implicit val webDriver: WebDriver = Env.driver

  implicit override lazy val app = GuiceApplicationBuilder().configure(Map(
        "auditing.enabled" -> false,
        "auditing.traceRequests" -> false,
        "microservice.services.api-platform-test-user.port" -> ApiPlatformTestUserStub.port)).build()

  val mocks = Seq(ApiPlatformTestUserStub)

  override protected def beforeEach(): Unit = {
    mocks.foreach(m => if (!m.server.isRunning) m.server.start())
  }

  override protected def afterEach(): Unit = {
    webDriver.manage().deleteAllCookies()
    mocks.foreach(_.mock.resetMappings())
  }

  override protected def afterAll(): Unit = {
    mocks.foreach(_.server.stop())
  }
}

case class MockHost(port: Int) {
  val server = new WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
  val mock = new WireMock("localhost", port)
  val url = s"http://localhost:$port"
}
