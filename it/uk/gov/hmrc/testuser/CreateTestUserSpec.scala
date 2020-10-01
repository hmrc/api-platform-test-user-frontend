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

package uk.gov.hmrc.testuser

import uk.gov.hmrc.testuser.models._
import uk.gov.hmrc.testuser.pages.CreateTestUserPage
import uk.gov.hmrc.testuser.pages.CreateTestUserPage._
import uk.gov.hmrc.testuser.stubs.ApiPlatformTestUserStub._
import uk.gov.hmrc.testuser.stubs.ThirdPartyDeveloperFrontendStub.givenTheUserNavigationLinks

class CreateTestUserSpec extends BaseSpec {

  private val individualUserId = "individual"
  private val individualPassword = "pwd"
  private val individualSaUtr = "1555369052"
  private val individualNino = "CC333333C"
  private val vrn = "999902541"

  private val organisationId = "organisation"
  private val organisationPassword = "pws2"
  private val organisationSaUtr = "1555369053"

  private val empRef = "555/EIA000"
  private val organisationCtUtr = "1555369054"

  val userNavigationLinks = Seq(NavLink("sign-in", "/sign-in"))
  val services = Seq(
    Service("service1", "Service 1", Seq(UserTypes.INDIVIDUAL)),
    Service("service2", "Service 2", Seq(UserTypes.ORGANISATION)))

  feature("Create a test user") {

    scenario("Create a test individual") {
      givenTheServicesEndpointReturnsServices(services)
      givenTheUserNavigationLinks(userNavigationLinks)
      givenTestIndividualIsGenerated(
        s"""
          |{
          |  "userId":"$individualUserId",
          |  "password":"$individualPassword",
          |  "saUtr":"$individualSaUtr",
          |  "nino":"$individualNino",
          |  "vrn":"$vrn"
          |}
        """.stripMargin)

      goOn(CreateTestUserPage)
      clickOnElement(individualCheckbox)
      clickOnSubmit()

      verifyText("data-userid", individualUserId)
      verifyText("data-password", individualPassword)
      verifyText("data-sautr", individualSaUtr)
      verifyText("data-nino", individualNino)
      verifyText("data-vrn", vrn)
    }

    scenario("Create a test organisation") {
      givenTheServicesEndpointReturnsServices(services)
      givenTheUserNavigationLinks(userNavigationLinks)
      givenTestOrganisationIsGenerated(
        s"""
           |{
           |  "userId":"$organisationId",
           |  "password":"$organisationPassword",
           |  "saUtr":"$organisationSaUtr",
           |  "empRef":"$empRef",
           |  "ctUtr":"$organisationCtUtr",
           |  "vrn":"$vrn"
           |}
        """.stripMargin)

      goOn(CreateTestUserPage)
      clickOnElement(organisationCheckbox)
      clickOnSubmit()

      verifyText("data-userid", organisationId)
      verifyText("data-password", organisationPassword)
      verifyText("data-sautr", organisationSaUtr)
      verifyText("data-ctutr", organisationCtUtr)
      verifyText("data-empref", s"$empRef")
      verifyText("data-vrn", vrn)
    }
  }

}
