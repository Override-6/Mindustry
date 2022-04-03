package mindustry.game;

import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import arc.util.serialization.*;
import arc.util.serialization.Json.*;
import mindustry.content.*;
import mindustry.graphics.g3d.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.type.Weather.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

/**
 * Defines current rules on how the game should function.
 * Does not store game state, just configuration.
 */
public class Rules{
    /** Sandbox mode: Enables infinite resources, build range and build speed. */
    public boolean infiniteResources;
    /** Team-specific rules. */
    public TeamRules teams = new TeamRules();
    /** Whether the waves come automatically on a timer. If not, waves come when the play button is pressed. */
    public boolean waveTimer = true;
    /** Whether waves are spawnable at all. */
    public boolean waves;
    /** Whether the game objective is PvP. Note that this enables automatic hosting. */
    public boolean pvp;
    /** Whether to pause the wave timer until all enemies are destroyed. */
    public boolean waitEnemies = false;
    /** Determinates if gamemode is attack mode. */
    public boolean attackMode = false;
    /** Whether this is the editor gamemode. */
    public boolean editor = false;
    /** Whether a gameover can happen at all. Set this to false to implement custom gameover conditions. */
    public boolean canGameOver = true;
    /** Whether cores change teams when they are destroyed. */
    public boolean coreCapture = false;
    /** Whether reactors can explode and damage other blocks. */
    public boolean reactorExplosions = true;
    /** Whether schematics are allowed. */
    public boolean schematicsAllowed = true;
    /** Whether friendly explosions can occur and set fire/damage other blocks. */
    public boolean damageExplosions = true;
    /** Whether fire is enabled. */
    public boolean fire = true;
    /** Whether units use and require ammo. */
    public boolean unitAmmo = false;
    /** Whether cores add to unit limit */
    public boolean unitCapVariable = true;
    /** How fast unit factories build units. */
    public float unitBuildSpeedMultiplier = 1f;
    /** How much damage any other units deal. */
    public float unitDamageMultiplier = 1f;
    /** Whether to allow units to build with logic. */
    public boolean logicUnitBuild = true;
    /** How much health blocks start with. */
    public float blockHealthMultiplier = 1f;
    /** How much damage blocks (turrets) deal. */
    public float blockDamageMultiplier = 1f;
    /** Multiplier for buildings resource cost. */
    public float buildCostMultiplier = 1f;
    /** Multiplier for building speed. */
    public float buildSpeedMultiplier = 1f;
    /** Multiplier for percentage of materials refunded when deconstructing. */
    public float deconstructRefundMultiplier = 0.5f;
    /** No-build zone around enemy core radius. */
    public float enemyCoreBuildRadius = 400f;
    /** If true, no-build zones are calculated based on the closest core. */
    public boolean polygonCoreProtection = false;
    /** If true, dead teams in PvP automatically have their blocks & units converted to derelict upon death. */
    public boolean cleanupDeadTeams = true;
    /** Radius around enemy wave drop zones.*/
    public float dropZoneRadius = 300f;
    /** Time between waves in ticks. */
    public float waveSpacing = 2 * Time.toMinutes;
    /** Wave after which the player 'wins'. Used in sectors. Use a value <= 0 to disable. */
    public int winWave = 0;
    /** Base unit cap. Can still be increased by blocks. */
    public int unitCap = 0;
    /** Environmental flags that dictate visuals & how blocks function. */
    public int environment = Env.terrestrial | Env.spores | Env.groundOil | Env.groundWater;
    /** Attributes of the environment. */
    public Attributes attributes = new Attributes();
    /** Sector for saves that have them. */
    public @Nullable Sector sector;
    /** Spawn layout. */
    public Seq<SpawnGroup> spawns = new Seq<>();
    /** Starting items put in cores. */
    public Seq<ItemStack> loadout = ItemStack.list(Items.copper, 100);
    /** Weather events that occur here. */
    public Seq<WeatherEntry> weather = new Seq<>(1);
    /** Blocks that cannot be placed. */
    public ObjectSet<Block> bannedBlocks = new ObjectSet<>();
    /** Units that cannot be built. */
    public ObjectSet<UnitType> bannedUnits = new ObjectSet<>();
    /** Reveals blocks normally hidden by build visibility. */
    public ObjectSet<Block> revealedBlocks = new ObjectSet<>();
    /** Unlocked content names. Only used in multiplayer when the campaign is enabled. */
    public ObjectSet<String> researched = new ObjectSet<>();
    /** Block containing these items as requirements are hidden. */
    public ObjectSet<Item> hiddenBuildItems = new ObjectSet<>();
    /** Whether ambient lighting is enabled. */
    public boolean lighting = false;
    /** Whether enemy lighting is visible.
     * If lighting is enabled and this is false, a fog-of-war effect is partially achieved. */
    public boolean enemyLights = true;
    /** Ambient light color, used when lighting is enabled. */
    public Color ambientLight = new Color(0.01f, 0.01f, 0.04f, 0.99f);
    /** team of the player by default. */
    public Team defaultTeam = Team.sharded;
    /** team of the enemy in waves/sectors. */
    public Team waveTeam = Team.crux;
    /** color of clouds that is displayed when the player is landing */
    public Color cloudColor = new Color(0f, 0f, 0f, 0f);
    /** name of the custom mode that this ruleset describes, or null. */
    public @Nullable String modeName;
    /** Whether cores incinerate items when full, just like in the campaign. */
    public boolean coreIncinerates = false;
    /** If false, borders fade out into darkness. Only use with custom backgrounds!*/
    public boolean borderDarkness = true;
    /** special tags for additional info. */
    public StringMap tags = new StringMap();
    /** Name of callback to call for background rendering in mods; see Renderer#addCustomBackground. Runs last. */
    public @Nullable String customBackgroundCallback;
    /** path to background texture with extension (e.g. "sprites/space.png")*/
    public @Nullable String backgroundTexture;
    /** background texture move speed scaling - bigger numbers mean slower movement. 0 to disable. */
    public float backgroundSpeed = 27000f;
    /** background texture scaling factor */
    public float backgroundScl = 1f;
    /** background UV offsets */
    public float backgroundOffsetX = 0.1f, backgroundOffsetY = 0.1f;
    /** Parameters for planet rendered in the background. Cannot be changed once a map is loaded. */
    public @Nullable PlanetParams planetBackground;

