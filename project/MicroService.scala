import sbt.Keys._
import sbt.Tests.{SubProcess, Group}
import sbt._
import play.routes.compiler.StaticRoutesGenerator
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._


trait MicroService {

  import uk.gov.hmrc._
  import DefaultBuildSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt, SbtAutoBuildPlugin}
  import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
  import uk.gov.hmrc.versioning.SbtGitVersioning
  import play.sbt.routes.RoutesKeys.routesGenerator


  import TestPhases._

  val appName: String

  lazy val appDependencies : Seq[ModuleID] = ???
  lazy val plugins : Seq[Plugins] = Seq.empty
  lazy val playSettings : Seq[Setting[_]] = Seq.empty

  def unitFilter(name: String): Boolean = name startsWith "unit"
  def itTestFilter(name: String): Boolean = name startsWith "it"

  lazy val microservice = Project(appName, file("."))
    .enablePlugins(Seq(play.sbt.PlayScala,SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin) ++ plugins : _*)
    .settings(playSettings : _*)
    .settings(scalaSettings: _*)
    .settings(publishingSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
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
      unmanagedSourceDirectories in IntegrationTestWithSASS <<= (baseDirectory in IntegrationTestWithSASS)(base => Seq(base / "test")),
      unmanagedResourceDirectories in IntegrationTestWithSASS <<= (baseDirectory in IntegrationTestWithSASS)(base => Seq(base / "test", base /"target/web/public/test")),
      addTestReportOption(IntegrationTestWithSASS, "int-test-reports"),
      testGrouping in IntegrationTestWithSASS := oneForkedJvmPerTest((definedTests in IntegrationTestWithSASS).value),
      parallelExecution in IntegrationTestWithSASS := false)
      .settings(resolvers ++= Seq(
        Resolver.bintrayRepo("hmrc", "releases"),
        Resolver.jcenterRepo
      ))
}

private object TestPhases {

  lazy val IntegrationTestWithSASS = config("it") extend Test

  def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
    tests map {
      test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
    }
}
