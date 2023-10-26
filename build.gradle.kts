plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.8"
}

group = "bambi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
