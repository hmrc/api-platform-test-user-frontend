import sbt._
import play.sbt.PlayImport._

object AppDependencies {
  def apply() = compile ++ test
  
  lazy val bootStrapVersion = "8.4.0"
  lazy val seleniumVersion = "4.14.1"

  lazy  val compile = Seq(
    ws,
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-30"   % bootStrapVersion,
    "uk.gov.hmrc"             %% "play-partials-play-30"        % "9.1.0",
    "uk.gov.hmrc"             %% "domain-play-30"               % "9.0.0",
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-30"   % "8.5.0"
  )

  lazy val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"       % bootStrapVersion,
    "org.jsoup"               %  "jsoup"                        % "1.8.1",
    "org.mockito"             %% "mockito-scala-scalatest"      % "1.17.30",
    "com.titusfortner"        % "selenium-logger"               % "2.3.0",
    "uk.gov.hmrc"             %% "ui-test-runner"               % "0.16.0"
  ).map(_ % "test")
}

