import sbt._
import play.core.PlayVersion
import play.sbt.PlayImport._

object AppDependencies {
  def apply() = appDependencies

  lazy val appDependencies: Seq[ModuleID] = compile ++ test
  
  val seleniumVersion = "4.2.2"

  lazy val scope: String = "test, it"

  lazy val compile = Seq(
    ws,
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-28"   % "5.13.0",
    "uk.gov.hmrc"             %% "play-partials"                % "8.2.0-play-28",
    "uk.gov.hmrc"             %% "domain"                       % "6.2.0-play-28",
    "uk.gov.hmrc"             %% "govuk-template"               % "5.70.0-play-28",
    "uk.gov.hmrc"             %% "play-ui"                      % "9.7.0-play-28",
    "uk.gov.hmrc"             %% "play-frontend-govuk"          % "1.0.0-play-28",
    "uk.gov.hmrc"             %% "play-frontend-hmrc"           % "1.6.0-play-28"
  )

  lazy val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"       % "5.13.0",
    "org.jsoup"               %  "jsoup"                        % "1.8.1",
    "com.github.tomakehurst"  %  "wiremock-jre8-standalone"     % "2.31.0",
    "org.scalatestplus"       %% "selenium-4-2"                 % "3.2.13.0",
    "org.seleniumhq.selenium" %  "selenium-java"                % seleniumVersion,
    "org.seleniumhq.selenium" %  "selenium-api"                 % seleniumVersion,
    "org.seleniumhq.selenium" %  "selenium-firefox-driver"      % seleniumVersion,
    "org.seleniumhq.selenium" %  "selenium-chrome-driver"       % seleniumVersion,
    "org.mockito"             %% "mockito-scala-scalatest"      % "1.7.1",
    "com.vladsch.flexmark"    %  "flexmark-all"                 % "0.62.2"
  ).map(_ % scope)
}
