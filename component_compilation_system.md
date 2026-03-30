# Component Compilation System — Technical Design Summary

## The Problem

I'm building a grid-based isometric automation game with an organic/biological theme. Players place biological components — logic flowers, vine wires, signal mushrooms — and connect them together to build circuits and item pipelines. The endgame goal is that a dedicated player could theoretically build a fully working computer within the game.

The problem is performance. A working computer requires thousands or millions of logic gates. If the game has to simulate every single gate on every single game tick, performance will collapse long before the player gets anywhere close to a CPU. I need a system that lets the player build arbitrarily complex things while keeping the game running smoothly.

## The Core Idea

When a player finishes building a circuit and "packages" it into a reusable single-tile component, the game analyzes what they built and tries to replace the internal simulation with something mathematically equivalent but dramatically cheaper to evaluate at runtime. The player never sees this — their organism just "matures" and works efficiently.

This isn't one technique. It's a cascade of strategies, each catching things the previous one missed, layered from most optimized to least optimized.

## Foundation: The Port and Signal System

Every component in the game — whether it's a single primitive gate or a massive packaged circuit — exposes the same interface: a list of typed ports (inputs and outputs) and a `tick()` method that advances it by one game step.

Ports carry typed signals:

- **Binary** — true/false, used for logic
- **Integer** — numeric values, used for addressing, counting, data
- **Item** — physical game items flowing through pipelines

This uniformity is critical. A compiled mega-component with 10,000 internal gates looks identical from the outside to a single primitive gate: just ports in, ports out, tick to advance. This is what makes nesting work — a compiled component can be used inside a larger circuit, which itself gets compiled, and so on indefinitely.

## Step 0: Trait Analysis

Before any compilation happens, the system determines *what kind of thing* it's looking at. Every primitive component in the game self-declares its traits:

- **PURE_LOGIC** — output depends only on current inputs, nothing else
- **STATEFUL** — output depends on internal memory (like a flip-flop or counter)
- **ENVIRONMENTAL** — output depends on world state (time of day, weather, season)
- **RANDOM** — output involves randomness
- **ITEM_HANDLER** — processes physical items, not just signals

When the player packages a composite component, the system walks through every child component inside it and unions their traits together. If every child is `PURE_LOGIC`, the composite is pure logic. If even one child deep inside is `RANDOM`, the composite contains randomness. This propagates automatically through any depth of nesting.

These traits determine which compilation strategy applies, and they make the decision instant — no need to analyze the circuit topology just to figure out what category it falls into.

## Step 1: Structural Pattern Recognition (First Pass)

Before anything else, the compiler scans the internal circuit graph for known structural patterns. This is cheap and produces guaranteed-correct results.

The approach is subgraph matching. The compiler maintains a library of known patterns — two cross-coupled NAND gates form an SR latch, a specific arrangement of gates forms a D flip-flop, eight flip-flops with shared enable form a register, and so on. It scans the circuit looking for subgraphs that match these templates.

This is done greedily, largest patterns first:

1. Scan for RAM arrays (largest, most valuable optimization)
2. Scan for registers
3. Scan for counters, shift registers
4. Scan for individual flip-flops and latches
5. Each match claims those nodes; they're removed from further analysis

Each recognized pattern gets replaced by a native Java implementation. An SR latch becomes a single boolean variable. A register becomes a byte. A RAM block becomes a byte array. The performance gain is enormous — a player-built 64KB RAM bank that contains thousands of interconnected gates becomes, at runtime, a 65,536-element array with an index lookup.

**Importantly, this step is optional.** The system works without it. Pattern recognition is a first-pass optimization that avoids the need for more expensive analysis on well-known structures. Everything it misses falls through to the next steps.

As the game evolves, I can add new patterns to the library. Every player's existing circuits automatically benefit on their next recompile — I can frame this in-game as their organisms "evolving."

## Step 2: Trait-Based Compilation of Remaining Clusters

After structural pattern matching has claimed what it can, the remaining unclaimed nodes form clusters. Each cluster is categorized based on the combined traits of its components, and a strategy is selected.

### Pure Logic Clusters → Truth Table

If every component in the cluster is `PURE_LOGIC` and the cluster has no feedback loops, then the output is a pure function of the inputs. The compiler builds a truth table by exhaustively testing every possible input combination, simulating the cluster once for each, and recording the output.

At runtime, evaluation is a single array lookup. A cluster with 4 inputs has 16 rows. With 8 inputs, 256 rows. With 16 inputs, 65,536 rows — still trivially small. The practical ceiling is around 20 inputs (about 1 million rows), above which the table gets too large and the cluster stays as raw simulation or gets split into smaller pieces.

This is guaranteed correct because every possible input has been tested. There's no approximation.

### Stateful Clusters (No Randomness) → Lazy Factor-Aware Cache

If the cluster contains `STATEFUL` or `ENVIRONMENTAL` components but no `RANDOM` components, the output depends on the inputs plus some set of additional factors (internal state, world conditions). The compiler doesn't try to enumerate all possible combinations upfront. Instead, it uses a lazy caching strategy.

