# Create: More Girder - AI Coding Instructions

## Project Overview
This is a **NeoForge Minecraft mod** that extends the **Create mod** to add more girder variants. The project uses:
- **NeoForge 21.1.209** for Minecraft 1.21.1
- **Create mod integration** with Registrate for item registration
- **Java 21** toolchain with Gradle build system

## Critical Development Context

### Architecture & Dependencies
- **Main class**: `CreateMoreGirder` (note: constructor mistakenly named `CreateShuffleFilter` - needs fixing)
- **Registrate pattern**: Use `CreateRegistrate` from Create mod for all item/block registration
- **Create integration**: Items automatically appear in Create's base creative tab via `AllCreativeModeTabs.BASE_CREATIVE_TAB`
- **Required dependencies**: Create, Ponder, Flywheel, Registrate (versions referenced but not defined in gradle.properties)

### Key Project Structure
```
src/main/java/com/agent772/createmoregirder/   # Main mod code
src/main/resources/assets/examplemod/          # Resources (needs updating to createmoregirder)
src/main/templates/META-INF/                   # Mod metadata templates
src/generated/resources/                       # Data generation output
```

### Development Workflow
- **Run configurations**: Use `gradlew runClient`, `gradlew runServer`, `gradlew runData` for testing
- **Build**: `gradlew build` (outputs to `build/libs/`)
- **Data generation**: Required for recipes, tags, models - use `runData` configuration
- **IDE setup**: IntelliJ IDEA or Eclipse recommended, auto-downloads sources/javadoc

### Critical Patterns & Conventions

#### Item Registration (Registrate Pattern)
```java
public static final ItemEntry<FilterItem> ITEM_NAME = REGISTRATE
    .item("item_id", ItemClass::constructor)
    .lang("Display Name")
    .register();
```

#### Mod Structure Requirements
- **Mod ID**: `createmoregirder` (defined in gradle.properties)
- **Package**: `com.agent772.createmoregirder`
- **Main class**: Must be annotated with `@Mod(CreateMoreGirder.MODID)`
- **Event bus**: Register Registrate with `REGISTRATE.registerEventListeners(modEventBus)`

#### Resource Organization
- Textures: `src/main/resources/assets/createmoregirder/textures/`
- Language files: `src/main/resources/assets/createmoregirder/lang/en_us.json`
- Models: Generated via data generation or manually in `assets/createmoregirder/models/`

### Known Issues to Address
1. **Constructor naming mismatch**: Main class has wrong constructor name
2. **Missing version properties**: Create dependencies reference undefined gradle properties
3. **Template resources**: Still using "examplemod" namespace instead of "createmoregirder"
4. **Archive name**: build.gradle sets archivesName to "create-shufflefilter" but should match mod purpose

### NeoForge-Specific Considerations
- Use **NeoForge APIs**, not legacy Forge APIs
- **Run configurations** are pre-configured in build.gradle
- **Parchment mappings** provide better parameter names
- **Data generation** is essential for proper mod functionality
- **Access transformers** and **mixins** supported but not currently used

### Create Mod Integration Points
- Extend Create's item types (FilterItem, BlockItem, etc.)
- Use Create's creative tabs and item groups
- Follow Create's naming conventions for consistency
- Leverage Create's Registrate for automatic recipe/model generation

### Build System Notes
- **Java 21** required (ships with MC 1.21.1)
- **Gradle wrapper** included - always use `gradlew` commands
- **Multi-project** structure possible but currently single-mod
- **Publishing** configured to local Maven repository in `build/repo/`

When implementing new features:
1. Use Registrate for all registry objects
2. Follow Create mod's patterns and conventions
3. Ensure proper localization in language files
4. Run data generation after adding new content
5. Test in both client and server environments