/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.testuser.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class AppConfig @Inject()(config: Configuration) {

  lazy val host: String = config.get[String]("host")

  lazy val cookies: String         = host + config.get[String]("urls.footer.cookies")
  lazy val privacy: String         = host + config.get[String]("urls.footer.privacy")
  lazy val termsConditions: String = host + config.get[String]("urls.footer.termsConditions")
  lazy val govukHelp: String       = config.get[String]("urls.footer.govukHelp")
  lazy val accessibility: String   = config.get[String]("urls.footer.accessibility")
}
