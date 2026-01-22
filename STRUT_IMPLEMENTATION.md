# Girder Strut Implementation

## Overview
This implementation adds dynamic spanning Girder Struts to Create: More Girder, allowing players to connect two anchor points up to 8 blocks apart with an I-beam structure.

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
     "createmoregirder.strut.too_far": "Too far apart (max 8 blocks)",
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
- **Max Distance:** 8 blocks (configurable via `GirderStrutBlock.MAX_SPAN`)
- **Axis Limit:** Max 2 different axes (prevents pure diagonal connections)
- **Angle Limit:** Max 75° from anchor facing direction
- **Double-Render Prevention:** Only renders from higher Y position (or higher X/Z if Y is equal)

### Rendering
- **Mode:** FAST only (simple segment repetition)
- **Segments:** Calculated based on distance, I-beam model repeated along connection
- **Rotation:** Dynamically calculated to point toward target anchor
- **Render Bounds:** Inflated by 10 blocks to ensure visibility at distance

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
