package mindustry.game;

import arc.*;
import arc.func.*;
import arc.util.*;
import mindustry.maps.*;

/** Defines preset rule sets. */
public enum Gamemode{
    survival(rules -> {
        rules.setWaveTimer(true);
        rules.setWaves(true);
    }, map -> map.spawns > 0),
    sandbox(rules -> {
        rules.setInfiniteResources(true);
        rules.setWaves(true);
        rules.setWaveTimer(false);
    }),
    attack(rules -> {
        rules.setAttackMode(true);
        rules.setWaves(true);
        rules.setWaveTimer(true);

        rules.setWaveSpacing(2f * Time.toMinutes);
        rules.teams.get(rules.waveTeam).infiniteResources = true;
    }, map -> map.teams.size > 1),
    pvp(rules -> {
        rules.setPvp(true);
        rules.setEnemyCoreBuildRadius(600f);
        rules.setBuildCostMultiplier(1f);
        rules.setBuildSpeedMultiplier(1f);
        rules.setUnitBuildSpeedMultiplier(2f);
        rules.setAttackMode(true);
    }, map -> map.teams.size > 1),
    editor(true, rules -> {
        rules.setInfiniteResources(true);
        rules.setEditor(true);
        rules.setWaves(false);
        rules.setWaveTimer(false);
    });

    private final Cons<Rules> rules;
    private final Boolf<Map> validator;

    public final boolean hidden;
    public final static Gamemode[] all = values();

    Gamemode(Cons<Rules> rules){
        this(false, rules);
    }

    Gamemode(boolean hidden, Cons<Rules> rules){
         this(hidden, rules, m -> true);
    }

    Gamemode(Cons<Rules> rules, Boolf<Map> validator){
        this(false, rules, validator);
    }

    Gamemode(boolean hidden, Cons<Rules> rules, Boolf<Map> validator){
        this.rules = rules;
        this.hidden = hidden;
        this.validator = validator;
    }

    /** Applies this preset to this ruleset. */
    public Rules apply(Rules in){
        rules.get(in);
        return in;
    }

    /** @return whether this mode can be played on the specified map. */
    public boolean valid(Map map){
        return validator.get(map);
    }

    public String description(){
        return Core.bundle.get("mode." + name() + ".description");
    }

    @Override
    public String toString(){
        return Core.bundle.get("mode." + name() + ".name");
    }
}
