import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

import scala.util.Properties
import bloop.integrations.sbt.BloopDefaults

lazy val playSettings: Seq[Setting[_]] = Seq.empty
lazy val appName = "api-platform-test-user-frontend"

lazy val microservice = (project in file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtDistributablesPlugin)
  .settings(
    name := appName,
    defaultSettings(),
    playSettings,
    scalaSettings,
    publishingSettings,
    scalaVersion := "2.12.12",
    libraryDependencies ++= AppDependencies(),
    retrieveManaged := true,
    majorVersion := 0
  )
  .settings(SilencerSettings(): _*)
  .settings(ScoverageSettings(): _*)
  .settings(
    TwirlKeys.templateImports ++= Seq(
      "play.twirl.api.HtmlFormat",
      "uk.gov.hmrc.govukfrontend.views.html.components._"
    )
  )
  .configs(Test)
  .settings(inConfig(Test)(Defaults.testSettings): _*)
  .settings(
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-eT"),
    Test / unmanagedSourceDirectories += baseDirectory.value / "test",
    Test / unmanagedSourceDirectories += baseDirectory.value / "test-utils",
    Test / unmanagedResourceDirectories += baseDirectory.value / "test" / "resources",
    Test / sourceDirectory := baseDirectory.value / "test",
    Test / parallelExecution := false,
    Test / fork := false
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(inConfig(IntegrationTest)(BloopDefaults.configSettings))
  .settings(
    IntegrationTest / sourceDirectory := baseDirectory.value / "it",
    IntegrationTest / fork := true,
    IntegrationTest / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-eT"),
    IntegrationTest / unmanagedSourceDirectories += baseDirectory.value / "it",
    IntegrationTest / unmanagedSourceDirectories += baseDirectory.value / "test-utils",
    IntegrationTest / unmanagedResourceDirectories += baseDirectory.value / "test" / "resources",
    IntegrationTest / testGrouping := oneForkedJvmPerTest((IntegrationTest / definedTests).value),
    addTestReportOption(IntegrationTest, "int-test-reports")
  )

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] =
  tests map { test =>
    Group(
      test.name,
      Seq(test),
      SubProcess(
        ForkOptions().withRunJVMOptions(
          Vector(s"-Dtest.name={test.name}", s"-Dbrowser=${Properties.propOrElse("browser", "chrome")}"))
      )
    )
  }
