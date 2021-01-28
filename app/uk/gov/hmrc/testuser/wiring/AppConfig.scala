/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.testuser.wiring

import com.google.inject.ImplementedBy
import javax.inject.Inject
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@ImplementedBy(classOf[AppConfigImpl])
trait AppConfig {
  val appName: String

  val contactHost: String
  val contactFormServiceIdentifier: String

  val analyticsToken: String
  val analyticsHost: String

  val reportAProblemPartialUrl: String
  val reportAProblemNonJSUrl: String
}

class AppConfigImpl @Inject()(config: ServicesConfig) extends AppConfig {
  val appName = config.getString("appName")

  val contactHost = config.getString("contact-frontend.host")
  val contactFormServiceIdentifier = "MyService"

  val analyticsToken = config.getString("google-analytics.token")
  val analyticsHost = config.getString("google-analytics.host")

  val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
}
