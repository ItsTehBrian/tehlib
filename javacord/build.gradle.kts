plugins {
  id("tehlib.java-conventions")
}

repositories {
  mavenCentral()
}

dependencies {
  api(project(":tehlib-user"))
  api(project(":tehlib-cloud"))
  api(project(":tehlib-configurate"))

  api("cloud.commandframework:cloud-javacord:1.8.0")
  api("org.javacord:javacord:3.7.0")
}
