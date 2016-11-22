/*
 * Copyright 2016 HM Revenue & Customs
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

package it.uk.gov.hmrc.testuser

import java.util.concurrent.TimeUnit

import it.uk.gov.hmrc.testuser.pages.CreateTestUserPage
import it.uk.gov.hmrc.testuser.pages.CreateTestUserPage.{createIndividualButton, createOrganisationButton}
import scala.concurrent.duration.Duration

class CreateTestUserSpec extends BaseSpec {

  val timeout = Duration(5, TimeUnit.SECONDS)
  val serviceUrl = s"http://localhost:$port"

  feature("Create a test user") {

    scenario("Create a test individual") {

      go(CreateTestUserPage)
      clickOnElement(createIndividualButton)
      verifyText("username", "USER")
      verifyText("password", "PASSWORD")
    }

    scenario("Create a test organisation") {

      go(CreateTestUserPage)
      clickOnElement(createOrganisationButton)
      verifyText("username", "USER")
      verifyText("password", "PASSWORD")

    }
  }
}
