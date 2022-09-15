package me.drex.vanish.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class VanishConfig {

    @Comment("Show vanish status in the action bar")
    public boolean actionBar = true;

    @Comment("Text to be displayed when placeholder vanish:vanished is used for a vanished player")
    public String placeHolderDisplay = " <gray>☠";

}