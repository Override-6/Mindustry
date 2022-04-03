package mindustry.core;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.maps.*;
import mindustry.type.*;
import mindustry.world.blocks.*;

import static mindustry.Vars.*;

public class GameState{
    public int wave = 1;
    public float wavetime;
    public double tick;
    public boolean gameOver = false, serverPaused = false;
    public int serverTps = -1;
    public Map map = emptyMap;
    public Rules rules = new Rules();
    public GameStats stats = new GameStats();
    public Attributes envAttrs = new Attributes();
    public Teams teams = new Teams();
    public int enemies;
    private State state = State.menu;

    @Nullable
    public Unit boss(){
        return teams.bosses.firstOpt();
    }

    public void set(State astate){
        //cannot pause when in multiplayer
        if(astate == State.paused && net.active()) return;

        Events.fire(new StateChangeEvent(state, astate));
        setState(astate);
    }

    public boolean hasSpawns(){
        return rules.waves && !(isCampaign() && rules.attackMode);
    }

    /** Note that being in a campaign does not necessarily mean having a sector. */
    public boolean isCampaign(){
        return rules.sector != null;
    }

    public boolean hasSector(){
        return rules.sector != null;
    }

    @Nullable
    public Sector getSector(){
        return rules.sector;
    }

    public boolean isEditor(){
        return rules.editor;
    }

    public boolean isPaused(){
        return (is(State.paused) && !net.active()) || (gameOver && (!net.active() || isCampaign())) || (serverPaused && !isMenu());
    }

    public boolean isPlaying(){
        return (state == State.playing) || (state == State.paused && !isPaused());
    }

    /** @return whether the current state is *not* the menu. */
    public boolean isGame(){
        return state != State.menu;
    }

    public boolean isMenu(){
        return state == State.menu;
    }

    public boolean is(State astate){
        return state == astate;
    }

    public State getState(){
        return state;
    }

    public int setWave(int wave) {
        this.wave = wave;
        return wave;
    }

    public float setWavetime(float wavetime) {
        this.wavetime = wavetime;
        return wavetime;
    }

    public double setTick(double tick) {
        this.tick = tick;
        return tick;
    }

    public boolean setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        return gameOver;
    }

    public boolean setServerPaused(boolean serverPaused) {
        this.serverPaused = serverPaused;
        return serverPaused;
    }

    public int setServerTps(int serverTps) {
        this.serverTps = serverTps;
        return serverTps;
    }

    public Map setMap(Map map) {
        this.map = map;
        return map;
    }

    public Rules setRules(Rules rules) {
        this.rules = rules;
        return rules;
    }

    public GameStats setStats(GameStats stats) {
        this.stats = stats;
        return stats;
    }

    public Attributes setEnvAttrs(Attributes envAttrs) {
        this.envAttrs = envAttrs;
        return envAttrs;
    }

    public Teams setTeams(Teams teams) {
        this.teams = teams;
        return teams;
    }

    public int setEnemies(int enemies) {
        this.enemies = enemies;
        return enemies;
    }

    public State setState(State state) {
        this.state = state;
        return state;
    }

    public enum State{
        paused, playing, menu
    }
}
