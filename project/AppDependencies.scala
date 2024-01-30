import sbt._
import play.core.PlayVersion
import play.sbt.PlayImport._

object AppDependencies {
  def apply() = appDependencies

  lazy val appDependencies: Seq[ModuleID] = compile ++ test
  
<<<<<<< HEAD

  lazy val scope: String = "test, it"
  lazy val bootStrapVersion = "8.4.0"
  lazy val seleniumVersion = "4.14.1"
=======
  
  lazy val scope: String = "test, it"
  lazy val bootStrapVersion = "8.4.0"
  lazy val seleniumVersion = "4.14.0"
>>>>>>> 21934dd (Bobbins - wip)

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
    "org.mockito"             %% "mockito-scala-scalatest"      % "1.17.29",
    "org.seleniumhq.selenium" % "selenium-java"                 % "4.17.0",
    "com.titusfortner"        % "selenium-logger"               % "2.3.0",
    // "uk.gov.hmrc"             %% "ui-test-runner"               % "0.10.0"
  ).map(_ % scope)
}
