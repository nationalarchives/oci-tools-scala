name := "oci-tools-scala"

organization := "uk.gov.nationalarchives.oci"

version := "0.2.0"

scalaVersion := "2.13.3"

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/nationalarchives/oci-tools-scala"))

startYear := Some(2020)

description := "Omega Catalogue Identifier tools"

organizationName := "The National Archives"

organizationHomepage := Some(url("http://nationalarchives.gov.uk"))

scmInfo := Some(ScmInfo(
    url("https://github.com/nationalarchives/oci-tools-scala"),
    "scm:git@github.com:nationalarchives/oci-tools-scala.git")
)

developers := List(
  Developer(
    id    = "adamretter",
    name  = "Adam Retter",
    email = "adam@evolvedbinary.com",
    url   = url("https://www.evolvedbinary.com")
  )
)

headerLicense := Some(HeaderLicense.MIT("2020", "The National Archives"))

libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2"
libraryDependencies += "com.michaelpollmeier" %% "scala-arm" % "2.1"
libraryDependencies += "org.scalameta" %% "munit" % "0.7.17" % "test"
libraryDependencies += "commons-codec" % "commons-codec" % "1.15" % "test"
testFrameworks += new TestFramework("munit.Framework")

// Fancy up the Assembly JAR
packageOptions in (Compile, packageBin) +=  {
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

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultUniversalScript(shebang = false)))

// Add assembly to publish step
artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

// Publish to Maven Repo

publishMavenStyle := true

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots/")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2/")
}

publishArtifact in Test := false
