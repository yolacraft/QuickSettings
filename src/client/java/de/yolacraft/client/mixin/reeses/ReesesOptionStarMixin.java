package de.yolacraft.client.mixin.reeses;

import de.yolacraft.client.FavoriteManager;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.AbstractFrame;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = AbstractFrame.class, remap = false)
public abstract class ReesesOptionStarMixin {

    @Shadow protected List<Renderable> children;

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderStars(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta,
            CallbackInfo ci
    ) {
        AbstractFrame frame = (AbstractFrame) (Object) this;
        Minecraft mc = Minecraft.getInstance();

        int frameMinY = frame.getY();
        int frameMaxY = frame.getLimitY();

        for (Object obj : this.children) {
            if (!(obj instanceof ControlElement element)) {
                continue;
            }

            int elementY = element.getY();
            int elementHeight = element.getHeight();

            if (elementY + elementHeight < frameMinY || elementY > frameMaxY) {
                continue;
            }

            if (elementY < 20) {
                continue;
            }

            String optionName = element.getOption().getName().getString();

            if (!FavoriteManager.isAvailable(optionName)) {
                continue;
            }

            int textWidth = mc.font.width(optionName);

            int x = element.getX() + 6 + textWidth + 4;
            int y = elementY + (elementHeight / 2) - 4;

            boolean fav = FavoriteManager.getSodiumElments().contains(optionName);

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