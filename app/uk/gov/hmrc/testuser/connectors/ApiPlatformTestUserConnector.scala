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

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

import play.api.http.Status.{CREATED, OK}
import play.api.libs.json.Json
import play.api.{Configuration, Environment}
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.client.{HttpClientV2, RequestBuilder}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, UpstreamErrorResponse, _}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import uk.gov.hmrc.testuser.ApplicationLogger
import uk.gov.hmrc.testuser.models.JsonFormatters._
import uk.gov.hmrc.testuser.models._
import uk.gov.hmrc.testuser.wiring.AppConfig

class ApiPlatformTestUserConnector @Inject() (
    httpClient: HttpClientV2,
    appConfig: AppConfig,
    configuration: Configuration,
    environment: Environment,
    servicesConfig: ServicesConfig,
  )(implicit ec: ExecutionContext
  ) extends ApplicationLogger {
  private val serviceKey = "api-platform-test-user"

  private val bearerToken = servicesConfig.getConfString(s"$serviceKey.bearer-token", "")

  private def configureEbridge(requestBuilder: RequestBuilder): RequestBuilder =
    EbridgeConfigurator.configure(bearerToken)(requestBuilder)

  val serviceUrl: String = {
    val context = servicesConfig.getConfString(s"$serviceKey.context", "")
    if (context.length > 0) s"${servicesConfig.baseUrl(serviceKey)}/$context"
    else servicesConfig.baseUrl(serviceKey)
  }

  def createIndividual(enrolments: Seq[String])(implicit hc: HeaderCarrier): Future[Either[Int, TestIndividual]] = {
    val payload = CreateUserRequest(enrolments)

    post(url"$serviceUrl/individuals", payload) map { response =>
      response.status match {
        case CREATED => Right(response.json.as[TestIndividual])
        case status  =>
          logger.warn(s"Unexpected response code=${status} message=${response.body}")
          Left(status)
      }
    }
  }

  def createOrganisation(enrolments: Seq[String])(implicit hc: HeaderCarrier): Future[Either[Int, TestOrganisation]] = {
    val payload = CreateUserRequest(enrolments)

    post(url"$serviceUrl/organisations", payload) map { response =>
      response.status match {
        case CREATED => Right(response.json.as[TestOrganisation])
        case status  =>
          logger.warn(s"Unexpected response code=${status} message=${response.body}")
          Left(status)
      }
    }
  }

  def getServices()(implicit hc: HeaderCarrier): Future[Seq[Service]] = {
    configureEbridge(httpClient.get(url"$serviceUrl/services"))
      .execute[Either[UpstreamErrorResponse, HttpResponse]]
      .map {
        case Right(response) if (response.status == OK)      => response.json.as[Seq[Service]]
        case Right(response)                                 => throw new RuntimeException(s"Unexpected response code=${response.status} message=${response.body}")
        case Left(UpstreamErrorResponse(body, status, _, _)) => throw new RuntimeException(s"Unexpected response code=${status} message=${body}")
      }
  }

  private def post(url: URL, payload: CreateUserRequest)(implicit hc: HeaderCarrier) = {
    configureEbridge(httpClient.post(url))
      .setHeader("Content-Type" -> "application/json")
      .withBody(Json.toJson(payload))
      .execute[HttpResponse]
  }
}
