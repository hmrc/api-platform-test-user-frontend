import scoverage.ScoverageKeys

object ScoverageSettings {
  def apply() = Seq(
    ScoverageKeys.coverageMinimumStmtTotal := 80.00,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    ScoverageKeys.coverageExcludedPackages :=
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
