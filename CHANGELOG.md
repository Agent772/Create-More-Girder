## Unreleased
### Added
- Server config option `placement.createGirderPlacementSystem` (default `true`) to toggle between base Create's girder placement/update system and the CMG mechanic (sticky TOP/BOTTOM connectors + no beam→pole collapse) #163

### Migration
- **The new default (`createGirderPlacementSystem=true`) makes CMG girders behave like base Create's metal girder.** Builds relying on sticky brackets or persistent horizontal beams will degrade lazily (brackets cleared, beams collapsing to poles) on their next neighbor update. To keep the old CMG behavior, set `createGirderPlacementSystem=false` in `serverconfig/createmoregirder-server.toml` **before** opening an existing world.

## Version 2.1.2
### Fixed
- fixed iron bracket using the same recipe as metal bracket #15