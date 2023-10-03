package me.drex.vanish.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class VanishConfig {

    @Comment("Show vanish status in the action bar")
    public boolean actionBar = true;

    @Comment("Text to be displayed when placeholder vanish:vanished is used for a vanished player")
    public String placeHolderDisplay = " <gray>☠";

    @Comment("Prevents vanished players from using chat")
    public boolean disableChat = true;

    @Comment("Hide vanished players from entities, prevents hostile entities from targeting players, and more")
    public boolean hideFromEntities = true;

    @Comment("Prevent vanished player world interactions")
    public Interaction interaction = new Interaction();

    @ConfigSerializable
    public static class Interaction {

        @Comment("Prevent block interactions (pressure plates, dripleaf, tripwire, farmland, redstone ore, sculk sensor/shrieker and turtle egg)")
        public boolean blocks = true;

        @Comment("Prevent player vibrations (sculk sensor/shrieker and warden)")
        public boolean vibrations = true;

        @Comment("Prevent mob spawning")
        public boolean mobSpawning = true;

        @Comment("Prevent chunk loading / generation")
        public boolean chunkLoading = false;

        @Comment("Prevent entity collisions")
        public boolean entityCollisions = true;

        @Comment("Prevent entity pickups (arrows, experience orbs, items and tridents)")
        public boolean entityPickup = true;

        @Comment("Prevent world interactions - As if player was in spawn protected area (attacking, breaking blocks, armour stand, chests, riding entity, etc)")
        public boolean worldInteractions = false;
    }

}
