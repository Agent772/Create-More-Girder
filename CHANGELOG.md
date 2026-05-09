## Version 1.2.0

### Added
- Copycat Girder and Copycat Strut blocks that adopt the texture of any block placed against them, letting them blend seamlessly into any build
- Girder struts can now be included in Create contraptions — struts assemble onto bearings, pistons, and cart assemblers and move correctly with the structure
- A placement-cost overlay now appears while positioning a strut, showing the total material cost based on beam length before you confirm the placement
- CMG girders can now be used to pave rails under curved track sections, in addition to straight segments
- Girders are registered as light blocks for Create Aeronautics (Sable) compatibility
- Wrench rotation helpers are now available for all CMG girder variants
- Girder strut maximum span increased to 30 blocks

### Fixed
- Fixed item duplication caused by orphaned anchor blocks dropping items without an attached strut
- Fixed Copycat Strut anchor attachment rendering too far inward when placed on pole-shaped girders
- Fixed struts rejecting placement when the anchor block sits at a different Y level than the target
- Fixed TOP brackets not being filled when paving certain track layouts with CMG girders
- Fixed schematics reporting only the anchor count rather than the full material cost for struts
- Fixed multiple struts being connectable to the same anchor block
- Fixed girder visuals not rendering correctly when using the Iris shader pack
- Fixed the wrong girder item dropping when breaking a curved track section paved with CMG girders
- Fixed a spurious center column appearing when paving straight track with CMG girders
