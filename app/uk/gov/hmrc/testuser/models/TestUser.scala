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

import play.api.libs.json._

import scala.collection.immutable

case class Field(key: String, label: String, value: String)

sealed trait TestUser {
  val label: String
  val userId: String
  val password: String
  val fields: Seq[Field]
}

case class TestIndividual(userId: String, password: String, fields: Seq[Field]) extends TestUser {
  override val label = "Individual"
}

object TestIndividual extends TestIndividualJsonMapper(new DefaultFieldDefinitionsProvider())

class TestIndividualJsonMapper(fieldDefinitionsProvider: FieldDefinitionsProvider) extends TestUserMapper(fieldDefinitionsProvider) {
  implicit val testIndividualReads: Reads[TestIndividual] = new Reads[TestIndividual] {
    override def reads(json: JsValue): JsResult[TestIndividual] = {
      val userId = (json \ "userId").as[String]
      val password = (json \ "password").as[String]
      val fields = json.as[Map[String, String]]
      JsSuccess(TestIndividual(userId, password, asFields(withoutCredentials(fields)).toList))
    }
  }
}

case class TestOrganisation(userId: String, password: String, fields: Seq[Field]) extends TestUser {
  override val label = "Organisation"
}

object TestOrganisation extends TestOrganisationJsonMapper(new DefaultFieldDefinitionsProvider)

class TestOrganisationJsonMapper(fieldDefinitionsProvider: FieldDefinitionsProvider) extends TestUserMapper(fieldDefinitionsProvider) {
  implicit val testOrganisationReads: Reads[TestOrganisation] = new Reads[TestOrganisation] {
    override def reads(json: JsValue): JsResult[TestOrganisation] = {
      val userId = (json \ "userId").as[String]
      val password = (json \ "password").as[String]
      val fields = json.as[Map[String, String]]
      JsSuccess(TestOrganisation(userId, password, asFields(withoutCredentials(fields)).toList))
    }
  }
}

object UserTypes extends Enumeration {
  type UserType = Value
  val INDIVIDUAL = Value("INDIVIDUAL")
  val ORGANISATION = Value("ORGANISATION")
  val AGENT = Value("AGENT")

  def from(userType: String) = UserTypes.values.find(e => e.toString == userType.toUpperCase)
}

case class CreateUserRequest(serviceNames: Seq[String])

sealed class TestUserMapper(fieldDefinitionsProvider: FieldDefinitionsProvider) {
  protected def asFields(rawFields: Map[String, String]): immutable.Iterable[Field] = rawFields.map (f => {
    val fieldDefs = fieldDefinitionsProvider.get()
    Field(f._1, fieldDefs.find(fd => fd.key == f._1).getOrElse(FieldDefinition(f._1, f._1)).label, f._2)
  })

  protected def withoutCredentials(fields: Map[String, String]): Map[String, String] = {
    fields.filter(f => f._1 != "userId" && f._1 != "password")
  }
}
