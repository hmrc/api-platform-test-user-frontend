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

import uk.gov.hmrc.testuser.helpers.{WebPage, Env}
import org.openqa.selenium.By


object CreateTestUserPage extends WebPage {
  override val url: String = s"http://localhost:${Env.port}/api-test-user"

  override val pageTitle = "Create a test user"

  def selectIndividual(): Unit = {
    selectCheckbox(By.id("Individual"))
  }

  def selectOrganisation(): Unit = {
    selectCheckbox(By.id("Organisation"))
  }

  def clickOnSubmit(): Unit = {
    click(By.id("submit"))
  }

  def getPassword(): String = {
    getByCssSelector("data-password")
  }

  def getUserId(): String = {
    getByCssSelector("data-userid")
  }

  def getSaUtr(): String = {
    getByCssSelector("data-sautr")
  }

  def getCtUtr(): String = {
    getByCssSelector("data-ctutr")
  }

  def getEmpRef(): String = {
    getByCssSelector("data-empref")
  }

  def getVrn(): String = {
    getByCssSelector("data-vrn")
  }

  def getNino(): String = {
    getByCssSelector("data-nino")
  }

  private def getByCssSelector(fieldName: String): String = {
    getText(By.cssSelector(s"[$fieldName]"))
  }
}
