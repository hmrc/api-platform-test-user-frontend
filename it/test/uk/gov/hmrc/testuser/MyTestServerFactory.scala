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

import play.api.test.DefaultTestServerFactory
import play.api.{Application, Mode}
import play.core.server.ServerConfig

import uk.gov.hmrc.testuser.helpers.Env

object MyTestServerFactory extends MyTestServer

class MyTestServer extends DefaultTestServerFactory {

  override protected def serverConfig(app: Application): ServerConfig = {
    val sc = ServerConfig(port = Some(Env.port), sslPort = None, mode = Mode.Test, rootDir = app.path)
    sc.copy(configuration = sc.configuration withFallback overrideServerConfiguration(app))
  }
}
