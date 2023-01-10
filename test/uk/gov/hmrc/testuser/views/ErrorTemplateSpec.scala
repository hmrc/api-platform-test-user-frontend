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

package uk.gov.hmrc.testuser.views

import javax.inject.Inject

import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import play.api.i18n.Messages
import play.api.test.FakeRequest
import uk.gov.hmrc.test.utils.HmrcSpec

import uk.gov.hmrc.testuser.config.ApplicationConfig
import uk.gov.hmrc.testuser.views.html.ErrorTemplate

class ErrorTemplateSpec @Inject() (errorTemplate: ErrorTemplate) extends HmrcSpec with GuiceOneAppPerSuite {
  "Error template page" should {
    "render correctly when given title, heading and message" in {
      val message = "Error Message"

      val messages                  = app.injector.instanceOf[Messages]
      val config: ApplicationConfig = app.injector.instanceOf[ApplicationConfig]
      val page                      = errorTemplate.render("", "", message, FakeRequest(), messages, config)

      page.body should include(message)
    }
  }
}
