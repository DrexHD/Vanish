import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginExtension

plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
    id("maven-publish")
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("com.gradleup.shadow") version "9.2.2"
    id("org.jetbrains.changelog")
}

version = findProperty("mod_version") as String + "+" + findProperty("minecraft_version")
group = findProperty("maven_group") as String

base {
    archivesName = findProperty("archives_base_name") as String
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(findProperty("java_version") as String)
}

repositories {
    mavenCentral()
    maven("https://maven.nucleoid.xyz/")
    maven("https://api.modrinth.com/maven")
    maven("https://jitpack.io")
    maven("https://repo.mikeprimm.com")
    maven("https://repo.bluecolored.de/releases") // BlueMapAPI
}

loom {
    splitEnvironmentSourceSets()

    runConfigs.all {
        ideConfigGenerated(true)
    }
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())

fun DependencyHandlerScope.includeMod(dep: String) {
    include(modImplementation(dep)!!)
}

dependencies {
    minecraft("com.mojang:minecraft:${findProperty("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${findProperty("loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${findProperty("fabric_version")}")

    includeMod("me.lucko:fabric-permissions-api:${findProperty("permission_api_version")}")
    includeMod("eu.pb4:placeholder-api:${findProperty("placeholder_api_version")}")
    includeMod("eu.pb4:player-data-api:${findProperty("player_data_api_version")}")
    includeMod("xyz.nucleoid:server-translations-api:${findProperty("translations_version")}")

    shadow("org.spongepowered:configurate-hocon:${findProperty("configurate_hocon_version")}")

    // Mod compat
    modCompileOnly("maven.modrinth:styled-chat:${findProperty("styled_chat_version")}")
    compileOnly("de.bluecolored:bluemap-api:${findProperty("bluemap_api_version")}")
    compileOnly("us.dynmap:DynmapCoreAPI:${findProperty("dynmap_api_version")}")
    compileOnly("xyz.jpenilla:squaremap-api:${findProperty("squaremap_api")}")
    compileOnly("maven.modrinth:pl3xmap:${findProperty("pl3xmap_version")}")
}

publishMods {
    file.set(tasks.remapJar.get().archiveFile)
    type.set(STABLE)
    changelog.set(fetchChangelog())

    displayName = "Vanish ${version.get()}"
    modLoaders.add("fabric")
    modLoaders.add("quilt")


    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        projectId = "676275"
        minecraftVersions.addAll(findProperty("curseforge_minecraft_versions")!!.toString().split(", "))
    }
    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        projectId = "UL4bJFDY"
        minecraftVersions.addAll(findProperty("modrinth_minecraft_versions")!!.toString().split(", "))
    }
    github {
        accessToken = providers.environmentVariable("GITHUB_TOKEN")
        repository = providers.environmentVariable("GITHUB_REPOSITORY").getOrElse("DrexHD/Vanish")
        commitish = providers.environmentVariable("GITHUB_REF_NAME").getOrElse("main")
    }
}

stonecutter {
    replacements.string(eval(current.version, "<=1.21.10")) {
        replace("Identifier", "ResourceLocation")
        replace("identifier()", "location()")
        replace("net.minecraft.world.entity.npc.villager.", "net.minecraft.world.entity.npc.")
        replace("import net.minecraft.world.entity.vehicle.minecart.", "import net.minecraft.world.entity.vehicle.")
        replace("net.minecraft.advancements.criterion.", "net.minecraft.advancements.critereon.")
        replace("net.minecraft.util.Util", "net.minecraft.Util")
    }

    swaps["profile_class"] = when {
        eval(current.version, "<=1.21.8") -> "com.mojang.authlib.GameProfile"
        else -> "net.minecraft.server.players.NameAndId"
    }
    swaps["player_profile"] = when {
        eval(current.version, "<=1.21.8") -> "getGameProfile()"
        else -> "nameAndId()"
    }
}

tasks {
    remapJar {
        dependsOn(shadowJar)
        input.set(shadowJar.get().archiveFile)
    }

    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        minimize()
    }

    processResources {
        val props = mapOf(
            "version" to project.version,
            "javaVersion" to findProperty("java_version")
        )

        inputs.properties(props)

        filesMatching(listOf("fabric.mod.json")) {
            expand(props)
        }
    }
}

fun fetchChangelog(): String {
    val log = rootProject.extensions.getByType<ChangelogPluginExtension>()
    val modVersion = findProperty("mod_version")!!.toString()
    return if (log.has(modVersion)) {
        log.renderItem(
            log.get(modVersion).withHeader(false),
            Changelog.OutputType.MARKDOWN
        )
    } else {
        ""
    }
}