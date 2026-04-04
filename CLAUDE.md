\# Shrink Mechanic



\## Overview

A 2D top-down game built with \*\*Java 21\*\*, \*\*JavaFX\*\*, and \*\*Maven\*\*.



\## Architecture

The architecture follows an ECS-inspired pattern with systems (TickSystem, RenderSystem, CollisionSystem, EntitySystem, CameraSystem) coordinated by an ObjectRegistry.



\- \*\*Systems are decoupled\*\* — each IGameSystem handles one concern and is registered via ObjectRegistry.

\- \*\*Interface-driven composition\*\* — objects opt into behavior via interfaces (ITickable, IRenderable, ICollidable, IManaged, ICollisionAware).

\- \*\*Chunk-based world\*\* — terrain is generated with Perlin noise and loaded/unloaded around the camera.

\- \*\*GameObjects own no systems\*\* — they are registered into systems externally; the Spawner pattern enables child object creation.



\## Multiplayer Plans (Not Yet Implemented)

We plan to add \*\*client-authoritative multiplayer\*\* using a \*\*listen-server model\*\* with \*\*Steam\*\* integration. One player hosts and is both server and client; others connect via Steam invites.



Code changes should not make multiplayer harder to implement. Watch for:

\- Hard-coded singleton assumptions (single player, single camera, single world).

\- State mutations that would need to be synchronized but aren't clearly separated.

\- Input handling tightly coupled to game logic (should be separable for remote players).

\- Use of static mutable state that can't be partitioned per-client.



\## Code Standards

\- Package: `dev.lucasfransson.shrinkmechanic`

\- Follow existing naming conventions — camelCase fields, PascalCase classes.

\- Prefer composition over inheritance where possible.

\- New world objects should extend WorldObject or Tile and implement IRandomizable if they need seeded variation.

\- Keep the game loop lean — avoid allocations in hot paths (update/render).



\## Build

```

mvn compile        # compile

mvn package        # build jar

mvn javafx:run     # run the game

```

