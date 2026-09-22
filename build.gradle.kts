plugins {
    id("net.fabricmc.fabric-loom") version "1.17.21"
}

group = "fr.mrrelax"
version = property("mod_version") as String

base {
    archivesName.set("spyglass")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
}

fabricApi {
    configureTests {
        createSourceSet = true
        modId = "spyglass-gametest"
        enableGameTests = false
        enableClientGameTests = true
        eula = true
    }
}

providers.gradleProperty("compat").orNull?.let { profile ->
    dependencies {
        "gametestRuntimeOnly"(fileTree("$profile/mods") {
            include("entity_model_features-*.jar", "entity_texture_features-*.jar", "notenoughanimations-*.jar")
        })
    }
    val copyCompatPacks = tasks.register<Copy>("copyCompatPacks") {
        from("$profile/resourcepacks") {
            include("FreshAnimations_*.zip", "FA+Player-*.zip")
        }
        into(layout.buildDirectory.dir("run/clientGameTest/resourcepacks"))
        mustRunAfter("deleteGameTestRunDir")
    }
    tasks.named("runClientGameTest") {
        dependsOn(copyCompatPacks)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(25)
    }
    jar {
        from("LICENSE")
    }
    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }
}
