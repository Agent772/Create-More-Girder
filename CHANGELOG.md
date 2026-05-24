## Unreleased

### Fixed
- Fixed shaft / cog / fluid-pipe brackets dropping on the ground when their host is broken in **creative** — brackets and any copycat mimic are now discarded with the rest of the block (survival pickaxe / hand breaks still pop bracket and mimic on the ground as before)
- Fixed sneak-wrenching a shaft / cog / fluid-pipe host: bracket and any copycat mimic now go into the player's inventory in survival (overflowing to the ground if full) instead of always popping on the ground; nothing drops in creative
- Fixed track paving doing nothing for Iron Truss and Iron Beam — stale hand-written tag JSONs were shadowing the Registrate-generated `girder` / `girder_encased_shaft` / `paving_girder` tags so the iron entries never reached the runtime tag set

## Version 2.1.0

### Added
- Iron Truss and Iron Beam block families, each with encased shaft and strut variants — six new blocks with crafting recipes using vanilla iron blocks and iron plates
- Ten new bracket variants: Andesite, Brass, Iron, Weathered Iron, Zinc, Copycat, and four waxed copper oxidation stages (Copper, Exposed Copper, Weathered Copper, Oxidized Copper), each snapping onto shafts, cogwheels, and fluid pipes
- Shortcut recipe for Waxed Oxidized Copper Bracket crafted from unwaxed oxidized copper, so fully-oxidized vanilla copper is no longer a dead-end

### Fixed
- Fixed Copycat Truss and Copycat Beam side brackets not adopting the mimic texture when placed next to solid blocks
- Fixed brackets placed on third-party cogwheels using the shaft bracket model instead of the cog bracket model
- Fixed sneak-wrenching a host block with a non-copycat bracket incorrectly routing the bracket through the player's inventory

## Version 2.0.1

### Fixed
- Fixed Copycat Girder blocks rendering stained glass as solid or fully transparent instead of translucent with the correct tint
