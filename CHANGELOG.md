## Unreleased

### Added
- Nine new bracket variants: Andesite, Brass, Iron, Weathered Iron, Zinc, and the four waxed copper oxidation stages (Copper, Exposed, Weathered, Oxidized). Each variant uses Create's bracket model and snaps onto shafts, cogwheels, and fluid pipes the same way the vanilla Create bracket does.
- Shortcut recipe: a Waxed Oxidized Copper Bracket can be crafted directly from unwaxed `minecraft:oxidized_copper` (copper nuggets + copper sheets + andesite alloy) so fully-oxidized vanilla copper is no longer a dead-end. The other three waxed copper brackets still require their matching `waxed_*_copper` block.

### Fixed
- Copycat Bracket now renders its mimicked texture on pipes / cogs / shafts added by other mods that extend Create's base classes (e.g. TFMG, Create: Diesel Generators). Previously the bracket attached correctly but always fell back to the default zinc skin on non-Create hosts.
- Brackets placed on third-party cogwheels (any block that implements Create's `ICogWheel` marker — TFMG, Diesel Generators, ...) now use the COG bracket model instead of the SHAFT bracket.
- Sneak-wrenching a host that owns a non-copycat bracket no longer routes the bracket through the player's inventory / vanishes in creative — only copycat brackets follow the new capture path; vanilla and CMG metal brackets pop on the ground as before.

## Version 2.0.1

### Fixed
- Fixed Copycat Truss and Copycat Beam blocks (and their strut anchors and encased shaft variants) rendering incorrectly with translucent mimic materials such as stained glass — they now render translucent as expected instead of appearing fully transparent or fully opaque
