
name := """tutorial"""
organization := "com.asdevel"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  javaJdbc,
  cacheApi,
  javaWs
)

libraryDependencies += guice

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
libraryDependencies += "com.drewnoakes" % "metadata-extractor" % "2.10.1"