    /** Copies this ruleset exactly. Not efficient at all, do not use often. */
    public Rules copy(){
        return JsonIO.copy(this);
    }

    /** Returns the gamemode that best fits these rules. */
    public Gamemode mode(){
        if(pvp){
            return Gamemode.pvp;
        }else if(editor){
            return Gamemode.editor;
        }else if(attackMode){
            return Gamemode.attack;
        }else if(infiniteResources){
            return Gamemode.sandbox;
        }else{
            return Gamemode.survival;
        }
    }

    public boolean hasEnv(int env){
        return (environment & env) != 0;
    }

    public float unitBuildSpeed(Team team){
        return unitBuildSpeedMultiplier * teams.get(team).unitBuildSpeedMultiplier;
    }

    public float unitDamage(Team team){
        return unitDamageMultiplier * teams.get(team).unitDamageMultiplier;
    }

    public float blockHealth(Team team){
        return blockHealthMultiplier * teams.get(team).blockHealthMultiplier;
    }

    public float blockDamage(Team team){
        return blockDamageMultiplier * teams.get(team).blockDamageMultiplier;
    }

    public float buildSpeed(Team team){
        return buildSpeedMultiplier * teams.get(team).buildSpeedMultiplier;
    }

    public boolean setInfiniteResources(boolean infiniteResources) {
        this.infiniteResources = infiniteResources;
        return infiniteResources;
    }

    public TeamRules setTeams(TeamRules teams) {
        this.teams = teams;
        return teams;
    }

    public boolean setWaveTimer(boolean waveTimer) {
        this.waveTimer = waveTimer;
        return waveTimer;
    }

    public boolean setWaves(boolean waves) {
        this.waves = waves;
        return waves;
    }

    public boolean setPvp(boolean pvp) {
        this.pvp = pvp;
        return pvp;
    }

    public boolean setWaitEnemies(boolean waitEnemies) {
        this.waitEnemies = waitEnemies;
        return waitEnemies;
    }

    public boolean setAttackMode(boolean attackMode) {
        this.attackMode = attackMode;
        return attackMode;
    }

    public boolean setEditor(boolean editor) {
        this.editor = editor;
        return editor;
    }

    public boolean setCanGameOver(boolean canGameOver) {
        this.canGameOver = canGameOver;
        return canGameOver;
    }

    public boolean setCoreCapture(boolean coreCapture) {
        this.coreCapture = coreCapture;
        return coreCapture;
    }

    public boolean setReactorExplosions(boolean reactorExplosions) {
        this.reactorExplosions = reactorExplosions;
        return reactorExplosions;
    }

