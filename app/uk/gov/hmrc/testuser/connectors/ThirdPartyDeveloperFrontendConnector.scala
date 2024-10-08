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

package uk.gov.hmrc.testuser.connectors

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

import play.api.{Configuration, Environment}
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, _}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.NavLink

@Singleton
class ThirdPartyDeveloperFrontendConnector @Inject() (
    httpClient: HttpClientV2,
    runModeConfiguration: Configuration,
    environment: Environment,
    servicesConfig: ServicesConfig
  )(implicit ec: ExecutionContext
  ) {
  lazy val serviceUrl = servicesConfig.baseUrl("third-party-developer-frontend")

  def fetchNavLinks()(implicit hc: HeaderCarrier): Future[Seq[NavLink]] = {
    httpClient.get(url"$serviceUrl/developer/user-navlinks").execute[Seq[NavLink]]
  }
}
