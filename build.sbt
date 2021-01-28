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

lazy val appName = "api-platform-test-user-frontend"
lazy val appDependencies: Seq[ModuleID] = compile ++ test
lazy val bootstrapPlayVersion = "2.3.0"
lazy val playPartialsVersion = "6.11.0-play-26"
lazy val pegdownVersion = "1.6.0"
lazy val scalaTestPlusVersion = "3.1.3"
lazy val wiremockVersion = "2.25.1"
lazy val mockitoVersion = "1.10.19"

lazy val scope: String = "test, it"

lazy val compile = Seq(
  ws,
  "uk.gov.hmrc" %% "bootstrap-play-26" % bootstrapPlayVersion,
  "uk.gov.hmrc" %% "play-partials" % playPartialsVersion,
  "uk.gov.hmrc" %% "domain" % "5.6.0-play-26",
  "uk.gov.hmrc" %% "govuk-template" % "5.61.0-play-26",
  "uk.gov.hmrc" %% "play-ui" % "8.21.0-play-26",
  "uk.gov.hmrc" %% "play-frontend-govuk" % "0.60.0-play-26",
  "uk.gov.hmrc" %% "play-frontend-hmrc" % "0.38.0-play-26"
)

lazy val test = Seq(
  "org.pegdown" % "pegdown" % pegdownVersion % scope,
  "org.jsoup" % "jsoup" % "1.8.1" % scope,
  "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
  "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusVersion % scope,
  "com.github.tomakehurst" % "wiremock" % wiremockVersion % scope,
  // "org.seleniumhq.selenium" % "selenium-java" % "2.53.1" % scope,
  // "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % "2.52.0" % scope,

  "org.seleniumhq.selenium" % "selenium-java" % "3.141.59" % scope,
  "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.141.59" % scope,
  "org.seleniumhq.selenium" % "selenium-chrome-driver" % "3.141.59" % scope,
  "org.mockito" %% "mockito-scala-scalatest" % "1.7.1" % scope
)

val jettyVersion = "9.2.24.v20180105"

val jettyOverrides = Seq(
  "org.eclipse.jetty" % "jetty-server" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-servlet" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-security" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-servlets" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-continuation" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-xml" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-client" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-http" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-io" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty" % "jetty-util" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty.websocket" % "websocket-api" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty.websocket" % "websocket-common" % jettyVersion % IntegrationTest,
  "org.eclipse.jetty.websocket" % "websocket-client" % jettyVersion % IntegrationTest
)

lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty

lazy val microservice = (project in file("."))
  .enablePlugins(Seq(_root_.play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory) ++ plugins: _*)
  .settings(
    name := appName,
    defaultSettings(),
    playSettings,
    scalaSettings,
    publishingSettings,
    scalaVersion := "2.12.12",
    libraryDependencies ++= appDependencies,
    dependencyOverrides ++= jettyOverrides,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    parallelExecution in Test := false,
    fork in Test := false,
    majorVersion := 0,
    scoverageSettings,
    resolvers ++= Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.jcenterRepo
    )
  )
  .settings(
    TwirlKeys.templateImports ++= Seq(
      "play.twirl.api.HtmlFormat",
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.govukfrontend.views.html.helpers._"
    )
  )
  .configs(Test)
  .settings(inConfig(Test)(Defaults.testSettings): _*)
  .settings(
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-eT"),
    Test / unmanagedSourceDirectories += baseDirectory.value / "test",
    Test / unmanagedSourceDirectories += baseDirectory.value / "test-utils",
    Test / sourceDirectory := baseDirectory.value / "test"
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
    IntegrationTest / testGrouping := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
    addTestReportOption(IntegrationTest, "int-test-reports")
  )

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] =
  tests map { test =>
    Group(
      test.name,
      Seq(test),
      SubProcess(
        ForkOptions().withRunJVMOptions(
          Vector(s"-Dtest.name={test.name}", s"-Dtest_driver=${Properties.propOrElse("test_driver", "chrome")}"))
      )
    )
  }

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    ScoverageKeys.coverageMinimum := 75.00,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    ScoverageKeys.coverageExcludedPackages := "<empty>;com.kenshoo.play.metrics.*;" +
      ".*definition.*;" +
      "prod.*;" +
      "testOnlyDoNotUseInAppConf.*;" +
      "app.*;" +
      "uk.gov.hmrc.BuildInfo;controllers.javascript.*;" +
      "uk.gov.hmrc.testuser.FrontendModule;" +
      "uk.gov.hmrc.testuser.controllers.javascript.*;" +
      "uk.gov.hmrc.testuser.controllers.FormKeys;" +
      "uk.gov.hmrc.testuser.ErrorHandler"
  )
}
