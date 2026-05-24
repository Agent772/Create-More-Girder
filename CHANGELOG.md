## Unreleased

### Fixed
- Fixed shaft / cog / fluid-pipe brackets dropping on the ground when their host is broken in **creative** — brackets and any copycat mimic are now discarded with the rest of the block (survival pickaxe / hand breaks still pop bracket and mimic on the ground as before)
- Fixed sneak-wrenching (pickup) a shaft / cog / fluid-pipe with a non-copycat bracket popping the bracket on the ground — the bracket now follows the host into the player's inventory in survival (overflow to the ground if full) and is discarded in creative, matching the existing copycat-bracket pickup behaviour
- Fixed track paving with **Iron Truss** and **Iron Beam** doing nothing — the legacy hand-written `paving_girder` / `girder` tag JSONs in `src/main/resources` were shadowing the data-generated tags and missing the new iron entries, so the paver could not recognise iron girders. The stale tag files are now removed; the Registrate-generated tag set under `src/generated/resources` is the single source of truth

## Version 2.1.0

### Added
- Iron Truss and Iron Beam block families, each with encased shaft and strut variants — six new blocks with crafting recipes using vanilla iron blocks and iron plates
- Ten new bracket variants: Andesite, Brass, Iron, Weathered Iron, Zinc, Copycat, and four waxed copper oxidation stages (Copper, Exposed Copper, Weathered Copper, Oxidized Copper), each snapping onto shafts, cogwheels, and fluid pipes
- Shortcut recipe for Waxed Oxidized Copper Bracket crafted from unwaxed oxidized copper, so fully-oxidized vanilla copper is no longer a dead-end

### Fixed
- Fixed Copycat Truss and Copycat Beam side brackets not adopting the mimic texture when placed next to solid blocks
- Fixed brackets placed on third-party cogwheels using the shaft bracket model instead of the cog bracket model
- Fixed sneak-wrenching a host block with a non-copycat bracket incorrectly routing the bracket through the player's inventory
