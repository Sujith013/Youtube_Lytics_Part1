name := """app_project"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.15"

libraryDependencies += guice
libraryDependencies += "com.google.apis" % "google-api-services-youtube" % "v3-rev20240514-2.0.0"
libraryDependencies ++= Seq(
  "com.google.apis" % "google-api-services-youtube" % "v3-rev20240514-2.0.0",
  "com.google.api-client" % "google-api-client" % "2.6.0",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.36.0",
  "com.google.http-client" % "google-http-client-jackson2" % "1.44.2"
)

