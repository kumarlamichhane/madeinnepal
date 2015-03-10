name := "nepalikapas"

version := "1.0.1"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "org.apache.commons" % "commons-email" % "1.3.3",
  "com.typesafe.akka" %% "akka-actor" % "2.2.1"
)

play.Project.playScalaSettings


