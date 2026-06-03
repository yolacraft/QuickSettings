package de.yolacraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static KeyMapping OPEN_QUICK_SETTINGS;

    public static void register() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("quicksettings", "category")
        );

        OPEN_QUICK_SETTINGS = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.quicksettings.open_quick_settings",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_Y,
                        category
                )
        );
    }
}