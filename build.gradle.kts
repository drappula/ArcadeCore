plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "9.4.3"
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("dev.dejvokep:boosted-yaml:1.3.7")
    implementation("dev.dejvokep:boosted-yaml-spigot:1.5")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.shadowJar {
    dependencies {
        include(dependency("dev.dejvokep:boosted-yaml:1.3.7"))
        include(dependency("dev.dejvokep:boosted-yaml-spigot:1.5"))
    }

    // Relocates the packages to prevent conflicts
    relocate("dev.dejvokep.boostedyaml", "me.plugin.libs")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.11")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
kotlin {
    jvmToolchain(21)
}