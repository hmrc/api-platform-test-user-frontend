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

package uk.gov.hmrc.testuser.connectors

import javax.inject.{Singleton, Inject}

import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.testuser.config.WSHttp
import uk.gov.hmrc.testuser.models.NavLink
import uk.gov.hmrc.testuser.models.JsonFormatters._

import scala.concurrent.Future
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ThirdPartyDeveloperFrontendConnector @Inject()() extends ServicesConfig {

  lazy val serviceUrl = baseUrl("third-party-developer-frontend")

  def fetchNavLinks()(implicit hc: HeaderCarrier): Future[Seq[NavLink]] = {
    WSHttp.GET[Seq[NavLink]](s"$serviceUrl/developer/user-navlinks")
  }
}
