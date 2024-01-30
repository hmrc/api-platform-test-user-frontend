import sbt._
import play.core.PlayVersion
import play.sbt.PlayImport._

object AppDependencies {
  def apply() = appDependencies

  lazy val appDependencies: Seq[ModuleID] = compile ++ test
  
  
  lazy val scope: String = "test, it"
  lazy val bootStrapVersion = "8.4.0"
  lazy val seleniumVersion = "4.14.0"

  lazy  val compile = Seq(
    ws,
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-30"   % bootStrapVersion,
    "uk.gov.hmrc"             %% "play-partials-play-30"        % "9.1.0",
    "uk.gov.hmrc"             %% "domain-play-30"               % "9.0.0",
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-30"   % "8.4.0"
  )

  lazy val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"       % bootStrapVersion,
    "org.jsoup"               %  "jsoup"                        % "1.8.1",
    // "com.github.tomakehurst"  %  "wiremock-jre8-standalone"     % "2.31.0",
    // "org.scalatestplus"       %% "selenium-4-2"                 % "3.2.13.0",
    // "org.seleniumhq.selenium" %  "selenium-api"                 % seleniumVersion,
    // "org.seleniumhq.selenium" %  "selenium-firefox-driver"      % seleniumVersion,
    // "org.seleniumhq.selenium" %  "selenium-chrome-driver"       % seleniumVersion,
    "org.mockito"             %% "mockito-scala-scalatest"      % "1.17.29",
    // "org.scalatest"           %% "scalatest"                    % "3.2.17",
    // "com.vladsch.flexmark"    %  "flexmark-all"                 % "0.62.2",
//    "org.seleniumhq.selenium" % "selenium-java"                 % "4.17.0",
    "com.titusfortner"        % "selenium-logger"               % "2.3.0",
     "uk.gov.hmrc"             %% "ui-test-runner"               % "0.16.0"
  ).map(_ % scope)
}
