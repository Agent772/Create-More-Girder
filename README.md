
# Create: More Girder

A NeoForge mod that adds more girder variants to the Create mod ecosystem.

## Project Overview

This mod is built on the foundation of Create: Diesel Generators' andesite girder implementation. It adds multiple girder variants including:

- Andesite Girder (base from Create: Diesel Generators)
- Brass Girder
- Copper Girder  
- Iron Girder
- Zinc Girder

## Dependencies

### Required
- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.209
- **Create**: 6.0.0

### Optional
- **Create: Diesel Generators**: 1.2i (soft dependency for andesite girder patterns)

## Build Setup

The mod is configured to use Create: Diesel Generators as a compile-only dependency, meaning:
1. You can compile the mod with access to CDG's andesite girder classes
2. The mod runs independently - CDG is not required at runtime
3. If CDG is present, players get the original andesite girder; if not, this mod provides its own

## License

This project is licensed under the MIT License. It incorporates code patterns and designs from Create: Diesel Generators (also MIT licensed) by jesz.

## Development Notes

- All girder items appear in Create's base creative tab
- Uses Create's Registrate system for automatic recipe and model generation
- Follows Create mod naming conventions and patterns
- Built with NeoForge 1.21.1 toolchain

## Texture Requirements

You'll need to create or modify the following texture files:
- `src/main/resources/assets/createmoregirder/textures/block/andesite_girder.png`
- `src/main/resources/assets/createmoregirder/textures/block/brass_girder.png`
- `src/main/resources/assets/createmoregirder/textures/block/copper_girder.png`
- `src/main/resources/assets/createmoregirder/textures/block/iron_girder.png`
- `src/main/resources/assets/createmoregirder/textures/block/zinc_girder.png`

The andesite girder texture should be based on Create: Diesel Generators' implementation, while the others can be variants with different material colors.

## Additional Resources
Community Documentation: https://docs.neoforged.net/  
NeoForged Discord: https://discord.neoforged.net/
