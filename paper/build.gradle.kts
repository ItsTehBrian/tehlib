plugins {
    id("tehlib.java-conventions")
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/") {
        name = "papermc"
    }
}

dependencies {
    api(project(":tehlib-core"))

    api("cloud.commandframework", "cloud-paper", "1.6.2")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}
