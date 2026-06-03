package de.yolacraft.client.mixin.accessor;

import net.caffeinemc.mods.sodium.client.gui.VideoSettingsScreen;
import net.caffeinemc.mods.sodium.client.gui.widgets.OptionListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VideoSettingsScreen.class)
public interface VideoSettingsScreenAccessor {

    @Accessor("optionList")
    OptionListWidget getOptionList();
}