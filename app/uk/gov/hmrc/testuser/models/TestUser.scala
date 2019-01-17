/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.testuser.models

import uk.gov.hmrc.domain._
import uk.gov.hmrc.testuser.models.ServiceName.ServiceName

sealed trait TestUser {
  val label: String
}

case class TestIndividual(userId: String, password: String, saUtr: SaUtr, nino: Nino) extends TestUser {
  override val label = "Individual"
}

case class TestOrganisation(userId: String, password: String, saUtr: SaUtr, empRef: EmpRef, ctUtr: CtUtr, vrn: Vrn) extends TestUser {
  override val label = "Organisation"
}

object UserType extends Enumeration {
  type UserType = Value
  val INDIVIDUAL = Value("INDIVIDUAL")
  val ORGANISATION = Value("ORGANISATION")

  def from(userType: String) = UserType.values.find(e => e.toString == userType.toUpperCase)

}

object ServiceName extends Enumeration {
  type ServiceName = Value
  val NATIONAL_INSURANCE = Value("national-insurance")
  val SELF_ASSESSMENT = Value("self-assessment")
  val CORPORATION_TAX = Value("corporation-tax")
  val PAYE_FOR_EMPLOYERS = Value("paye-for-employers")
  val SUBMIT_VAT_RETURNS = Value("submit-vat-returns")
  val MTD_VAT = Value("mtd-vat")
  val MTD_INCOME_TAX = Value("mtd-income-tax")
  val AGENT_SERVICES = Value("agent-services")
}

case class CreateUserRequest(serviceNames: Seq[ServiceName])
