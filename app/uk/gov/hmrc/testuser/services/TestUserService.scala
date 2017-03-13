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

package uk.gov.hmrc.testuser.services

import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.testuser.connectors.{ApiPlatformTestUserConnectorImpl, ApiPlatformTestUserConnector}
import javax.inject.Inject

import uk.gov.hmrc.testuser.models.{TestUser, UserType}
import uk.gov.hmrc.testuser.models.UserType.UserType
import uk.gov.hmrc.testuser.models.UserType.UserType

import scala.concurrent.Future

trait TestUserService {

  val apiPlatformTestUserConnector: ApiPlatformTestUserConnector

  def createUser(userType: UserType)(implicit hc: HeaderCarrier): Future[TestUser] = {
    userType match {
      case UserType.INDIVIDUAL => apiPlatformTestUserConnector.createIndividual()
      case UserType.ORGANISATION => apiPlatformTestUserConnector.createOrganisation()
    }
  }
}

class TestUserServiceImpl @Inject()(override val apiPlatformTestUserConnector: ApiPlatformTestUserConnectorImpl) extends TestUserService {
}
