## Version 2.0.0

### Added
- Plate Girder family — new plate-style girder blocks for all material variants (andesite, brass, copper, exposed copper, oxidized copper, weathered copper, weathered iron, and waxed variants), each with encased shaft and strut sub-variants
- Strut block tag (`#createmoregirder:strut`) covering all strut blocks, for use by data packs and cross-mod compatibility

### Changed
- Renamed all girder block IDs for clarity: `*_girder` → `*_strut_girder` and `*_metal_girder` → `*_plate_girder` (`create_metal_girder_strut` is unchanged). Existing worlds are migrated automatically via registry aliases — placed blocks survive the rename
- Sable light tag now references CMG block tags instead of listing individual blocks, so new girder and strut variants are automatically included

### Fixed
- Fixed rendering of Copycat Girders with translucent or transparent texture blocks
- Fixed Copycat Girder dropping its texture block when destroyed in Creative mode
