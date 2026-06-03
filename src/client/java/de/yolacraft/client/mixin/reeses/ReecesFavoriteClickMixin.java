package de.yolacraft.client.mixin.reeses;

import de.yolacraft.client.ReecesFavoriteClickHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
public interface ReecesFavoriteClickMixin {

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    default void onMouseClicked(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (FabricLoader.getInstance().isModLoaded("reeses-sodium-options")) {
            ReecesFavoriteClickHelper.handleMouseClicked((ContainerEventHandler) this, event, cir);
        }
    }
}