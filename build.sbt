name := "call4blood"

version := "1.0.1"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "org.apache.commons" % "commons-email" % "1.3.3"
)     

play.Project.playScalaSettings


