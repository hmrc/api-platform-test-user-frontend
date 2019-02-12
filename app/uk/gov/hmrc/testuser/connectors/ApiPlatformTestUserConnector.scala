/*
 * Copyright 2019 HM Revenue & Customs
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

import javax.inject.Inject
import play.api.http.Status.CREATED
import play.api.{Configuration, Environment}
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models.ServiceName._
import uk.gov.hmrc.testuser.models.{CreateUserRequest, TestIndividual, TestOrganisation}

import scala.concurrent.ExecutionContext.Implicits.global

class ApiPlatformTestUserConnector @Inject()(httpClient: HttpClient,
                                             override val runModeConfiguration: Configuration,
                                             environment: Environment) extends ServicesConfig {

  override protected def mode = environment.mode

  private val serviceKey = "api-platform-test-user"

  val bearerToken = getConfString(s"$serviceKey.bearer-token", "")

  val serviceUrl: String = {
    val context = getConfString(s"$serviceKey.context", "")
    if (context.length > 0) s"${baseUrl(serviceKey)}/$context"
    else baseUrl(serviceKey)
  }

  def createIndividual()(implicit hc: HeaderCarrier) = {
    val payload = CreateUserRequest(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX))

    makeCall(s"$serviceUrl/individuals", payload)(buildHc(hc)) map { response =>
      response.status match {
        case CREATED => response.json.as[TestIndividual]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }

  def createOrganisation()(implicit hc: HeaderCarrier) = {
    val payload = CreateUserRequest(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX,
      CORPORATION_TAX, PAYE_FOR_EMPLOYERS, SUBMIT_VAT_RETURNS))

    makeCall(s"$serviceUrl/organisations", payload)(buildHc(hc)) map { response =>
      response.status match {
        case CREATED => response.json.as[TestOrganisation]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }

  private def makeCall(url: String, payload: CreateUserRequest)(implicit hc: HeaderCarrier) = {
    httpClient.POST[CreateUserRequest, HttpResponse](url, payload, Seq("Content-Type" -> "application/json"))
  }

  private def buildHc(hc: HeaderCarrier) = {
    hc.copy(authorization = Some(Authorization("Bearer " + bearerToken)))
  }
}

