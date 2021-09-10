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

package uk.gov.hmrc.testuser.connectors

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.NavLink

import uk.gov.hmrc.http.HttpReads.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ThirdPartyDeveloperFrontendConnector @Inject()(httpClient: HttpClient,
                                                     runModeConfiguration: Configuration,
                                                     environment: Environment,
                                                     servicesConfig: ServicesConfig) {
  lazy val serviceUrl = servicesConfig.baseUrl("third-party-developer-frontend")

  def fetchNavLinks()(implicit hc: HeaderCarrier): Future[Seq[NavLink]] = {
    httpClient.GET[Seq[NavLink]](s"$serviceUrl/developer/user-navlinks")
  }
}
