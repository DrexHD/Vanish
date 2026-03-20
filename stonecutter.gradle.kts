plugins {
    id("dev.kikugie.stonecutter")
    id("org.jetbrains.changelog") version "2.2.0"
}
stonecutter active "26.1"

changelog {
    path = rootProject.file("CHANGELOG.md").path
}

stonecutter {
    tasks {
        order("publishGithub")
        order("publishModrinth")
        order("publishCurseforge")
    }
}

stonecutter parameters {
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