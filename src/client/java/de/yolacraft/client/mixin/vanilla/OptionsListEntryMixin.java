package de.yolacraft.client.mixin.vanilla;

import de.yolacraft.client.FavoriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.OptionsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net.minecraft.client.gui.components.OptionsList$Entry")
public class OptionsListEntryMixin {

    @Shadow
    private List<OptionsList.OptionInstanceWidget> children;

    @Inject(method = "extractContent", at = @At("HEAD"))
    private void shrinkButtons(
            GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            boolean hovered, float a, CallbackInfo ci) {

        for (OptionsList.OptionInstanceWidget widget : children) {
            AbstractWidget w = widget.widget();
            if (w.getWidth() == 310) {
                w.setWidth(300);
            } else if (w.getWidth() == 150) {
                w.setWidth(140);
            }
        }
    }

    @Inject(method = "extractContent", at = @At("TAIL"))
    private void renderStarAndRegister(
            GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            boolean hovered, float a, CallbackInfo ci) {

        Minecraft mc = Minecraft.getInstance();

        for (OptionsList.OptionInstanceWidget widget : children) {
            OptionInstance<?> option = widget.optionInstance();
            if (option == null) continue;

            AbstractWidget w = widget.widget();
            FavoriteManager.registerWidget(w, option);

            int starX = w.getX() + w.getWidth() + 4;
            int starY = w.getY() + 6;


            boolean fav = FavoriteManager.isFavorite(option) | FavoriteManager.isSodiumFavorite(option);

            graphics.text(
                    mc.font,
                    fav ? "★" : "☆",
                    starX, starY,
                    fav ? 0xFFFFD700 : 0xFFFFFFFF,
                    true
            );
        }
    }
}