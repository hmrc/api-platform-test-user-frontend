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

package uk.gov.hmrc.testuser.connectors

import javax.inject.Inject
import play.api.{Configuration, Environment}
import play.api.http.Status.{CREATED, OK}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.testuser.models._
import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.wiring.AppConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ApiPlatformTestUserConnector @Inject()(proxiedHttpClient: ProxiedHttpClient,
                                             appConfig: AppConfig,
                                             configuration: Configuration,
                                             environment: Environment,
                                             servicesConfig: ServicesConfig) {
  private val serviceKey = "api-platform-test-user"

  private val bearerToken = servicesConfig.getConfString(s"$serviceKey.bearer-token", "")

  private val httpClient = proxiedHttpClient.withAuthorization(bearerToken)

  val serviceUrl: String = {
    val context = servicesConfig.getConfString(s"$serviceKey.context", "")
    if (context.length > 0) s"${servicesConfig.baseUrl(serviceKey)}/$context"
    else servicesConfig.baseUrl(serviceKey)
  }

  def createIndividual(enrolments: Seq[String])(implicit hc: HeaderCarrier): Future[TestIndividual] = {
    val payload = CreateUserRequest(enrolments)

    post(s"$serviceUrl/individuals", payload) map { response =>
      response.status match {
        case CREATED => response.json.as[TestIndividual]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }

  def createOrganisation(enrolments: Seq[String])(implicit hc: HeaderCarrier): Future[TestOrganisation] = {
    val payload = CreateUserRequest(enrolments)

    post(s"$serviceUrl/organisations", payload) map { response =>
      response.status match {
        case CREATED => response.json.as[TestOrganisation]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }

  def getServices()(implicit hc: HeaderCarrier): Future[Seq[Service]] = {
    httpClient.GET(s"$serviceUrl/services") map { response =>
      response.status match {
        case OK => response.json.as[Seq[Service]]
        case _ => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
      }
    }
  }

  private def post(url: String, payload: CreateUserRequest)(implicit hc: HeaderCarrier) = {
    httpClient.POST[CreateUserRequest, HttpResponse](url, payload, Seq("Content-Type" -> "application/json"))
  }
}

