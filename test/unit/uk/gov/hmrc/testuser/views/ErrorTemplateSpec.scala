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

import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.OneServerPerSuite
import play.api.i18n.Messages.Implicits.applicationMessages
import play.api.test.FakeRequest
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.config.AppConfig

class ErrorTemplateSpec extends UnitSpec with MockitoSugar with OneServerPerSuite {
  "Error template page" should {
    "render correctly when given title, heading and message" in {
      val message = "Error Message"

      val config = app.injector.instanceOf[AppConfig]

      val page = uk.gov.hmrc.testuser.views.html.error_template.render("", "", message, FakeRequest(), applicationMessages, config)

      page.body should include(message)
    }
  }
}