The cache key is composed of:

- The current input values
- The current internal state (hashed from all internal wire values)
- Any relevant environment values (time of day discretized into periods, current weather, current season, etc.)

Environment values are discretized into small enums so they contribute a manageable number of bits to the cache key. Time of day isn't a continuous float — it's `DAWN`, `DAY`, `DUSK`, or `NIGHT`, giving four possible values.

The first time a particular combination of (inputs + state + environment) is encountered during gameplay, the system simulates the cluster gate-by-gate, records the result, and caches it. Every subsequent time that same combination occurs, it's an instant lookup.

This means:

- **No upfront compilation cost.** Packaging is instant.
- **Only real-world combinations get cached.** If the player never feeds a particular input pattern into the component, the system never wastes time computing it.
- **Components get faster over time.** A factory that's been running for an hour has nearly everything cached. The organisms are "settling in."
- **Memory is efficient.** Only transitions that actually occur in practice are stored.

If the cache encounters a combination it hasn't seen, it falls back to gate-level simulation for that one tick, caches the result, and next time it's instant. The game gets progressively faster the longer it runs.

### Clusters Containing Randomness → Isolation

If a cluster contains any `RANDOM` components, those specific random components are isolated. The compiler draws a boundary around each random subcomponent and compiles the deterministic regions on either side of it normally (as truth tables, caches, or whatever applies).

At runtime, the deterministic regions evaluate via their compiled form (instant), the random components simulate every tick (unavoidable, but they're individual components, not entire circuits), and boundary signals propagate between them.

So a circuit that goes: complex logic → randomizer → complex logic becomes: truth table lookup → one simulated component → truth table lookup. The vast majority of the computation is optimized away despite the presence of randomness.

### Item Pipeline Clusters → Buffer Simulation

Components that handle physical items are modeled as buffers with known capacity and timing. Items move discretely (one position per N ticks) and this is simulated directly, but the logic controlling item flow can still be compiled. A vine that routes items based on a logic signal becomes a compiled truth table controlling which buffer items enter.

## Step 3: The Execution Graph

The output of compilation is a hybrid execution graph. Some nodes are compiled (truth tables, native pattern implementations, caches). Some are raw simulated (random components, irreducibly complex clusters). They're all wired together with boundary edges that propagate signals between regions.

The execution order is topologically sorted so that each node evaluates only after all its upstream dependencies have been evaluated. Each game tick, the system walks this execution order:

1. Evaluate each node (compiled nodes are instant lookups, raw nodes simulate gate-by-gate)
2. After each node, propagate boundary signals to downstream nodes
3. Move items through pipeline buffers
4. Process entity behaviors (bees carrying items, etc.)

## Step 4: Render Distance Optimization

Inside the player's render distance, components need visual representation — vines animating, flowers pulsing, items visibly traveling. Outside render distance, all visual processing is dropped entirely. The compiled execution graphs still tick, but with zero rendering overhead.

A compiled truth table or cached state machine doesn't care whether anyone is watching. This means a massive factory spanning hundreds of chunks costs almost nothing for the distant portions while still producing correct outputs that feed into the parts the player is looking at.

## Nesting and Recursive Compilation

When a player packages a component, it becomes a single tile with exposed ports. When they use that inside a larger circuit and package *that*, the compiler sees it as a black box with known behavior.

If a parent circuit is pure combinational and contains compiled children that are also pure combinational, the compiler can inline the children's truth tables into the parent's truth table. Ten levels of nesting collapses to one array lookup at runtime — zero additional cost over a flat circuit.

For stateful compiled children, the parent compilation treats them as opaque nodes with known factor sets. The parent's cache keys incorporate the child's state as an additional factor.

The original gate-level blueprint is always preserved alongside the compiled form. The player can open and edit any packaged component, which triggers recompilation. Save files store both the blueprint (source of truth) and the compiled cache (so recompilation isn't needed on every load).

## What This Achieves

The player builds whatever they want using simple biological primitives. They don't need to know about truth tables, state machines, or compilation strategies. The game silently analyzes their creation and finds the fastest possible way to run it.

In the best case (pure logic, manageable input count), the compiled form is a single array lookup regardless of internal complexity — a thousand-gate adder and a two-gate AND have the same runtime cost.

In the typical case (mixed logic and state), most of the circuit compiles to instant lookups and only small pockets simulate gate-by-gate.

In the worst case (lots of randomness, irreducibly complex feedback), the system gracefully degrades to direct simulation, but only for the specific parts it couldn't optimize. Everything around them still benefits from compilation.

Over time, the lazy caching means the game naturally gets faster as components encounter their common operating patterns. A mature factory runs faster than a freshly built one.

This makes the endgame — a player-built computer running an operating system — technically feasible. The CPU they build is compiled down to transition tables. The RAM is a byte array. The ALU is a truth table. What was millions of gate evaluations per tick becomes a handful of array lookups.
