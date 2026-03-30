# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
mvn javafx:run       # Run the game
mvn clean compile    # Compile only
mvn clean package    # Build distributable jar
```

Java 21, JavaFX 21, Maven build system. Main class: `dev.lucasfransson.shrinkmechanic.core.Main`.

## Architecture Overview

This is a JavaFX 2D top-down game with a custom engine. The code is split between generic engine systems (`engine/`) and game-specific logic (`world/`, `entities/`, `items/`).

### Core Loop

`Main.java` wires everything together and starts `GameLoop` (extends `AnimationTimer`, ~60fps). Each frame:
1. `CollisionSystem.updateDynamicPositions()` — reindex moving objects in spatial hash grid
2. `TickSystem.update(deltaTime)` — call `update()` on all `ITickable` objects
3. `GameWorld.updateChunks()` — load/unload chunks near camera
4. `RenderSystem` collects visible sprites, advances animations
5. `GameCanvas.render()` draws to JavaFX Canvas

### ObjectRegistry Pattern

Central registry that connects objects to systems. When an object is constructed, it calls `tryRegister()` on each system. Systems check `instanceof` and register if applicable. On destruction, `ObjectRegistry.destroy()` unregisters from all systems. Feature interfaces: `ITickable`, `IRenderable`, `ICollidable`, `ICollisionAware`, `IDestroyable`.

### Collision System

Spatial hash grid with 64-bit cell keys. Separates static and dynamic objects. `moveWithCollision(dx, dy, deltaTime)` in `Entity.java` does AABB collision: tries horizontal move, reverts if blocked, then vertical move, reverts if blocked.

### World Generation

`GameWorld` manages chunks (`CHUNK_SIZE=16` cells, `CHUNK_LOAD_RADIUS=1` → 3×3 chunk grid loaded). `Chunk.java` uses seeded Perlin noise for tile/object placement. `chunk.load(registry)` instantiates all objects; `chunk.unload(registry)` destroys them.

### Key Constants (`GameConfig.java`)

| Constant | Value | Meaning |
|---|---|---|
| `GRID_CELL_SIZE` | 32 | pixels per world cell |
| `CHUNK_SIZE` | 16 | cells per chunk edge |
| `CHUNK_LOAD_RADIUS` | 1 | chunks loaded around camera |

### Entity Physics

`Entity.java` has velocity + drag. `applyPhysics()` separates overlapping entities, calls `moveWithCollision()`, then applies drag. `LocalPlayer` reads WASD from `InputManager` and moves accordingly.

### Rendering

`RenderSystem` collects sprites from all `IRenderable` objects, sorts by `renderingLayer`. `Camera` lerps toward player, supports zoom (0.25–5.0×, Alt+scroll). `GameCanvas` converts world→screen coordinates and draws via JavaFX `GraphicsContext`.

### Future features

- Client-authorative multiplayer support via Steam using a listen-server model
- Machinery and logic gates in the form of plants, animals and other organisms.
- The player should be able to package a region in the world, so that this region fits into one tile. The packages should be able to keep have machinery and logic inside them and have inputs and outputs
- A compilation system described in `component_compilation_system.md`

### How to use this file

Update this file whenever information that might be relevant for Claude Code to remember is mentioned.
