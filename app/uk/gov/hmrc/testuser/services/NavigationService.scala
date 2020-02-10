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

package uk.gov.hmrc.testuser.services

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import uk.gov.hmrc.testuser.connectors.ThirdPartyDeveloperFrontendConnector
import uk.gov.hmrc.testuser.models.NavLink
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import uk.gov.hmrc.http.HeaderCarrier

@Singleton
class NavigationService @Inject()(connector: ThirdPartyDeveloperFrontendConnector, configuration: Configuration) {

  lazy val developerFrontendUrl = configuration.getString(s"third-party-developer-frontend.host").getOrElse("")

  def headerNavigation()(implicit hc: HeaderCarrier): Future[Seq[NavLink]] =
    connector.fetchNavLinks() map (navLinks => addUrlPrefix(developerFrontendUrl, navLinks))

  private def addUrlPrefix(urlPrefix: String, links: Seq[NavLink]) =
    links map (link => link.copy(href = urlPrefix.concat(link.href)))
}
