package de.yolacraft.client.gui;

import de.yolacraft.client.FavoriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class QuickSettingsScreen extends OptionsSubScreen {

    public QuickSettingsScreen(Screen lastScreen) {
        super(new TitleScreen(), Minecraft.getInstance().options, Component.literal("QuickSettings"));
    }

    public QuickSettingsScreen(Screen lastScreen, net.minecraft.client.Minecraft minecraft) {
        super(lastScreen, minecraft.options, Component.literal("QuickSettings"));
    }

    @Override
    protected void addOptions() {
        List<OptionInstance<?>> favorites = List.copyOf(FavoriteManager.getFavorites());
        if (favorites.isEmpty()) return;

        this.list.addSmall(favorites.toArray(new OptionInstance[0]));
    }
}