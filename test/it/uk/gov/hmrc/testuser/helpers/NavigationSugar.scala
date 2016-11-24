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

package it.uk.gov.hmrc.testuser.helpers

import org.openqa.selenium.support.ui.{ExpectedCondition, WebDriverWait}
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.concurrent.Eventually
import org.scalatest.selenium.WebBrowser
import org.scalatest.selenium.WebBrowser.{go => goo}
import org.scalatest.{Assertions, Matchers}

trait NavigationSugar extends WebBrowser with Eventually with Assertions with Matchers {

  def goOn(page: WebPage)(implicit webDriver: WebDriver) = {
    go(page)
    on(page)
  }

  def go(page: WebLink)(implicit webDriver: WebDriver) = {
    goo to page
  }

  def clickOnSubmit() (implicit webDriver: WebDriver) = {
    webDriver.findElement(By.id("submit")).click()
  }

  def clickOnElement(locator: By)(implicit webDriver: WebDriver) = {
    webDriver.findElement(locator).click()
  }

  def verifyText(selectorId: String, expected: String)(implicit webDriver: WebDriver) = {
    webDriver.findElement(By.cssSelector(s"[$selectorId]")).getText shouldBe expected
  }

  def on(page: WebPage)(implicit webDriver: WebDriver) = {
    eventually {
      webDriver.findElement(By.tagName("body"))
    }
    withClue(s"Currently in page: $currentUrl " + find(tagName("h1")).map(_.text).fold(" - ")(h1 => s", with title '$h1' - ")) {
      assert(page.isCurrentPage, s"Page was not loaded: ${page.url}")
    }
  }
}
