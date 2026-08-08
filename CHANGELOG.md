## Version 2.2.0

### Added
- Added a configurable server-side toggle (`girder.createGirderPlacementSystem`) to switch between CMG's girder placement behavior (sticky TOP/BOTTOM connectors, no beam-to-pole collapse) and base Create's standard girder behavior. Defaults to `true` (base Create rules). Syncs to clients on server join.
- Added Russian (ru_RU) translation by savereks.

### Changed
- Renamed all "Beam" display names to "Girder" (e.g. "Andesite Beam" → "Andesite Girder"). Block and item IDs are unchanged, so existing worlds, schematics, KubeJS scripts, and datapacks are unaffected.

### Fixed
- Fixed copycat girder mimic rendering for blocks that have no `cullface` quads (e.g. Foxy's Mod glowing blocks), multi-layer translucent blocks, and emissive/glow-shell blocks. Each render layer now draws on its own pass with correct brightness and alpha blending, matching base Create's copycat step behavior.
- Fixed iron bracket sharing the same crafting recipe as the metal bracket.
- Fixed a mixin remap issue causing incorrect behavior and oversized mod file size.

### Migration
- **The new default (`createGirderPlacementSystem=true`) makes CMG girders behave like base Create's metal girder.** Builds relying on sticky brackets or persistent horizontal beams will degrade lazily (brackets cleared, beams collapsing to poles) on their next neighbor update. To keep the old CMG behavior, set `createGirderPlacementSystem=false` in `serverconfig/createmoregirder-server.toml` **before** opening an existing world.
