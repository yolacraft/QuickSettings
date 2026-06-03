package de.yolacraft.client.mixin.sodium;

import de.yolacraft.client.FavoriteManager;
import de.yolacraft.client.mixin.accessor.VideoSettingsScreenAccessor;
import net.caffeinemc.mods.sodium.client.config.structure.Option;
import net.caffeinemc.mods.sodium.client.gui.VideoSettingsScreen;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.caffeinemc.mods.sodium.client.gui.widgets.OptionListWidget;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VideoSettingsScreen.class)
public class SodiumFavoriteClickMixin {

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onMouseClicked(MouseButtonEvent event, boolean doubleClick,
                                CallbackInfoReturnable<Boolean> cir) {

        if (event.button() != 0) return;

        VideoSettingsScreen screen = (VideoSettingsScreen)(Object)this;
        OptionListWidget list = ((VideoSettingsScreenAccessor) screen).getOptionList();

        double mouseX = event.x();
        double mouseY = event.y();

        for (ControlElement element : list.getControls()) {
            String optionName = element.getOption().getName().getString();

            if (!FavoriteManager.isAvailable(optionName)) {
                continue;
            }

            int starX = element.getX() -6;
            int starY = element.getY() + (element.getHeight() / 2) - 4;
            int size = 10;

            if (mouseX >= starX && mouseX <= starX + size && mouseY >= starY && mouseY <= starY + size) {

                Option option = element.getOption();
                FavoriteManager.toggleSodiumOption(option.getName().getString());
            }
        }
    }
}