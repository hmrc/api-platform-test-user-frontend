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

package it.uk.gov.hmrc.testuser.helpers

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import org.openqa.selenium.WebDriver

import scala.util.Try

trait Env {
  val driver: WebDriver = createWebDriver
  lazy val createWebDriver: WebDriver = {
    val targetBrowser = Option(System.getenv("test_driver")).getOrElse("firefox")
    targetBrowser match {
      case "chrome" => createChromeDriver()
      case "firefox" => createFirefoxDriver()
      case _ => throw new IllegalArgumentException(s"target browser $targetBrowser not recognised")
    }
  }

  def createChromeDriver(): WebDriver = {
    new ChromeDriver()
  }

  def createFirefoxDriver(): WebDriver = {
    val profile = new FirefoxProfile
    profile.setAcceptUntrustedCertificates(true)
    new FirefoxDriver(profile)
  }


  sys addShutdownHook {
    Try(driver.quit())
  }
}

object Env extends Env
