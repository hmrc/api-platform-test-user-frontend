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

case class FieldDefinition (key: String, label: String)

trait FieldDefinitionsProvider {
  def get(): Seq[FieldDefinition]
}

class DefaultFieldDefinitionsProvider extends FieldDefinitionsProvider {
  // I'm pushing the field definitions out to here from elsewhere, essentially they were originally in the view.
  // eventually they'll be pushed over to api-platform-test-user
  def get(): Seq[FieldDefinition] = Seq(
    FieldDefinition("saUtr", "Self Assessment UTR"),
    FieldDefinition("nino", "National Insurance Number (NINO)"),
    FieldDefinition("vrn", "VAT Registration Number"),
    FieldDefinition("empRef", "Employer Reference"),
    FieldDefinition("ctUtr", "Corporation Tax UTR"),
    FieldDefinition("eoriNumber","Economic Operator Registration and Identification (EORI) number"),
    FieldDefinition("userFullName", "Full Name"),
    FieldDefinition("emailAddress", "Email Address"),
    FieldDefinition("mtdItId", "Making Tax Digital Income Tax ID"),
    FieldDefinition("vatRegistrationDate", "VAT Registration Date"),
    FieldDefinition("lisaManagerReferenceNumber", "LISA Manager Reference Number"),
    FieldDefinition("secureElectronicTransferReferenceNumber", "Secure Electronic Transfer reference number"),
    FieldDefinition("pensionSchemeAdministratorIdentifier", "Pension Scheme Administrator Identifier"),
    FieldDefinition("organisationDetails", "Organisation Details"),
    FieldDefinition("individualDetails", "Individual Details"))
}
