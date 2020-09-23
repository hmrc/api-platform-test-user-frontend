/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.testuser.views

import uk.gov.hmrc.testuser.controllers.routes // DOuble check this after
import javax.inject.Inject
import play.api.i18n.Messages
import uk.gov.hmrc.testuser.config.AppConfig
import uk.gov.hmrc.govukfrontend.views.viewmodels.footer.FooterItem

class FooterLinks @Inject()(appConfig: AppConfig) {

  def cookieLink(implicit messages: Messages): FooterItem = FooterItem(
    Some(messages("footer.cookies")),
    Some(appConfig.cookies)
  )

  def privacyLink(implicit messages: Messages): FooterItem = FooterItem(
    Some(messages("footer.privacy")),
    Some(appConfig.privacy)
  )

  def termsConditionsLink(implicit messages: Messages): FooterItem = FooterItem(
    Some(messages("footer.termsConditions")),
    Some(appConfig.termsConditions)
  )

  def govukHelpLink(implicit messages: Messages): FooterItem = FooterItem(
    Some(messages("footer.govukHelp")),
    Some(appConfig.govukHelp)
  )

  def accessibilityLink(implicit messages: Messages): FooterItem = FooterItem(
    Some(messages("footer.accessibility")),
    Some(appConfig.accessibility)
  )

  def items(implicit messages: Messages) = Seq(
    cookieLink,
    accessibilityLink,
    privacyLink,
    termsConditionsLink,
    govukHelpLink
  )
}
