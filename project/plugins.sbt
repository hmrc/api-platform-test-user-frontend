resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"
resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)

addSbtPlugin("uk.gov.hmrc"        %  "sbt-auto-build"         % "3.20.0")
addSbtPlugin("uk.gov.hmrc"        %  "sbt-distributables"     % "2.4.0")
addSbtPlugin("org.playframework"  %  "sbt-plugin"             % "3.0.1")
addSbtPlugin("io.github.irundaia" %  "sbt-sassify"            % "1.5.2")
addSbtPlugin("org.scoverage"      %  "sbt-scoverage"          % "2.0.9")
addSbtPlugin("org.scalastyle"     %% "scalastyle-sbt-plugin"  % "1.0.0")
addSbtPlugin("org.scalameta"      %  "sbt-scalafmt"           % "2.5.2")
addSbtPlugin("ch.epfl.scala"      %  "sbt-bloop"              % "1.5.13")
addSbtPlugin("ch.epfl.scala"      %  "sbt-scalafix"           % "0.11.1")
addSbtPlugin("uk.gov.hmrc"        % "sbt-test-report"         % "0.22.0")

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
