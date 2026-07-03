# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

ArcadeCore is a Paper/Spigot Minecraft plugin (group `org.drappula`, plugin main class `org.drappula.arcadeCore.ArcadeCore`) providing core lobby/arcade-server functionality (target: Paper 1.21.11).

## Build & run commands

- `./gradlew build` — compiles and produces the shadowed plugin jar (shadowJar runs as part of `build`).
- `./gradlew shadowJar` — build the relocated, dependency-bundled jar directly (output in `build/libs`).
- `./gradlew runServer` — launches a local Paper 1.21.11 test server with the plugin installed (via `xyz.jpenilla.run-paper`); use this for manual in-game testing. Server heap is set to `-Xms2G -Xmx2G`.
- There is no test suite configured in this project.

## Architecture

- **Language/toolchain**: Java 21 and Kotlin (`kotlin("jvm")` plugin applied, targeting JVM 21), built with Gradle Kotlin DSL.
- **Shadow/relocation**: `com.gradleup.shadow` bundles and relocates `dev.dejvokep:boosted-yaml` (both the core and spigot-serializer artifacts) under `me.plugin.libs` to avoid classpath conflicts with other plugins on the same server (see `build.gradle.kts` `shadowJar` block).
- **Plugin entry point**: `ArcadeCore` (`src/main/java/org/drappula/arcadeCore/ArcadeCore.java`) is a singleton accessible via `ArcadeCore.get()`. `onEnable()` initializes `DataConfig` and registers commands through the lifecycle event `LifecycleEvents.COMMANDS` using the Brigadier-based command API (`Commands.registrar()`), not the legacy `plugin.yml` commands map — there is no `plugin.yml`, only `paper-plugin.yml`.
- **Config layer**: Two YAML-backed config classes under `config/`, both using `dev.dejvokep.boostedyaml.YamlDocument` with `SpigotSerializer` (so Bukkit types like `Location` can be (de)serialized directly) and `BasicVersioning` keyed on a `version` field for auto-updating the file to the packaged default:
  - `DataConfig` (`src/main/resources/data.yml`) — runtime/mutable state (currently just `SPAWN_LOCATION`). Instantiated once in `ArcadeCore#onEnable`; static accessor `DataConfig.get()` returns the `YamlDocument`, plus helpers `setSpawnLocation`/`getSpawnLocation`.
  - `MainConfig` (`src/main/resources/config.yml`) — admin-facing settings (e.g. `lobby.teleport-spawn`, `lobby.gamemode`). **Note:** unlike `DataConfig`, `MainConfig` is never instantiated in `ArcadeCore#onEnable` and `LobbyListener` is never registered as a listener — both are referenced from other classes but not currently wired up at plugin startup. When working on lobby/config features, check whether this wiring needs to be added.
- **Commands**: `MainCommand.get()` builds a single Brigadier command tree under `/arcade` with subcommands `reload` (reloads `DataConfig`) and `setspawn` (saves the invoker's location to `DataConfig`); the bare `/arcade` prints plugin name/version/description using MiniMessage (`sendRichMessage`/`TagResolver`).
- **Listeners**: `LobbyListener` (`listeners/`) handles `PlayerJoinEvent` — teleports to the saved spawn (if `lobby.teleport-spawn` is enabled), resets health to max, and sets the gamemode from `MainConfig`.
- **Resource filtering**: `paper-plugin.yml` uses Gradle `processResources` token expansion for `${description}` and `${version}`, sourced from `gradle.properties`.
