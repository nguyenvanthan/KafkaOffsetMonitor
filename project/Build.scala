import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import com.typesafe.sbt.SbtNativePackager.autoImport._


object KafkaUtilsBuild extends Build {

  def sharedSettings = Seq(
    version := "0.3.0-SNAPSHOT",
    scalaVersion := "2.10.3",
    organization := "com.quantifind",
    scalacOptions := Seq("-deprecation", "-unchecked", "-optimize"),
    unmanagedJars in Compile <<= baseDirectory map { base => (base / "lib" ** "*.jar").classpath },
    retrieveManaged := true,
    transitiveClassifiers in Scope.GlobalScope := Seq("sources"),
    resolvers ++= Seq(
      "sonatype-snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "http://oss.sonatype.org/content/repositories/releases",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "JBoss Repository" at "http://repository.jboss.org/nexus/content/repositories/releases/"),
    libraryDependencies ++= Seq(
      "log4j" % "log4j" % "1.2.17",
      "org.scalatest" %% "scalatest" % "1.9.1" % "test",
      "org.apache.kafka" %% "kafka" % "0.8.2.1"))

  val slf4jVersion = "1.6.1"

  def debianSettings = Seq(
    maintainer := "Someone <someone@company.com>",
    packageSummary := "Kafka offset monitor, fork from https://github.com/quantifind/KafkaOffsetMonitor",
    packageDescription := "Kafka offset monitor web application")

  def offsetmonSettings = Seq(
    mergeStrategy in assembly := {
       case "about.html"                                => MergeStrategy.discard
       case x =>
  		val oldStrategy = (mergeStrategy in assembly).value
      		oldStrategy(x)
  	},
    name := "kafka-offset-monitor",
    libraryDependencies ++= Seq(
      "net.databinder" %% "unfiltered-filter" % "0.8.4",
      "net.databinder" %% "unfiltered-jetty" % "0.8.4",
      "net.databinder" %% "unfiltered-json4s" % "0.8.4",
      "com.quantifind" %% "sumac" % "0.3.0",
      "com.typesafe.slick" %% "slick" % "2.0.0",
      "org.xerial" % "sqlite-jdbc" % "3.7.2",
      "com.twitter" % "util-core" % "3.0.0",
      "org.reflections" % "reflections" % "0.9.10"),
    resolvers ++= Seq(
      "java m2" at "http://download.java.net/maven/2",
      "twitter repo" at "http://maven.twttr.com"))

  //offsetmonitor project
  lazy val offsetmonitor = (project in file(".")).
    enablePlugins(com.typesafe.sbt.packager.debian.DebianPlugin).
    enablePlugins(com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging).
    settings(sharedSettings: _*).
    settings(debianSettings: _*).
    settings(offsetmonSettings: _*)
}
