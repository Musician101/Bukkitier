buildscript {
    configurations {
        classpath {
            resolutionStrategy {
                force("org.ow2.asm:asm:9.6")
                force("org.ow2.asm:asm-commons:9.6")
            }
        }
    }
}

plugins {
    `java-library`
    `maven-publish`
}

group = "io.musician101"
version = "2.1.0"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnlyApi("com.mojang:brigadier:1.2.9")
    compileOnlyApi("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
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