    public boolean setSchematicsAllowed(boolean schematicsAllowed) {
        this.schematicsAllowed = schematicsAllowed;
        return schematicsAllowed;
    }

    public boolean setDamageExplosions(boolean damageExplosions) {
        this.damageExplosions = damageExplosions;
        return damageExplosions;
    }

    public boolean setFire(boolean fire) {
        this.fire = fire;
        return fire;
    }

    public boolean setUnitAmmo(boolean unitAmmo) {
        this.unitAmmo = unitAmmo;
        return unitAmmo;
    }

    public boolean setUnitCapVariable(boolean unitCapVariable) {
        this.unitCapVariable = unitCapVariable;
        return unitCapVariable;
    }

    public float setUnitBuildSpeedMultiplier(float unitBuildSpeedMultiplier) {
        this.unitBuildSpeedMultiplier = unitBuildSpeedMultiplier;
        return unitBuildSpeedMultiplier;
    }

    public float setUnitDamageMultiplier(float unitDamageMultiplier) {
        this.unitDamageMultiplier = unitDamageMultiplier;
        return unitDamageMultiplier;
    }

    public boolean setLogicUnitBuild(boolean logicUnitBuild) {
        this.logicUnitBuild = logicUnitBuild;
        return logicUnitBuild;
    }

    public float setBlockHealthMultiplier(float blockHealthMultiplier) {
        this.blockHealthMultiplier = blockHealthMultiplier;
        return blockHealthMultiplier;
    }

    public float setBlockDamageMultiplier(float blockDamageMultiplier) {
        this.blockDamageMultiplier = blockDamageMultiplier;
        return blockDamageMultiplier;
    }

    public float setBuildCostMultiplier(float buildCostMultiplier) {
        this.buildCostMultiplier = buildCostMultiplier;
        return buildCostMultiplier;
    }

    public float setBuildSpeedMultiplier(float buildSpeedMultiplier) {
        this.buildSpeedMultiplier = buildSpeedMultiplier;
        return buildSpeedMultiplier;
    }

    public float setDeconstructRefundMultiplier(float deconstructRefundMultiplier) {
        this.deconstructRefundMultiplier = deconstructRefundMultiplier;
        return deconstructRefundMultiplier;
    }

    public float setEnemyCoreBuildRadius(float enemyCoreBuildRadius) {
        this.enemyCoreBuildRadius = enemyCoreBuildRadius;
        return enemyCoreBuildRadius;
    }

    public boolean setPolygonCoreProtection(boolean polygonCoreProtection) {
        this.polygonCoreProtection = polygonCoreProtection;
        return polygonCoreProtection;
    }

    public boolean setCleanupDeadTeams(boolean cleanupDeadTeams) {
        this.cleanupDeadTeams = cleanupDeadTeams;
        return cleanupDeadTeams;
    }

    public float setDropZoneRadius(float dropZoneRadius) {
        this.dropZoneRadius = dropZoneRadius;
        return dropZoneRadius;
    }

    public float setWaveSpacing(float waveSpacing) {
        this.waveSpacing = waveSpacing;
        return waveSpacing;
    }

    public int setWinWave(int winWave) {
        this.winWave = winWave;
        return winWave;
    }

    public int setUnitCap(int unitCap) {
        this.unitCap = unitCap;
        return unitCap;
    }

    public int setEnvironment(int environment) {
        this.environment = environment;
        return environment;
    }

    public Attributes setAttributes(Attributes attributes) {
        this.attributes = attributes;
        return attributes;
    }

    public Sector setSector(Sector sector) {
        this.sector = sector;
        return sector;
    }

    public Seq<SpawnGroup> setSpawns(Seq<SpawnGroup> spawns) {
        this.spawns = spawns;
        return spawns;
    }

    public Seq<ItemStack> setLoadout(Seq<ItemStack> loadout) {
        this.loadout = loadout;
        return loadout;
    }

    public Seq<WeatherEntry> setWeather(Seq<WeatherEntry> weather) {
        this.weather = weather;
        return weather;
    }

    public ObjectSet<Block> setBannedBlocks(ObjectSet<Block> bannedBlocks) {
        this.bannedBlocks = bannedBlocks;
        return bannedBlocks;
    }

    public ObjectSet<UnitType> setBannedUnits(ObjectSet<UnitType> bannedUnits) {
        this.bannedUnits = bannedUnits;
        return bannedUnits;
    }

    public ObjectSet<Block> setRevealedBlocks(ObjectSet<Block> revealedBlocks) {
        this.revealedBlocks = revealedBlocks;
        return revealedBlocks;
    }

