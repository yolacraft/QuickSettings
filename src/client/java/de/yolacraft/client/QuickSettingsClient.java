package de.yolacraft.client;

import de.yolacraft.client.gui.QuickSettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class QuickSettingsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		FavoriteManager.load();

		ModKeybinds.register();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (ModKeybinds.OPEN_QUICK_SETTINGS.consumeClick()) {
				if (client.screen == null) {
					client.setScreen(new QuickSettingsScreen(null, client));
				}
			}
		});
	}
}