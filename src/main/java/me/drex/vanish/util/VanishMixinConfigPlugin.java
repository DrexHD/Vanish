package me.drex.vanish.util;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VanishMixinConfigPlugin implements IMixinConfigPlugin {

    public static final boolean EXPANDED_STORAGE = FabricLoader.getInstance().isModLoaded("expandedstorage");
    public static final boolean IMMERSIVE_PORTAL_CORE = FabricLoader.getInstance().isModLoaded("imm_ptl_core");

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!EXPANDED_STORAGE && mixinClassName.startsWith("me.drex.vanish.mixin.compat.expandedstorage")) {
            return false;
        }
        if (IMMERSIVE_PORTAL_CORE && mixinClassName.startsWith("me.drex.vanish.mixin.compat.not_imm_ptl_core")) {
            return false;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
