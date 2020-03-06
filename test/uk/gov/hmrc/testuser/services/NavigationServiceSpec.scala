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
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.connectors.ThirdPartyDeveloperFrontendConnector
import uk.gov.hmrc.testuser.models.NavLink

import scala.concurrent.Future.failed

class NavigationServiceSpec extends UnitSpec with MockitoSugar {

  trait Setup {
    implicit val hc = HeaderCarrier()
    val connector = mock[ThirdPartyDeveloperFrontendConnector]
    val configuration = mock[Configuration]

    val underTest = new NavigationService(connector, configuration)
  }

  "headerNavigation" should {
    "return the navigation links" in new Setup {
      val navLinks = Seq(NavLink("sign-in", "/sign-in"))

      given(connector.fetchNavLinks()).willReturn(navLinks)
      given(configuration.getString("third-party-developer-frontend.host")).willReturn(None)

      val result = await(underTest.headerNavigation())

      result shouldBe navLinks
    }

    "prefix the links with local environment when it is set" in new Setup {
      val navLinks = Seq(NavLink("sign-in", "/sign-in"))

      given(connector.fetchNavLinks()).willReturn(navLinks)
      given(configuration.getOptional[String]("third-party-developer-frontend.host")).willReturn(Some("http://localhost:1111"))

      val result = await(underTest.headerNavigation())

      result shouldBe Seq(NavLink("sign-in", "http://localhost:1111/sign-in"))
    }

    "fail when the links can not be retrieved" in new Setup {

      given(connector.fetchNavLinks()).willReturn(failed(new RuntimeException("test failure")))

      intercept[RuntimeException]{await(underTest.headerNavigation())}
    }
  }
}
