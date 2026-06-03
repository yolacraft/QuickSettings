package de.yolacraft.client.mixin.gui;

import de.yolacraft.client.gui.QuickSettingsScreen;
import de.yolacraft.client.mixin.accessor.ScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.components.SpriteIconButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void addQuickSettingsButton(CallbackInfo ci) {
        TitleScreen self = (TitleScreen)(Object) this;

        int topPos = self.height / 4 + 48;

        SpriteIconButton button = SpriteIconButton.builder(
                        Component.translatable("menu.quicksettings"),
                        btn -> Minecraft.getInstance().setScreen(new QuickSettingsScreen(self, Minecraft.getInstance())),
                        true
                )
                .sprite(Identifier.fromNamespaceAndPath("quicksettings", "icon"), 16, 16)
                .width(20)
                .build();

        button.setPosition(self.width / 2 + 104, topPos);

        ((ScreenAccessor) self).invokeAddRenderableWidget(button);
    }
}