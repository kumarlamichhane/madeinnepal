name := "callforblood"

version := "1.0.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
)     

play.Project.playScalaSettings

lazy val main = project.in(file("."))