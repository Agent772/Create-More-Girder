## Unreleased
### Added
- Server config option `placement.createGirderPlacementSystem` (default `true`) to toggle between base Create's girder placement/update system and the CMG mechanic (sticky TOP/BOTTOM connectors + no beam→pole collapse) #163
- Russian translation (`ru_ru.json`) by savereks, mirrored from GitHub PR #18

### Changed
- Renamed the "Beam" family display names to "Girder" (e.g. "Andesite Beam" → "Andesite Girder") #165. Lang-only change: block/item IDs stay `*_beam`, so schematics, KubeJS scripts, datapacks, and existing worlds are unaffected. The Truss family keeps its name.

### Migration
- **The new default (`createGirderPlacementSystem=true`) makes CMG girders behave like base Create's metal girder.** Builds relying on sticky brackets or persistent horizontal beams will degrade lazily (brackets cleared, beams collapsing to poles) on their next neighbor update. To keep the old CMG behavior, set `createGirderPlacementSystem=false` in `serverconfig/createmoregirder-server.toml` **before** opening an existing world.

## Version 2.1.2
### Fixed
- fixed iron bracket using the same recipe as metal bracket #15