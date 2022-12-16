/*
 * Copyright 2022 HM Revenue & Customs
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

import java.net.URL

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.remote.{RemoteWebDriver}
import org.openqa.selenium.{Dimension, WebDriver}

import scala.util.{Properties, Try}
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxDriver
import uk.gov.hmrc.webdriver.SingletonDriver

trait Env {
  lazy val windowSize = new Dimension(1024, 800)
  lazy val driver: WebDriver = createWebDriver()

  private lazy val browser = Properties.propOrElse("browser","chrome")
  private lazy val accessibilityTest = Properties.propOrElse("accessibility.test","false") == "true"

  private def createWebDriver(): WebDriver = {
    val driver = browser match {
      case "chrome" => if(accessibilityTest) SingletonDriver.getInstance() else createChromeDriver()
      case "remote-chrome" => createRemoteChromeDriver()
      case "firefox" => createFirefoxDriver()
      case "remote-firefox" => createRemoteFirefoxDriver()
    }
    driver.manage().deleteAllCookies()
    driver.manage().window().setSize(new Dimension(1280, 720))
    driver
  }

  def createFirefoxDriver(): WebDriver = {
    val options = new FirefoxOptions().setAcceptInsecureCerts(true)
    new FirefoxDriver(options)
  }

  def createRemoteFirefoxDriver() = {
    val browserOptions = new FirefoxOptions().setAcceptInsecureCerts(true)
    new RemoteWebDriver(new URL(s"http://localhost:4444/wd/hub"), browserOptions)
  }

  private def createChromeDriver(): WebDriver = {
    val options = new ChromeOptions()
    options.addArguments("--headless")
    options.addArguments("--proxy-server='direct://'")
    options.addArguments("--proxy-bypass-list=*")
    new ChromeDriver(options)
  }

  private def createRemoteChromeDriver() = {
    val browserOptions: ChromeOptions = new ChromeOptions()
    browserOptions.addArguments("--headless")
    browserOptions.addArguments("--proxy-server='direct://'")
    browserOptions.addArguments("--proxy-bypass-list=*")

    new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), browserOptions)
  }

  def shutdown = Try(driver.quit())

  sys addShutdownHook {
    shutdown
  }
}

object Env extends Env
