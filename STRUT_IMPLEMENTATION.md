# Girder Strut Implementation

## Overview
This implementation adds dynamic spanning Girder Struts to Create: More Girder, allowing players to connect two anchor points up to 30 blocks apart with an I-beam structure.

## Attribution
**Original Source:** Bits-n-Bobs by Industrialists-Of-Create  
**License:** MIT License  
**Repository:** https://github.com/Industrialists-Of-Create/Bits-n-Bobs  

Significant portions of code, models, and concepts have been adapted from the Bits-n-Bobs mod with modifications for Create: More Girder's architecture.

## Implementation Status

### ✅ Completed
1. **Core Java Classes**
   - `GirderStrutBlock` - Base block with connection mechanics
   - `GirderStrutBlockEntity` - Tracks connections between struts
   - `GirderStrutBlockItem` - 2-step placement logic with data components
   - `AndesiteGirderStrutBlock` - Andesite variant (base for extensibility)
   - `CMGDataComponents` - Data component registry for anchor storage

2. **Rendering System**
   - `GirderStrutBlockEntityRenderer` - FAST mode rendering only
   - `CMGPartialModels` - Partial model registry
   - Dynamic segment repetition along connections

3. **Registration**
   - Block registration in `CMGBlocks`
   - Block entity registration in `CMGBlockEntityTypes`
   - Data component registration in main mod class

4. **Models**
   - `andesite_girder_strut_attachment.json` - 10x6x10 bracket model
   - `andesite_girder_strut.json` - I-beam segment model
   - `andesite_girder_strut_item.json` - Item display model

5. **Recipe**
   - 3 Andesite Girders + 2 Andesite Alloy → 6 Andesite Girder Struts

### ⚠️ Pending
1. **Texture Creation**
   - Need to create `andesite_girder_attachment.png` (16x16)
   - Instructions in `src/main/resources/assets/createmoregirder/textures/block/TEXTURE_INSTRUCTIONS.md`
   - Can copy/recolor from Bits-n-Bobs or create new matching andesite aesthetic

2. **Language Entries**
   - Add to generated lang file:
     ```json
     "block.createmoregirder.andesite_girder_strut": "Andesite Girder Strut",
     "createmoregirder.strut.first_anchor": "First anchor placed. Click another position to complete.",
     "createmoregirder.strut.connected": "Strut connected!",
     "createmoregirder.strut.same_position": "Cannot connect to the same position",
     "createmoregirder.strut.too_far": "Too far apart (max 30 blocks)",
     "createmoregirder.strut.invalid_angle": "Invalid angle (no pure diagonals)",
     "createmoregirder.strut.angle_too_steep": "Angle too steep (max 75°)",
     "createmoregirder.strut.need_more": "Need %s more Girder Struts",
     "createmoregirder.strut.tooltip": "A type of girder used to span a distance between two anchor points."
     ```

## How It Works

### Placement Mechanics
1. **First Click:** Places an anchor block and stores its position in the item's data components
2. **Second Click:** Validates distance, angle, and axis constraints, then connects the two anchors
3. **Connection Storage:** Each strut block entity stores relative positions of connected struts
4. **Bidirectional:** Connections are stored in both anchor blocks

### Connection Constraints
- **Max Distance:** 30 blocks (configurable via `GirderStrutBlock.MAX_SPAN`)
- **Axis Limit:** Max 2 different axes (prevents pure diagonal connections)
- **Angle Limit:** Max 75° from anchor facing direction
- **Double-Render Prevention:** Only renders from higher Y position (or higher X/Z if Y is equal)

### Rendering
- **Mode:** FAST only (simple segment repetition)
- **Segments:** Calculated based on distance, I-beam model repeated along connection
- **Rotation:** Dynamically calculated to point toward target anchor
- **Render Bounds:** Inflated by `MAX_SPAN + 2` blocks to ensure visibility at distance

## Extensibility for Other Variants

The system is designed for easy addition of new variants:

### For Brass, Copper, etc. (Extending Andesite)
1. Create variant block class:
   ```java
   public class BrassGirderStrutBlock extends AndesiteGirderStrutBlock {
       public BrassGirderStrutBlock(Properties properties) {
           super(properties);
       }
   }
   ```

2. Register in `CMGBlocks`:
   ```java
   public static final BlockEntry<BrassGirderStrutBlock> BRASS_GIRDER_STRUT =
       REGISTRATE.block("brass_girder_strut", BrassGirderStrutBlock::new)
           .initialProperties(SharedProperties::softMetal)
           .properties(p -> p.mapColor(MapColor.COLOR_YELLOW).sound(SoundType.NETHERITE_BLOCK).noOcclusion())
           .transform(pickaxeOnly())
           .blockstate((c, p) -> p.simpleBlock(c.get(),
               p.models().getExistingFile(p.modLoc("block/girder_strut/brass_girder_strut_attachment"))))
           .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
           .item(GirderStrutBlockItem::new)
           .model((c, p) -> p.withExistingParent(c.getName(),
               p.modLoc("block/girder_strut/brass_girder_strut_item")))
           .build()
           .register();
   ```

