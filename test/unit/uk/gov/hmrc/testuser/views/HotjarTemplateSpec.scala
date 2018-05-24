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

package unit.uk.gov.hmrc.testuser.views

import junit.framework.TestCase
import org.mockito.BDDMockito.given
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.i18n.Messages.Implicits.applicationMessages
import play.api.mvc.Request
import uk.gov.hmrc.domain.{Nino, SaUtr}
import uk.gov.hmrc.testuser.config.AppConfig
import uk.gov.hmrc.testuser.models.NavLink
import uk.gov.hmrc.testuser.views.html._
import uk.gov.hmrc.testuser.models.TestIndividual

class HotjarTemplateSpec extends PlaySpec with MockitoSugar with OneAppPerSuite {

  trait Setup {
    val pageTitle = "pageTitle"
    val testUser = TestIndividual("id", "password", SaUtr("2234567890"), Nino("AA000003D"))
    val navLinks = Seq[NavLink]()
    val mockRequest = mock[Request[Any]]
    val mockApplicationConfig = mock[AppConfig]

    given(mockApplicationConfig.analyticsToken) willReturn ""
    given(mockApplicationConfig.analyticsHost) willReturn ""
  }

  "htmlView" must {
    "render hotjar script when hotjar id is defined and hotjar feature enabled" in new Setup {
      given(mockApplicationConfig.hotjarEnabled) willReturn true
      given(mockApplicationConfig.hotjarId) willReturn 123

      val renderedHtml = test_user.render(navLinks, testUser, mockRequest, applicationMessages, mockApplicationConfig)
      renderedHtml.body must include("hotjar")
      renderedHtml.body must include("hjid:123")
    }

    "render without hotjar script when hotjar id is defined and hotjar feature is disabled" in new Setup {
      given(mockApplicationConfig.hotjarEnabled) willReturn false

      val renderedHtml = test_user.render(navLinks, testUser, mockRequest, applicationMessages, mockApplicationConfig)
      renderedHtml.body must not include "hotjar"
      renderedHtml.body must not include "hjid:"
    }
  }
}

