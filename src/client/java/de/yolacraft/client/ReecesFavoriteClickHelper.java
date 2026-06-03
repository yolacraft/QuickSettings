package de.yolacraft.client;

import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.AbstractFrame;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ReecesFavoriteClickHelper {

    public static void handleMouseClicked(ContainerEventHandler handler, MouseButtonEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (!(handler instanceof AbstractFrame frame)) {
            return;
        }

        if (event.button() != 0) return;

        Minecraft mc = Minecraft.getInstance();
        double mouseX = event.x();
        double mouseY = event.y();

        int frameMinY = frame.getY();
        int frameMaxY = frame.getLimitY();

        for (GuiEventListener obj : frame.children()) {
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
            int starX = element.getX() + 6 + textWidth + 4;
            int starY = elementY + (elementHeight / 2) - 4;
            int size = 10;

            if (mouseX >= starX && mouseX <= starX + size && mouseY >= starY && mouseY <= starY + size) {
                FavoriteManager.toggleSodiumOption(optionName);
                cir.setReturnValue(true);
                return;
            }
        }
    }
}