3. Add to block entity valid blocks:
   ```java
   .validBlocks(CMGBlocks.ANDESITE_GIRDER_STRUT, CMGBlocks.BRASS_GIRDER_STRUT)
   ```

4. Add partial model:
   ```java
   public static final PartialModel BRASS_GIRDER_STRUT_SEGMENT = 
       block("girder_strut/brass_girder_strut");
   ```

5. Create 3 model files (copy and change texture paths):
   - `brass_girder_strut_attachment.json`
   - `brass_girder_strut.json`  
   - `brass_girder_strut_item.json`

6. Create texture:
   - `brass_girder_attachment.png`

7. Add recipe using `brass_girder` instead of `andesite_girder`

### For Copper Variants (Separate Base)
Follow the same pattern but extend `GirderStrutBlock` directly if you want different base behavior.

## Testing Checklist
- [ ] Texture created and looks good
- [ ] Can place first anchor
- [ ] Can place second anchor and connection forms
- [ ] Connection renders correctly in world
- [ ] Breaking one strut removes connection from other
- [ ] Recipe works (3 girders + 2 alloy = 6 struts)
- [ ] Item tooltip shows correctly
- [ ] Placement messages appear
- [ ] Validation prevents invalid connections (too far, wrong angle, etc.)
- [ ] Works with Create schematics (SAFE_NBT tag)
- [ ] No console errors

## Known Limitations
- **FAST rendering only** - No fancy plane clipping or advanced lighting
- **Simple geometry** - Segments may clip into blocks at steep angles
- **No wrench behavior** - Unlike regular girders, doesn't rotate/configure with wrench
- **Single variant renderer** - All variants use the same rendering logic (texture differences only)

## Future Enhancements
- Add FANCY rendering mode with plane clipping
- Add wrench behavior for breaking entire connections
- Support weathering/oxidation transitions for copper variants
- Add config options for max span and angle limits
- Optimize rendering with cached buffers per variant

## StrutYourStuff Library Evaluation

Evaluated in issue #12. The upstream strut system from Bits-n-Bobs has been extracted into a
standalone library called [StrutYourStuff](https://github.com/cakeGit/StrutYourStuff)
(`com.cake.struts:struts`), published to `maven.azmod.net`.

### Key Findings

- **License:** MIT (declared in `neoforge.mods.toml`). Both external dependency and `jarJar`
  bundling are viable. External dependency is preferred for parity with how Create is consumed.
- **Stable release available:** `1.1.1+mc1.21.1` on `maven.azmod.net/releases`, CurseForge, and
  Modrinth. The "wait for stable" concern from the original issue is resolved.
- **8-variant support:** `StrutModelType` is a `record`, not an enum. Consumers can construct
  unlimited instances, so our 8 material variants map cleanly.
- **MAX_SPAN:** The library uses 30, matching our updated value.
- **Library coverage:** 1:1 mapping for all of our strut classes (block, block entity, item,
  renderer, geometry, mesh, cap, model builder, model manipulator, placement effects, relighter).
- **#9 (length-scaled item consumption):** Not covered by the library. `StrutBlockItem` only
  consumes anchor count (max 2), not span length. Custom item logic is still needed either way.
- **API maturity:** Young library (~16 commits, single author). Pin to an exact version if
  adopting.

### Migration Decision

Proceed with migration as a **separate issue** once timing aligns with #9. The library is MIT,
stable, covers our full scope, and supports our 8 variants. Coordinate with #9 to avoid
implementing length-scaled consumption twice.

### Integration Path (for future migration)

- Add `maven.azmod.net/releases` repository and `com.cake.struts:struts:1.1.1+mc1.21.1` dependency
  to `build.gradle`
- Declare `struts` as a required dependency in `neoforge.mods.toml`
- Replace `GirderStrutBlock` with a thin subclass of `com.cake.struts.content.block.StrutBlock`
- Replace `GirderStrutBlockEntity` with a subclass of `StrutBlockEntity`
- Remove the 11 geometry/mesh/renderer classes the library covers
- Validate rendering parity across all 8 variants in FAST and Fancy mode
- Verify `IBE<GirderStrutBlockEntity>` coexists with the library's `EntityBlock` interface
- Pin to exact version `1.1.1+mc1.21.1`, not a range
