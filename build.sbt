name := "spark-netsuite"
version := "1.1.0"
organization := "com.springml"

scalaVersion := "2.11.8"

resolvers += "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/"
resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Spark Package Main Repo" at "https://dl.bintray.com/spark-packages/maven"

libraryDependencies ++= Seq(
  "org.mockito" % "mockito-core" % "2.1.0-RC.1",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

libraryDependencies += "com.databricks" %% "spark-csv" % "1.3.0"
libraryDependencies += "org.springframework.ws" % "spring-ws-core" % "2.3.0.RELEASE"
libraryDependencies += "net.sf.saxon" % "Saxon-HE" % "9.7.0-8"
libraryDependencies += "org.apache.ws.commons.axiom" % "axiom-api" % "1.2.19"
libraryDependencies += "org.apache.ws.commons.axiom" % "axiom-impl" % "1.2.19"

parallelExecution in Test := false

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", "axiom.xml", xs @ _*) => MergeStrategy.first
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}

// Spark Package Details (sbt-spark-package)
spName := "springml/spark-netsuite"
spAppendScalaVersion := true
sparkVersion := "2.1.0"
sparkComponents += "sql"

spDependencies += "elsevierlabs-os/spark-xml-utils:1.3.0"

  // Maven Details
publishMavenStyle := true
spIncludeMaven := true
spShortDescription := "Spark NetSuite Connector"
spDescription := """Spark NetSuite Connector
                   | - Creates dataframe using data fetched from NetSuite Web services """.stripMargin

// licenses += "Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0")
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>https://github.com/springml/spark-netsuite</url>
    <licenses>
      <license>
        <name>Apache License, Verision 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/springml/spark-netsuite</connection>
      <developerConnection>scm:git:git@github.com:springml/spark-netsuite</developerConnection>
      <url>github.com/springml/spark-netsuite</url>
    </scm>
    <developers>
      <developer>
        <id>springml</id>
        <name>Springml</name>
        <url>http://www.springml.com</url>
      </developer>
    </developers>)
