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

package uk.gov.hmrc.testuser.pages

import uk.gov.hmrc.testuser.helpers.WebPage
import org.openqa.selenium.By

class CreateTestUserPage(port: Int) extends WebPage {
  override val url: String = s"http://localhost:$port/api-test-user"

  override def isCurrentPage: Boolean = find(cssSelector("h1")).fold(false)(_.text == "Create a test user")
}

object CreateTestUserPage {
  val individualCheckbox: By   = By.id("Individual")
  val organisationCheckbox: By = By.id("Organisation")
}
