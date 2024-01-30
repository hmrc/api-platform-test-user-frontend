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

package uk.gov.hmrc.testuser

import uk.gov.hmrc.testuser.models._
import uk.gov.hmrc.testuser.pages.CreateTestUserPage
import uk.gov.hmrc.testuser.stubs.ApiPlatformTestUserStub._
import uk.gov.hmrc.testuser.stubs.ThirdPartyDeveloperFrontendStub.givenTheUserNavigationLinks

class CreateTestUserSpec extends BaseSpec {

  private val individualUserId   = "individual"
  private val individualPassword = "pwd"
  private val individualSaUtr    = "1555369052"
  private val individualNino     = "CC333333C"
  private val vrn                = "999902541"

  private val organisationId       = "organisation"
  private val organisationPassword = "pws2"
  private val organisationSaUtr    = "1555369053"

  private val empRef            = "555/EIA000"
  private val organisationCtUtr = "1555369054"

  val userNavigationLinks = Seq(NavLink("sign-in", "/sign-in"))
  val services            = Seq(Service("service1", "Service 1", Seq(UserTypes.INDIVIDUAL)), Service("service2", "Service 2", Seq(UserTypes.ORGANISATION)))

  Feature("Create a test user") {
    Scenario("Create a test individual") {
      givenTheServicesEndpointReturnsServices(services)
      givenTheUserNavigationLinks(userNavigationLinks)
      givenTestIndividualIsGenerated(s"""
                                        |{
                                        |  "userId":"$individualUserId",
                                        |  "password":"$individualPassword",
                                        |  "saUtr":"$individualSaUtr",
                                        |  "nino":"$individualNino",
                                        |  "vrn":"$vrn"
                                        |}
        """.stripMargin)

      val page = new CreateTestUserPage(port)
      page.goTo()
      page.isCurrentPage shouldBe true
      page.selectIndividual()
      page.clickOnSubmit()

      page.getPassword() shouldBe individualPassword
      page.getUserId() shouldBe individualUserId
      page.getSaUtr() shouldBe individualSaUtr
      page.getNino() shouldBe individualNino
      page.getVrn() shouldBe vrn
    }

    Scenario("Create a test organisation") {
      givenTheServicesEndpointReturnsServices(services)
      givenTheUserNavigationLinks(userNavigationLinks)
      givenTestOrganisationIsGenerated(s"""
                                          |{
                                          |  "userId":"$organisationId",
                                          |  "password":"$organisationPassword",
                                          |  "saUtr":"$organisationSaUtr",
                                          |  "empRef":"$empRef",
                                          |  "ctUtr":"$organisationCtUtr",
                                          |  "vrn":"$vrn"
                                          |}
        """.stripMargin)

      val page = new CreateTestUserPage(port)
      page.goTo()
      page.isCurrentPage shouldBe true

      page.selectOrganisation()

      page.clickOnSubmit()

      page.getPassword() shouldBe organisationPassword
      page.getUserId() shouldBe organisationId
      page.getSaUtr() shouldBe organisationSaUtr
      page.getCtUtr() shouldBe organisationCtUtr
      page.getEmpRef() shouldBe empRef
      page.getVrn() shouldBe vrn
    }
  }

}
