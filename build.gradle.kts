plugins {
    `java-library`
    java
    `maven-publish`
}

group = "io.musician101"
version = "1.3.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenLocal()
}

dependencies {
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnlyApi("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnlyApi("com.mojang:brigadier:1.0.18")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.musician101"
            artifactId = "bukkitier"
            version = "${project.version}"

            from(components["java"])
        }
    }
}
