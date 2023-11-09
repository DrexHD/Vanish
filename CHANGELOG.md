# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Config option to toggle fake join/disconnect messages
- Allow offline players in /vanish command

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
