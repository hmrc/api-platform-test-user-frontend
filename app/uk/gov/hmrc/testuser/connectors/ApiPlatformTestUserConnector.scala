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

package uk.gov.hmrc.testuser.connectors

import org.apache.http.HttpStatus.SC_CREATED
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.http.ws.WSPost
import uk.gov.hmrc.testuser.config.WSHttp
import uk.gov.hmrc.testuser.models.{TestOrganisation, TestIndividual}
import uk.gov.hmrc.testuser.models.JsonFormatters._
import scala.concurrent.ExecutionContext.Implicits.global

trait ApiPlatformTestUserConnector {

  val serviceUrl: String
  val http: WSPost

  def createIndividual()(implicit hc: HeaderCarrier) = {
    http.doEmptyPost(s"$serviceUrl/individual") map { response =>
      response.status match {
        case SC_CREATED => response.json.as[TestIndividual]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }

  def createOrganisation()(implicit hc: HeaderCarrier) = {
    http.doEmptyPost(s"$serviceUrl/organisation") map { response =>
      response.status match {
        case SC_CREATED => response.json.as[TestOrganisation]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }
}

class ApiPlatformTestUserConnectorImpl extends ApiPlatformTestUserConnector with ServicesConfig {
  override val serviceUrl: String = baseUrl("api-platform-test-user")
  override val http: WSPost = WSHttp
}
