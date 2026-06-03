package de.yolacraft.client.mixin.vanilla;

import de.yolacraft.client.FavoriteManager;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.gui.components.ContainerObjectSelectionList$Entry")
public class ContainerEntryClickMixin {

    @Inject(
            method = "mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onStarClicked(MouseButtonEvent event, boolean doubleClick,
                               CallbackInfoReturnable<Boolean> cir) {

        if (!this.getClass().getName().equals(
                "net.minecraft.client.gui.components.OptionsList$Entry")) return;
        if (event.button() != 0) return;

        double mouseX = event.x();
        double mouseY = event.y();

        if (!(this instanceof ContainerEventHandler container)) return;

        for (GuiEventListener listener : container.children()) {
            if (!(listener instanceof AbstractWidget w)) continue;

            int starX = w.getX() + w.getWidth() + 4;
            int starY = w.getY() + 6;
            int starW = 9;
            int starH = 9;

            if (mouseX >= starX && mouseX < starX + starW
                    && mouseY >= starY && mouseY < starY + starH) {

                OptionInstance<?> option = FavoriteManager.findOptionForWidget(w);
                if (option != null) {
                    if(FavoriteManager.isSodiumFavorite(option)){
                        FavoriteManager.toggleSodiumFavorite(option);
                    }else{
                        FavoriteManager.toggle(option);
                    }
                }

                cir.setReturnValue(true);
                return;
            }
        }
    }
}