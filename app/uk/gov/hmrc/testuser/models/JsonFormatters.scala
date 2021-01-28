/*
 * Copyright 2021 HM Revenue & Customs
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

import play.api.libs.json.{Format, JsError, JsSuccess, Writes, _}

object EnumJson {

  def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = {
    case JsString(s) => {
      try {
        JsSuccess(enum.withName(s))
      } catch {
        case _: NoSuchElementException =>
          JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not contain '$s'")
      }
    }
    case _ => JsError("String value expected")
  }

  def enumWrites[E <: Enumeration]: Writes[E#Value] = (v: E#Value) => JsString(v.toString)

  def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumReads(enum), enumWrites)
  }

}

object JsonFormatters {
  implicit val formatNavLinks = Json.format[NavLink]
  implicit val formatCreateUserServicesRequest = Json.format[CreateUserRequest]
  implicit val formatUserType =  EnumJson.enumFormat(UserTypes)
  implicit val formatService = Json.format[Service]
}
