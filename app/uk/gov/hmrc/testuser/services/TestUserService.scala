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

package uk.gov.hmrc.testuser.services

import javax.inject.Inject
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.testuser.connectors.ApiPlatformTestUserConnector
import uk.gov.hmrc.testuser.models.UserTypes.{INDIVIDUAL, ORGANISATION, UserType}
import uk.gov.hmrc.testuser.models.{Service, TestUser, UserTypes}

import scala.concurrent.{ExecutionContext, Future}

class TestUserService @Inject() (apiPlatformTestUserConnector: ApiPlatformTestUserConnector)(implicit ec: ExecutionContext) {

  def services(implicit hc: HeaderCarrier) = apiPlatformTestUserConnector.getServices()

  def createUser(userType: UserType)(implicit hc: HeaderCarrier): Future[TestUser] = {
    for {
      services <- apiPlatformTestUserConnector.getServices()
      testUser <- createUserWithServices(userType, services)
    } yield testUser

  }
  def createUserGeneric(userType: UserType, selectedServices: Seq[String])(implicit hc: HeaderCarrier): Future[TestUser] = {
    println(s"ACHI: $selectedServices")
    for {
      services <- apiPlatformTestUserConnector.getServices()
      testUser <- createUserWithServices(userType, services.filter(x => selectedServices.contains(x.key)))
    } yield testUser

  }

  private def createUserWithServices(userType: UserType, services: Seq[Service])(implicit hc: HeaderCarrier) = {
    userType match {
      case INDIVIDUAL             => apiPlatformTestUserConnector.createIndividual(serviceKeysForUserType(INDIVIDUAL, services))
      case UserTypes.ORGANISATION => apiPlatformTestUserConnector.createOrganisation(serviceKeysForUserType(ORGANISATION, services))
    }
  }

  private def serviceKeysForUserType(userType: UserType, services: Seq[Service]) = {
    services.filter(s => s.allowedUserTypes.contains(userType)).map(s => s.key)
  }
}
