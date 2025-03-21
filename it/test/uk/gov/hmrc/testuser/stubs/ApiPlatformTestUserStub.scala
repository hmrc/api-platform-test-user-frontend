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

package uk.gov.hmrc.testuser.stubs

import com.github.tomakehurst.wiremock.client.WireMock._
import org.apache.http.HttpStatus.{SC_CREATED, SC_OK}

import play.api.libs.json.Json

import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.Service

object ApiPlatformTestUserStub {

  def givenTestIndividualIsGenerated(jsonIndividual: String) = {
    stubFor(
      post(urlPathEqualTo("/individuals"))
        .willReturn(
          aResponse()
            .withStatus(SC_CREATED)
            .withHeader("Content-Type", "application/json")
            .withBody(jsonIndividual)
        )
    )
  }

  def givenTestIndividualIsErrored(status: Int) = {
    stubFor(
      post(urlPathEqualTo("/individuals"))
        .willReturn(
          aResponse()
            .withStatus(status)
            .withHeader("Content-Type", "application/json")
        )
    )
  }

  def givenTestOrganisationIsGenerated(jsonOrganisation: String) = {
    stubFor(
      post(urlPathEqualTo("/organisations"))
        .willReturn(
          aResponse()
            .withStatus(SC_CREATED)
            .withHeader("Content-Type", "application/json")
            .withBody(jsonOrganisation)
        )
    )
  }

  def givenTheServicesEndpointReturnsServices(services: Seq[Service]) = {
    stubFor(
      get(urlPathEqualTo("/services"))
        .willReturn(
          aResponse()
            .withStatus(SC_OK)
            .withHeader("Content-Type", "application/json")
            .withBody(Json.toJson(services).toString())
        )
    )
  }

}
