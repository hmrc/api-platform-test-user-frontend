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

import play.api.http.Status.CREATED
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.http.ws.WSPost
import uk.gov.hmrc.testuser.config.{ProxiedApiPlatformWSHttp, WSHttp}
import uk.gov.hmrc.testuser.models.{CreateUserRequest, TestIndividual, TestOrganisation}
import uk.gov.hmrc.testuser.models.ServiceName._
import uk.gov.hmrc.testuser.models.JsonFormatters._

import scala.concurrent.ExecutionContext.Implicits.global
import uk.gov.hmrc.http.HeaderCarrier

trait ApiPlatformTestUserConnector {

  val serviceUrl: String
  val http: WSPost

  def createIndividual()(implicit hc: HeaderCarrier) = {
    val payload = CreateUserRequest(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX))

    http.doPost[CreateUserRequest](s"$serviceUrl/individuals", payload, Seq("Content-Type" -> "application/json")) map { response =>
      response.status match {
        case CREATED => response.json.as[TestIndividual]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }

  def createOrganisation()(implicit hc: HeaderCarrier) = {
    val payload = CreateUserRequest(Seq(NATIONAL_INSURANCE, SELF_ASSESSMENT, MTD_INCOME_TAX,
      CORPORATION_TAX, PAYE_FOR_EMPLOYERS, SUBMIT_VAT_RETURNS, MTD_VAT))

    http.doPost[CreateUserRequest](s"$serviceUrl/organisations", payload, Seq("Content-Type" -> "application/json")) map { response =>
      response.status match {
        case CREATED => response.json.as[TestOrganisation]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }
}

class ApiPlatformTestUserConnectorImpl extends ApiPlatformTestUserConnector with ServicesConfig {
  private val serviceKey = "api-platform-test-user"

  override val serviceUrl: String = {
    val context = getConfString(s"$serviceKey.context", "")
    if (context.length > 0) s"${baseUrl(serviceKey)}/$context"
    else baseUrl(serviceKey)
  }

  override val http: WSPost = ProxiedApiPlatformWSHttp(getConfString(s"$serviceKey.bearer-token", ""))
}
