import _root_.play.sbt.routes.RoutesKeys.routesGenerator
import play.core.PlayVersion
import play.routes.compiler.StaticRoutesGenerator
import play.sbt.PlayImport._
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.{SbtAutoBuildPlugin, _}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.versioning.SbtGitVersioning

lazy val appName = "api-platform-test-user-frontend"
lazy val appDependencies: Seq[ModuleID] = compile ++ test
lazy val frontendBootstrapVersion = "8.9.0"
lazy val playPartialsVersion = "6.1.0"
lazy val hmrcTestVersion = "3.0.0"
lazy val scalaTestVersion = "2.2.6"
lazy val pegdownVersion = "1.6.0"
lazy val scalaTestPlusVersion = "1.5.1"
lazy val wiremockVersion = "1.58"
lazy val mockitoVersion = "1.10.19"

lazy val compile = Seq(
  ws,
  "uk.gov.hmrc" %% "frontend-bootstrap" % frontendBootstrapVersion,
  "uk.gov.hmrc" %% "play-partials" % playPartialsVersion)

lazy val scope: String = "test, it"

lazy val test = Seq(
  "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
  "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % scope,
  "org.pegdown" % "pegdown" % pegdownVersion % scope,
  "org.jsoup" % "jsoup" % "1.8.1" % scope,
  "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
  "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusVersion % scope,
  "org.mockito" % "mockito-core" % mockitoVersion % scope,
  "com.github.tomakehurst" % "wiremock" % wiremockVersion % scope,
  "org.seleniumhq.selenium" % "selenium-java" % "2.53.1" % "test,it",
  "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % "2.52.0"
)

lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty

def unitFilter(name: String): Boolean = name startsWith "unit"
def itTestFilter(name: String): Boolean = name startsWith "it"

lazy val microservice = (project in file("."))
  .enablePlugins(Seq(_root_.play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin) ++ plugins: _*)
  .settings(playSettings: _*)
  .settings(scalaSettings: _*)
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    name := appName,
    scalaVersion := "2.11.11",
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    parallelExecution in Test := false,
    fork in Test := false,
    testOptions in Test := Seq(Tests.Filter(unitFilter)),
    routesGenerator := StaticRoutesGenerator
  )
  .configs(IntegrationTestWithSASS)
  .settings(inConfig(IntegrationTestWithSASS)(Defaults.itSettings): _*)
  .settings(
    Keys.fork in IntegrationTestWithSASS := false,
    testOptions in IntegrationTestWithSASS := Seq(Tests.Filter(itTestFilter)),
    unmanagedSourceDirectories in IntegrationTestWithSASS <<= (baseDirectory in IntegrationTestWithSASS) (base => Seq(base / "test")),
    unmanagedResourceDirectories in IntegrationTestWithSASS <<= (baseDirectory in IntegrationTestWithSASS) (base => Seq(base / "test", base / "target/web/public/test")),
    addTestReportOption(IntegrationTestWithSASS, "int-test-reports"),
    testGrouping in IntegrationTestWithSASS := oneForkedJvmPerTest((definedTests in IntegrationTestWithSASS).value),
    parallelExecution in IntegrationTestWithSASS := false)
  .settings(resolvers ++= Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.jcenterRepo
  ))

lazy val IntegrationTestWithSASS = config("it") extend Test

def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
  tests map {
    test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
  }

