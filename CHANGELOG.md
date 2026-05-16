## Version 2.0.0

### Added
- Plate Girder family (registered as `*_beam`) — new plate-style girder blocks for andesite, brass, copper + 3 weathered + 4 waxed copper variants, weathered iron, and copycat, each with encased shaft and strut sub-variants. No Industrial Iron Beam — Industrial Iron is exclusive to the Truss family
- `create_metal_girder_strut` — a dedicated strut that bridges Create's own vanilla `create:metal_girder` blocks
- Strut block tag (`#createmoregirder:strut`) covering all strut blocks, for use by data packs and cross-mod compatibility

### Changed
- Renamed all girder block IDs for clarity: `*_girder` → `*_truss` and `*_metal_girder` → `*_beam` (`create_metal_girder_strut` is unchanged). Display names follow suit (e.g. `Brass Girder` → `Brass Truss`, `Brass Metal Girder` → `Brass Beam`). Existing 1.x worlds are migrated automatically via Forge's `MissingMappingsEvent` — placed blocks and stored items survive the rename
