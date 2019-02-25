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

import uk.gov.hmrc.testuser.pages.CreateTestUserPage
import uk.gov.hmrc.testuser.pages.CreateTestUserPage._
import uk.gov.hmrc.testuser.stubs.ThirdPartyDeveloperFrontendStub.givenTheUserNavigationLinks
import uk.gov.hmrc.testuser.stubs.ApiPlatformTestUserStub._
import uk.gov.hmrc.domain._
import uk.gov.hmrc.testuser.models._

class CreateTestUserSpec extends BaseSpec {

  val individual = TestIndividual("individual", "pwd", SaUtr("1555369052"), Nino("CC333333C"), Vrn("999902541"))
  val organisation = TestOrganisation("organisation", "pws2", SaUtr("1555369053"), EmpRef("555","EIA000"),
    CtUtr("1555369054"), Vrn("999902541"))
  val userNavigationLinks = Seq(NavLink("sign-in", "/sign-in"))
  val services = Seq(
    Service("service1", "Service 1", Seq(UserTypes.INDIVIDUAL)),
    Service("service2", "Service 2", Seq(UserTypes.ORGANISATION)))

  feature("Create a test user") {

    scenario("Create a test individual") {
      givenTheServicesEndpointReturnsServices(services)
      givenTheUserNavigationLinks(userNavigationLinks)
      givenTestIndividualIsGenerated(individual)

      goOn(CreateTestUserPage)
      clickOnElement(individualCheckbox)
      clickOnSubmit()

      verifyText("data-userid", individual.userId)
      verifyText("data-password", individual.password)
      verifyText("data-sautr", individual.saUtr.utr)
      verifyText("data-nino", individual.nino.value)
      verifyText("data-vrn", individual.vrn.value)
      verifyHasLink(userNavigationLinks.head.label)
    }

    scenario("Create a test organisation") {
      givenTheServicesEndpointReturnsServices(services)
      givenTheUserNavigationLinks(userNavigationLinks)
      givenTestOrganisationIsGenerated(organisation)

      goOn(CreateTestUserPage)
      clickOnElement(organisationCheckbox)
      clickOnSubmit()

      verifyText("data-userid", organisation.userId)
      verifyText("data-password", organisation.password)
      verifyText("data-sautr", organisation.saUtr.utr)
      verifyText("data-ctutr", organisation.ctUtr.utr)
      verifyText("data-empref", organisation.empRef.value)
      verifyText("data-vrn", organisation.vrn.value)
    }
  }

}
