package de.yolacraft.client.mixin.sodium;

import de.yolacraft.client.FavoriteManager;
import net.caffeinemc.mods.sodium.client.gui.widgets.OptionListWidget;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionListWidget.class)
public class SodiumOptionStarMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderStars(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta,
            CallbackInfo ci
    ) {
        OptionListWidget list = (OptionListWidget)(Object)this;

        Minecraft mc = Minecraft.getInstance();

        for (Object obj : list.getControls()) {

            System.out.println(obj);

            if (!(obj instanceof ControlElement element))
                continue;

            if(element.getY() < 20)
                continue;

            if(!FavoriteManager.isAvailable(element.getOption().getName().getString()))
                continue;

            int x = element.getX() - 6;
            int y = element.getY() + (element.getHeight() / 2) - 4;


            boolean fav = FavoriteManager.getSodiumElments().contains(element.getOption().getName().getString());
            graphics.text(
                    mc.font,
                    "★",
                    x,
                    y,
                    fav ? 0xFFFFD700 : 0xFFFFFFFF,
                    true
            );
        }
    }
}