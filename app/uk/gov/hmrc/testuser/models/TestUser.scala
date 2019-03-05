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

import play.api.libs.json.{Json, Reads, Writes}
import uk.gov.hmrc.domain._

sealed trait TestUser {
  val label: String
  val userId: String
  val password: String
  val fields: Seq[Field]
}

case class Field(key: String, label: String, value: String)

case class TestIndividual(userId: String, password: String, saUtr: SaUtr, nino: Nino, vrn: Vrn) extends TestUser {
  override val label = "Individual"
  override val fields = Seq(
    Field("saUtr", "Self Assessment UTR", saUtr.toString()),
    Field("nino", "National Insurance Number (NINO)", nino.toString()),
    Field("vrn", "VAT Registration Number", vrn.toString()))
}

object TestIndividual {
  implicit val testIndividualReads: Reads[TestIndividual] = Json.reads[TestIndividual]
}

case class TestOrganisation(userId: String, password: String, saUtr: SaUtr, empRef: EmpRef, ctUtr: CtUtr, vrn: Vrn) extends TestUser {
  override val label = "Organisation"
  override val fields = Seq(
    Field("saUtr", "Self Assessment UTR", saUtr.toString()),
    Field("empRef", "Employer Reference", empRef.toString()),
    Field("ctUtr", "Corporation Tax UTR", ctUtr.toString()),
    Field("vrn", "VAT Registration Number", vrn.toString()))
}

object UserTypes extends Enumeration {
  type UserType = Value
  val INDIVIDUAL = Value("INDIVIDUAL")
  val ORGANISATION = Value("ORGANISATION")
  val AGENT = Value("AGENT")

  def from(userType: String) = UserTypes.values.find(e => e.toString == userType.toUpperCase)
}

case class CreateUserRequest(serviceNames: Seq[String])
