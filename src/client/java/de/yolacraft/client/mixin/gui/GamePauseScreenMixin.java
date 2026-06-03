package de.yolacraft.client.mixin.gui;

import de.yolacraft.client.gui.QuickSettingsScreen;
import de.yolacraft.client.mixin.accessor.ScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class GamePauseScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void addQuickSettingsButton(CallbackInfo ci) {
        PauseScreen self = (PauseScreen)(Object) this;

        SpriteIconButton button = SpriteIconButton.builder(
                        Component.translatable("menu.quicksettings"),
                        btn -> Minecraft.getInstance().setScreen(new QuickSettingsScreen(self, Minecraft.getInstance())),
                        true
                )
                .sprite(Identifier.fromNamespaceAndPath("quicksettings", "icon"), 16, 16)
                .width(20)
                .build();



        AbstractWidget statsButton = null;
        for (GuiEventListener element : self.children()) {
            if (element instanceof AbstractWidget widget) {
                if (widget.getMessage() != null && widget.getMessage().getString().equals(Component.translatable("gui.stats").getString())) {
                    statsButton = widget;
                    break;
                }
            }
        }

        if (statsButton != null) {
            int xPos = statsButton.getX() + statsButton.getWidth() + 4;
            int yPos = statsButton.getY() + (statsButton.getHeight() - button.getHeight()) / 2;

            button.setPosition(xPos, yPos);
        } else {
            int topPos = self.height / 4 + 32;
            button.setPosition(self.width / 2 + 104, topPos);
        }
        ((ScreenAccessor) self).invokeAddRenderableWidget(button);
    }
}