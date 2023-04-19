
lazy val root = Project("kettle-test-framework", file("."))
  .settings(
    name := "oci-tools-scala",
    organization := "uk.gov.nationalarchives.oci",
    version := "0.3.0-SNAPSHOT",
    scalaVersion := "2.13.10",
    licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
    homepage := Some(url("https://github.com/nationalarchives/oci-tools-scala")),
    startYear := Some(2020),
    description := "Omega Catalogue Identifier tools",
    organizationName := "The National Archives",
    organizationHomepage := Some(url("http://nationalarchives.gov.uk")),
    scmInfo := Some(ScmInfo(
      url("https://github.com/nationalarchives/oci-tools-scala"),
      "scm:git@github.com:nationalarchives/oci-tools-scala.git")
    ),
    developers := List(
      Developer(
        id = "adamretter",
        name = "Adam Retter",
        email = "adam@evolvedbinary.com",
        url = url("https://www.evolvedbinary.com")
      )
    ),
    headerLicense := Some(HeaderLicense.MIT("2020", "The National Archives")),
    libraryDependencies ++= Seq(
      "com.github.scopt" %% "scopt" % "4.1.0",
      "com.michaelpollmeier" %% "scala-arm" % "2.1",
      "org.scalameta" %% "munit" % "0.7.29" % "test",
      "commons-codec" % "commons-codec" % "1.15" % "test"
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    publishMavenStyle := true,
    credentials += Credentials (Path.userHome / ".ivy2" / ".credentials"),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots/")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2/")
    },
    Test / publishArtifact := false
  )

// Fancy up the Assembly JAR
Compile / packageBin / packageOptions += {
  import java.text.SimpleDateFormat
  import java.util.Calendar
  import java.util.jar.Manifest
  import scala.sys.process._

  val gitCommit = "git rev-parse HEAD".!!.trim
  val gitTag = s"git name-rev --tags --name-only $gitCommit".!!.trim

  val additional = Map(
    "Build-Timestamp" -> new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance.getTime),
    "Built-By" -> sys.props("user.name"),
    "Build-Tag" -> gitTag,
    "Source-Repository" -> "scm:git:https://github.com/nationalarchives/oci-tools-scala.git",
    "Git-Commit-Abbrev" -> gitCommit.substring(0, 7),
    "Git-Commit" -> gitCommit,
    "Build-Jdk" -> sys.props("java.runtime.version"),
    "Description" -> "Omega Catalog Identifier Tools",
    "Build-Version" -> "N/A",
    "License" -> "MIT"
  )

  val manifest = new Manifest
  val attributes = manifest.getMainAttributes
  for((k, v) <- additional)
    attributes.putValue(k, v)
  Package.JarManifest(manifest)
}

// make the assembly executable with basic shell scripts
import sbtassembly.AssemblyPlugin.defaultUniversalScript

ThisBuild / assemblyPrependShellScript := Some(defaultUniversalScript(shebang = false))

Compile / assembly / artifact := {
  val art = (Compile / assembly / artifact).value
  art.withClassifier(Some("assembly"))
}

addArtifact(Compile / assembly / artifact, assembly)