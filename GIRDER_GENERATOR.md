# Material Girder Generator Script

This directory contains a PowerShell script to automatically generate new material girder variants from the brass girder template.

## Script

**File:** `create-material-girder.ps1`

**Usage:**
```powershell
.\create-material-girder.ps1 [material]
```

**Examples:**
```powershell
.\create-material-girder.ps1 copper
.\create-material-girder.ps1 zinc
.\create-material-girder.ps1 iron
```

## What the Script Does

1. **Copy Java Files**: Creates copies of all brass girder Java class files
   - `BrassGirderBlock.java` → `[Material]GirderBlock.java`
   - `BrassGirderEncasedShaftBlock.java` → `[Material]GirderEncasedShaftBlock.java`
   - `BrassGirderPlacementHelper.java` → `[Material]GirderPlacementHelper.java`

2. **Copy Model Files**: Creates copies of all model JSON files
   - `src/main/resources/assets/createmoregirder/models/block/brass_girder/` → `[material]_girder/`
   - `src/main/resources/assets/createmoregirder/models/block/brass_girder_encased_shaft/` → `[material]_girder_encased_shaft/`

3. **Text Replacement**: Replaces all instances of "brass" with your material in all copied files:
   - `brass_girder` → `[material]_girder`
   - `BrassGirder` → `[Material]Girder`
   - `BRASS_GIRDER` → `[MATERIAL]_GIRDER`
   - Package names, class names, variable names, and model references are all updated

4. **Directory Structure**: Creates new directories:
   ```
   src/main/java/com/agent772/createmoregirder/content/[material]_girder/
   src/main/resources/assets/createmoregirder/models/block/[material]_girder/
   src/main/resources/assets/createmoregirder/models/block/[material]_girder_encased_shaft/
   ```

## After Running the Script

The script creates both Java class files and JSON model files. You'll need to manually:

1. **Register Blocks**: Add the new blocks to `CMGBlocks.java`
2. **Register Block Entities**: Add block entity types to `CMGBlockEntityTypes.java`
3. **Copy Textures**: Copy and rename `brass_girder.png` to `[material]_girder.png`
4. **Run Data Generation**: Execute `.\gradlew runData` to generate recipes and tags

## Example Output

Running `.\create-material-girder.ps1 copper` creates:

**Java Classes:**
- `src/main/java/com/agent772/createmoregirder/content/copper_girder/CopperGirderBlock.java`
- `src/main/java/com/agent772/createmoregirder/content/copper_girder/CopperGirderEncasedShaftBlock.java`
- `src/main/java/com/agent772/createmoregirder/content/copper_girder/CopperGirderPlacementHelper.java`

**Model Files:**
- `src/main/resources/assets/createmoregirder/models/block/copper_girder/` (all JSON files)
- `src/main/resources/assets/createmoregirder/models/block/copper_girder_encased_shaft/` (all JSON files)

All references to "brass" are replaced with "copper" throughout the files.

## Error Handling

- Validates material parameter is provided
- Checks if source directory exists
- Warns if target directory already exists
- Provides clear error messages and next steps

## Supported Materials

Any material name works! Common Create mod materials:
- `copper`
- `zinc` 
- `iron`
- `andesite` (already exists)
- `brass` (template)

The scripts handle case conversion automatically (copper → Copper → COPPER as needed).