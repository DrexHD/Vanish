# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- `hideGameMode` config option
- `spectatorAdvancementProgress` config option

## [1.5.20] - 2025-07-02
### Added
- Prevent allays from interacting with vanished players

## [1.5.19] - 2025-07-02
### Added
- Hide waypoints of vanished players (1.21.6+)

## [1.5.18] - 2025-06-09
### Fixed
- Minecart mixin in 1.21.4

## [1.5.17] - 2025-05-26
### Added
- Prevent phantom spawning 

## [1.5.16] - 2025-05-23
### Changed
- Use stonecutter to support 1.21.1, 1.21.4 and 1.21.5

## [1.5.15] - 2025-05-03
### Fixed
- Invalid character in event phase id

## [1.5.14] - 2025-05-01
### Added
- Vanish join status event

### Changed
- Mod id to `vanish`

## [1.5.13] - 2025-03-25
### Fixed
- NoClassDefFoundError on launch (again)

## [1.5.12] - 2025-03-21
### Added
- Pl3xMap support

### Changed
- Optimize permission lookup

## [1.5.11] - 2025-02-03
### Fixed
- NoClassDefFoundError on launch

## [1.5.10] - 2025-02-03
### Added
- Integrated server support

## [1.5.9] - 2024-11-17
### Fixed
- Incompatibility with trade_cycling mod

## [1.5.8] - 2024-10-25
### Fixed
- Crash on 1.21.2/3

## [1.5.7] - 2024-09-18
### Added
- Moonrise compatibility

### Fixed
- Containers remaining open by vanished players in certain conditions
- Pressure plates remaining pressed by vanished players in certain conditions
- Vanished players blocking merchant (villager / wandering trader) trading
- Players can attack vanished players

## [1.5.6] - 2024-06-20
### Added
- Prevent advancement progress config option
- Prevent trial spawner detection
- Prevent vault detection

## [1.5.5] - 2024-05-31
### Added
- Vanished players don't obstruct block placement

## [1.5.4] - 2024-04-24
### Fixed
- Crash on startup

## [1.5.3] - 2024-04-22
### Added
- Squaremap support

## [1.5.2] - 2024-02-10
### Added
- Tatar Translation (Thanks to Amirhan-Taipovjan-Greatest-I)
- Prevent raid spawning

### Removed
- Expanded Storage compatibility

## [1.5.1] - 2024-01-17
### Added
- `vanish_on_join` meta key
- Hide vanished players from maps

### Changed
- Improved vanish lookup performance

## [1.5.0] - 2023-12-01
### Added
- Config option to toggle fake join/disconnect messages
- Allow offline players in /vanish command
- Prevent even more sounds
- Prevent special mob spawning (spawners, skeleton traps, zombie reinforcements)
- Prevent sweeping edge attacking players
- Prevent dispensers equipping armor

### Fixed
- Projectiles colliding/hitting players
- Boat and Minecart collisions
- Queries showing actual player count
- Polydex tooltips of vanished players
- Vanished players stopping minecarts

## [1.4.3] - 2023-10-04
### Added
- Config option to make vanished players invulnerable
- Config option for disabling private messages

### Fixed
- Hidden message command feedback
- Wardens can smell vanished players

## [1.4.2] - 2023-06-09
### Fixed
- Version constraint

## [1.4.1] - 2023-05-26
### Added
- Hide traceable entities
- Dynmap support

### Fixed
- Immersive Portals incompatibility

## [1.4.0] - 2023-03-13
### Added
- Prevent player inside block interactions
- Prevent player step on block interactions
- Prevent player vibrations
- Prevent chunk generation and mob spawning
- Prevent entity collisions
- Prevent entity pickup
- Experience orbs don't follow vanished
- Config options for world interactions

## [1.3.2] - 2023-01-25
### Added
- Expanded Storage compatibility
- Exclude vanished players from player count

## [1.3.1] - 2022-12-24
### Added
- Hide vanished player sleeping status 

## [1.3.0] - 2022-12-14
### Added
- BlueMap support

## [1.2.0] - 2022-12-14
### Added
- `boolean canViewVanished(SharedSuggestionProvider src)` to VanishAPI 
- hideFromEntities Config option
- StyledChat v2 compatibility

### Changed
- Updated to 1.19.3

### Fixed
- Modrinth dependency resolution

## [1.1.0] - 2022-09-20
### Added
- Hide falling particles
- Hide block destroy progress
- Developer API notes to the Readme
- Config option for chat disabling

### Changed
- Improve packet filtering
- Improve performance

### Fixed
- Environment sound are no longer removed

## [1.0.2] - 2022-09-14
### Changed
- Improve workflows

### Fixed
- Minecraft version lock

## [1.0.1] - 2022-09-14
### Added
- Changelog
- Release workflow

### Changed
- Update mod icon
- Include all required dependencies

## [1.0.0] - 2022-09-13
### Added
- Basic functionality
- API for developers
- Placeholders
- Mod compatibility with StyledChat
