/*
 * Copyright 2017 HM Revenue & Customs
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

package it.uk.gov.hmrc.testuser.stubs

import com.github.tomakehurst.wiremock.client.WireMock._
import it.uk.gov.hmrc.testuser.MockHost
import org.apache.http.HttpStatus.SC_CREATED
import play.api.libs.json.Json
import uk.gov.hmrc.testuser.models.{TestOrganisation, TestIndividual}
import uk.gov.hmrc.testuser.models.JsonFormatters._

object ApiPlatformTestUserStub extends MockHost(11111) {

  def givenTestIndividualIsGenerated(individual: TestIndividual) = {
    mock.register(post(urlPathEqualTo("/individual"))
      .willReturn(aResponse()
        .withStatus(SC_CREATED)
        .withHeader("Content-Type", "application/json")
        .withBody(Json.toJson(individual).toString())))
  }

  def givenTestOrganisationIsGenerated(organisation: TestOrganisation) = {
    mock.register(post(urlPathEqualTo("/organisation"))
      .willReturn(aResponse()
        .withStatus(SC_CREATED)
        .withHeader("Content-Type", "application/json")
        .withBody(Json.toJson(organisation).toString())))
  }

}
