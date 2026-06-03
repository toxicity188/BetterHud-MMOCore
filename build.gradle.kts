plugins {
    id("java")
    `maven-publish`
}

group = "kr.toxicity.hud.addon.mmocore"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:26.1.2-R0.1-SNAPSHOT")
    implementation("io.github.toxicity188:BetterHud-bukkit-api:2.0.0")
    compileOnly("io.lumine:MythicLib-dist:1.7.1-SNAPSHOT")
    compileOnly("net.Indyuce:MMOCore-API:1.13.1-SNAPSHOT")
    compileOnly("net.Indyuce:MMOItems-API:6.10.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    testCompileOnly("org.projectlombok:lombok:1.18.46")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.46")

    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    test {
        useJUnitPlatform()
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "version" to project.version
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}