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

package it.uk.gov.hmrc.testuser

import it.uk.gov.hmrc.testuser.pages.CreateTestUserPage
import it.uk.gov.hmrc.testuser.pages.CreateTestUserPage._
import it.uk.gov.hmrc.testuser.stubs.ThirdPartyDeveloperFrontendStub.givenTheUserNavigationLinks
import it.uk.gov.hmrc.testuser.stubs.ApiPlatformTestUserStub.{givenTestOrganisationIsGenerated, givenTestIndividualIsGenerated}
import uk.gov.hmrc.domain._
import uk.gov.hmrc.testuser.models.{NavLink, TestOrganisation, TestIndividual}

class CreateTestUserSpec extends BaseSpec {

  val individual = TestIndividual("individual", "pwd", SaUtr("1555369052"), Nino("CC333333C"))
  val organisation = TestOrganisation("organisation", "pws2", SaUtr("1555369053"), EmpRef("555","EIA000"),
    CtUtr("1555369054"), Vrn("999902541"))
  val userNavigationLinks = Seq(NavLink("sign-in", "/sign-in"))

  feature("Create a test user") {

    scenario("Create a test individual") {

      givenTheUserNavigationLinks(userNavigationLinks)
      givenTestIndividualIsGenerated(individual)

      goOn(CreateTestUserPage)
      clickOnElement(individualCheckbox)
      clickOnSubmit()

      verifyText("data-userid", individual.userId)
      verifyText("data-password", individual.password)
      verifyText("data-sautr", individual.saUtr.utr)
      verifyText("data-nino", individual.nino.value)
      verifyHasLink(userNavigationLinks.head.label)
    }

    scenario("Create a test organisation") {

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
