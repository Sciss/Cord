lazy val root = project.in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name               := "Cord",
    version            := "0.1.0-SNAPSHOT",
    organization       := "de.sciss",
    scalaVersion       := "2.13.3",
    description        := "A patcher system with Web Audio",
    homepage           := Some(url(s"https://github.com/Sciss/${name.value}")),
    licenses           := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt")),
    libraryDependencies ++= Seq(
      "org.scala-js"  %%% "scalajs-dom" % "1.1.0",
      "com.lihaoyi"   %%% "scalatags"   % "0.9.2"
    ),
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8")
  )
