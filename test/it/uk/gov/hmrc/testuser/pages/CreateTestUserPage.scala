/*
 * Copyright 2016 HM Revenue & Customs
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

package it.uk.gov.hmrc.testuser.pages

import it.uk.gov.hmrc.testuser.helpers.WebPage
import org.openqa.selenium.By

object CreateTestUserPage extends WebPage {

  override val url: String = "http://localhost:9000/api-test-user"

  override def isCurrentPage: Boolean = find(cssSelector("h1")).fold(false)(_.text == "Create test user")

  val createIndividualButton: By = By.cssSelector(s"[create-individual]")

  val createOrganisationButton: By = By.cssSelector(s"[create-organisation]")

}