    public ObjectSet<String> setResearched(ObjectSet<String> researched) {
        this.researched = researched;
        return researched;
    }

    public ObjectSet<Item> setHiddenBuildItems(ObjectSet<Item> hiddenBuildItems) {
        this.hiddenBuildItems = hiddenBuildItems;
        return hiddenBuildItems;
    }

    public boolean setLighting(boolean lighting) {
        this.lighting = lighting;
        return lighting;
    }

    public boolean setEnemyLights(boolean enemyLights) {
        this.enemyLights = enemyLights;
        return enemyLights;
    }

    public Color setAmbientLight(Color ambientLight) {
        this.ambientLight = ambientLight;
        return ambientLight;
    }

    public Team setDefaultTeam(Team defaultTeam) {
        this.defaultTeam = defaultTeam;
        return defaultTeam;
    }

    public Team setWaveTeam(Team waveTeam) {
        this.waveTeam = waveTeam;
        return waveTeam;
    }

    public Color setCloudColor(Color cloudColor) {
        this.cloudColor = cloudColor;
        return cloudColor;
    }

    public String setModeName(String modeName) {
        this.modeName = modeName;
        return modeName;
    }

    public boolean setCoreIncinerates(boolean coreIncinerates) {
        this.coreIncinerates = coreIncinerates;
        return coreIncinerates;
    }

    public boolean setBorderDarkness(boolean borderDarkness) {
        this.borderDarkness = borderDarkness;
        return borderDarkness;
    }

    public StringMap setTags(StringMap tags) {
        this.tags = tags;
        return tags;
    }

    public String setCustomBackgroundCallback(String customBackgroundCallback) {
        this.customBackgroundCallback = customBackgroundCallback;
        return customBackgroundCallback;
    }

    public String setBackgroundTexture(String backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        return backgroundTexture;
    }

    public float setBackgroundSpeed(float backgroundSpeed) {
        this.backgroundSpeed = backgroundSpeed;
        return backgroundSpeed;
    }

    public float setBackgroundScl(float backgroundScl) {
        this.backgroundScl = backgroundScl;
        return backgroundScl;
    }

    public float setBackgroundOffsetX(float backgroundOffsetX) {
        this.backgroundOffsetX = backgroundOffsetX;
        return backgroundOffsetX;
    }

    public float setBackgroundOffsetY(float backgroundOffsetY) {
        this.backgroundOffsetY = backgroundOffsetY;
        return backgroundOffsetY;
    }

    public PlanetParams setPlanetBackground(PlanetParams planetBackground) {
        this.planetBackground = planetBackground;
        return planetBackground;
    }

    /** A team-specific ruleset. */
    public static class TeamRule{
        /** Whether to use building AI. */
        public boolean ai;
        /** TODO Tier of blocks/designs that the AI uses for building. [0, 1] */
        public float aiTier = 1f;
        /** Whether, when AI is enabled, ships should be spawned from the core. */
        public boolean aiCoreSpawn = true;
        /** If true, blocks don't require power or resources. */
        public boolean cheat;
        /** If true, resources are not consumed when building. */
        public boolean infiniteResources;
        /** If true, this team has infinite unit ammo. */
        public boolean infiniteAmmo;

        /** How fast unit factories build units. */
        public float unitBuildSpeedMultiplier = 1f;
        /** How much damage any other units deal. */
        public float unitDamageMultiplier = 1f;
        /** How much health blocks start with. */
        public float blockHealthMultiplier = 1f;
        /** How much damage blocks (turrets) deal. */
        public float blockDamageMultiplier = 1f;
        /** Multiplier for building speed. */
        public float buildSpeedMultiplier = 1f;

        //build cost disabled due to technical complexity
    }

    /** A simple map for storing TeamRules in an efficient way without hashing. */
    public static class TeamRules implements JsonSerializable{
        final TeamRule[] values = new TeamRule[Team.all.length];

        public TeamRule get(Team team){
            TeamRule out = values[team.id];
            if(out == null) values[team.id] = (out = new TeamRule());
            return out;
        }

        @Override
        public void write(Json json){
            for(Team team : Team.all){
                if(values[team.id] != null){
                    json.writeValue(team.id + "", values[team.id], TeamRule.class);
                }
            }
        }

        @Override
        public void read(Json json, JsonValue jsonData){
            for(JsonValue value : jsonData){
                values[Integer.parseInt(value.name)] = json.readValue(TeamRule.class, value);
            }
        }
    }
}
