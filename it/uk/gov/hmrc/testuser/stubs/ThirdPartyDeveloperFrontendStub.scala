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

package uk.gov.hmrc.testuser.stubs

import com.github.tomakehurst.wiremock.client.WireMock._
import play.api.http.Status.OK
import play.api.libs.json.Json.{stringify, toJson}
import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.NavLink

object ThirdPartyDeveloperFrontendStub {

  def givenTheUserNavigationLinks(navLinks: Seq[NavLink]) = {
    stubFor(get(urlPathEqualTo("/developer/user-navlinks"))
      .willReturn(aResponse()
        .withStatus(OK)
        .withHeader("Content-Type", "application/json")
        .withBody(stringify(toJson(navLinks)))))
  }
}
