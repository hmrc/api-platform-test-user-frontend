import sbt._
import play.sbt.PlayImport._

object AppDependencies {
  def apply() = compile ++ test
  
  lazy val bootStrapVersion = "9.7.0"
  lazy val seleniumVersion = "4.14.1"

  lazy  val compile = Seq(
    ws,
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-30"   % bootStrapVersion,
    "uk.gov.hmrc"             %% "play-partials-play-30"        % "10.0.0",
    "uk.gov.hmrc"             %% "domain-play-30"               % "10.0.0",
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-30"   % "11.11.0",
    "org.typelevel"           %% "cats-core"                    % "2.10.0"

  )

  lazy val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"       % bootStrapVersion,
    "org.jsoup"               %  "jsoup"                        % "1.8.1",
    "org.mockito"             %% "mockito-scala-scalatest"      % "1.17.30",
    "com.titusfortner"        %  "selenium-logger"              % "2.3.0",
    "uk.gov.hmrc"             %% "ui-test-runner"               % "0.32.0"
  ).map(_ % "test")
}

