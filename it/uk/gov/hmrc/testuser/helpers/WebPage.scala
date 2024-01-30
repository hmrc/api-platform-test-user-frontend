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

package uk.gov.hmrc.testuser.helpers

import uk.gov.hmrc.selenium.component.PageObject
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.FluentWait
import uk.gov.hmrc.selenium.webdriver.Driver
import java.time.Duration
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions

trait WebPage extends PageObject {

  def url: String

  def pageTitle: String

  def isCurrentPage: Boolean

  def heading = getText(By.tagName("h1"))

  def bodyText = getText(By.tagName("body"))

  def goTo(): Unit = {
    get(url)
    println("**********************************")
    println(Driver.instance.getPageSource)
    println("**********************************")
    waitForElementToBePresent(By.cssSelector("service-info"))
  }

  private def waitForElementToBePresent(locator: By): WebElement = {
    fluentWait.until(ExpectedConditions.presenceOfElementLocated(locator))
  }

  // protected val continueButton: By = By.id("continue-button")
  private def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](Driver.instance)
    .withTimeout(Duration.ofSeconds(3))
    .pollingEvery(Duration.ofSeconds(1))
}
