import sbt._
import play.core.PlayVersion
import play.sbt.PlayImport._

object AppDependencies {
  def apply() = appDependencies

  lazy val appDependencies: Seq[ModuleID] = compile ++ test
  lazy val bootstrapPlayVersion = "2.3.0"
  lazy val playPartialsVersion = "6.11.0-play-26"
  lazy val pegdownVersion = "1.6.0"
  lazy val scalaTestPlusVersion = "3.1.3"
  lazy val wiremockVersion = "1.58"
  lazy val mockitoVersion = "1.10.19"

  lazy val scope: String = "test, it"

  lazy val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-26" % bootstrapPlayVersion,
    "uk.gov.hmrc" %% "play-partials" % playPartialsVersion,
    "uk.gov.hmrc" %% "domain" % "5.10.0-play-26",
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

    "org.seleniumhq.selenium" % "selenium-java" % "3.141.59" % scope,
    "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.141.59" % scope,
    "org.seleniumhq.selenium" % "selenium-chrome-driver" % "3.141.59" % scope,
    "org.mockito" %% "mockito-scala-scalatest" % "1.7.1" % scope
  )
}
