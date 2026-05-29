import sbt._
import play.sbt.PlayImport._

object AppDependencies {
  def apply() = compile ++ test
  
  lazy val bootStrapVersion     = "10.7.0"
  lazy val seleniumVersion      = "4.14.1"
  lazy val mockitoScalaVersion  = "2.0.0"

  lazy  val compile = Seq(
    ws,
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-30"   % bootStrapVersion,
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-30"   % "12.32.0",
    "org.typelevel"           %% "cats-core"                    % "2.13.0"
  )

  lazy val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"       % bootStrapVersion,
    "org.jsoup"               %  "jsoup"                        % "1.22.2",
    "org.mockito"             %% "mockito-scala-scalatest"      % mockitoScalaVersion,
    "uk.gov.hmrc"             %% "ui-test-runner"               % "0.54.0"
  ).map(_ % "test")
}

