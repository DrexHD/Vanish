# Vanish

## How to Install
1. Download the `.jar` file from GitHub, Modrinth or CurseForge
2. Drop the file into your `mods` folder
3. Restart your server!

## Features
- You are completely invisible and *undetectable*
- No open/close container (chests, barrels, ender chests, and shulker boxes) animations
- You don't make noise (placing, breaking, walking...)
- You are removed from command selectors (`/msg`, `/gamemode`...)
- You get removed from `/list` command
- You don't appear in the server list player sample and player count
- Your advancement, death, join/leave messages, and others are hidden
- Fake join/leave messages are sent on vanish/un-vanish

## Config
The config file is located at `./config/vanish.hocon`. Use `/vanish reload` to reload it.
```hocon
# Show vanish status in the action bar
action-bar=true
# Text to be displayed when placeholder vanish:vanished is used for a vanished player
place-holder-display=" <gray>☠"
#Prevents vanished players from using chat
disable-chat=true
```

## [Permissions](https://github.com/lucko/fabric-permissions-api)
You need to be an operator or have these permissions to be able to use the mod
- `vanish.command.vanish` - Access to `/vanish` command
- `vanish.command.vanish.reload` - Use `/vanish reload` to reload the config
- `vanish.command.vanish.other` - Set the vanish-status of others
- `vanish.feature.view` - See vanished player

## [Placeholders](https://placeholders.pb4.eu/)
- `vanish:vanished` - Displays a text (configurable via config) if a player is vanished
- `vanish:online` - The number of players that the player viewing the placeholder can see

## Developers
Add the modrinth maven repository to your `build.gradle`.
```gradle
repositories {
    // There might be other repos there too, just add it at the end
    maven { url "https://api.modrinth.com/maven" }
}
```
Declare vanish as an optional dependency!
```gradle
dependencies {
    // You will have other dependencies here too
    modImplementation "maven.modrinth:vanish:[VERSION]"
}
```
Before using the API you need to make sure the mod is actually loaded, by checking `FabricLoader.getInstance().isModLoaded("melius-vanish");`. 
If the mod is present you can use [VanishAPI](src/main/java/me/drex/vanish/api/VanishAPI.java) and [VanishEvents](src/main/java/me/drex/vanish/api/VanishEvents.java) to integrate your own mods.