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

package uk.gov.hmrc.testuser.models

import uk.gov.hmrc.testuser.models.UserType.{Individual, Organisation}

case class FieldDefinition(key: String, name: String, allowedUserTypes: Seq[UserType])

object FieldDefinitions {

  // I'm pushing the field definitions out to here from elsewhere, essentially they were originally in the view.
  // eventually they'll be pushed over to api-platform-test-user
  def get(): Seq[FieldDefinition] = Seq(
    FieldDefinition("saUtr", "Self Assessment UTR", Seq(Individual, Organisation)),
    FieldDefinition("nino", "National Insurance Number (NINO)", Seq(Individual, Organisation)),
    FieldDefinition("vrn", "VAT Registration Number", Seq(Individual, Organisation)),
    FieldDefinition("empRef", "Employer Reference", Seq(Organisation)),
    FieldDefinition("ctUtr", "Corporation Tax UTR", Seq(Organisation)),
    FieldDefinition("eoriNumber", "Economic Operator Registration and Identification (EORI) number", Seq(Individual, Organisation)),
    FieldDefinition("userFullName", "Full Name", Seq(Individual, Organisation)),
    FieldDefinition("emailAddress", "Email Address", Seq(Individual, Organisation)),
    FieldDefinition("mtdItId", "Making Tax Digital Income Tax ID", Seq(Individual, Organisation)),
    FieldDefinition("vatRegistrationDate", "VAT Registration Date", Seq(Individual, Organisation)),
    FieldDefinition("lisaManagerReferenceNumber", "LISA Manager Reference Number", Seq(Organisation)),
    FieldDefinition("secureElectronicTransferReferenceNumber", "Secure Electronic Transfer reference number", Seq(Organisation)),
    FieldDefinition("pensionSchemeAdministratorIdentifier", "Pension Scheme Administrator Identifier", Seq(Organisation)),
    FieldDefinition("organisationDetails", "Organisation Details", Seq()),
    FieldDefinition("individualDetails", "Individual Details", Seq()),
    FieldDefinition("groupIdentifier", "Group Identifier", Seq(Individual, Organisation))
  )
}
