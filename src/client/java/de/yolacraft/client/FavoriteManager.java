package de.yolacraft.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class FavoriteManager {

    private static final Set<String> FAVORITES = new HashSet<>();
    private static final Map<AbstractWidget, OptionInstance<?>> WIDGET_MAP = new HashMap<>();

    private static final Map<String, OptionInstance<?>> ID_TO_OPTION = new HashMap<>();
    private static boolean optionsIndexed = false;

    private static List<String> sodiumElments;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("favorites.json").toFile();
    private static final File SODIUM_FILE = FabricLoader.getInstance().getConfigDir().resolve("sodium-favorites.json").toFile();

    public static void load() {

        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                Type type = new TypeToken<HashSet<String>>() {}.getType();
                Set<String> loaded = GSON.fromJson(reader, type);
                if (loaded != null) {
                    FAVORITES.clear();
                    FAVORITES.addAll(loaded);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sodiumElments = new ArrayList<>();
        if (SODIUM_FILE.exists()) {
            try (FileReader reader = new FileReader(SODIUM_FILE)) {
                Type type = new TypeToken<List<String>>() {}.getType();
                List<String> loaded = GSON.fromJson(reader, type);
                if (loaded != null) {
                    sodiumElments.clear();
                    sodiumElments.addAll(loaded);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(FAVORITES, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSodiumElements() {

        try {
            if (!SODIUM_FILE.getParentFile().exists()) {
                SODIUM_FILE.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(SODIUM_FILE)) {
                GSON.toJson(sodiumElments, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexMinecraftOptions() {
        if (optionsIndexed) return;
        try {
            Minecraft client = Minecraft.getInstance();
            if (client != null && client.options != null) {
                Options options = client.options;
                for (Field field : Options.class.getDeclaredFields()) {
                    if (OptionInstance.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        OptionInstance<?> option = (OptionInstance<?>) field.get(options);
                        if (option != null) {
                            ID_TO_OPTION.put(option.toString(), option);
                        }
                    }
                }
                optionsIndexed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toggleSodiumOption(String id){
        if(sodiumElments.contains(id)){
            sodiumElments.remove(id);
        }else{
            sodiumElments.add(id);
        }
        saveSodiumElements();
    }

    public static void registerWidget(AbstractWidget widget, OptionInstance<?> option) {
        WIDGET_MAP.put(widget, option);
        ID_TO_OPTION.put(option.toString(), option);
    }

    public static OptionInstance<?> findOptionForWidget(AbstractWidget widget) {
        return WIDGET_MAP.get(widget);
    }

    public static boolean isFavorite(OptionInstance<?> option) {
        return FAVORITES.contains(option.toString());
    }



    public static boolean isSodiumFavorite(OptionInstance<?> option) {
        return sodiumElments.contains(getId(option));
    }

    public static void toggleSodiumFavorite(OptionInstance<?> option) {
        String id = getId(option);
        toggleSodiumOption(id);
    }

    public static String getId(OptionInstance<?> option){
        if (option == null) {
            return null;
        }

        String foundID = null;
        for (Map.Entry<String, OptionInstance<?>> entry : ID_TO_OPTION.entrySet()) {
            if (entry.getValue().equals(option)) {
                foundID = entry.getKey();
                break;
            }
        }
        return foundID;
    }

    public static void toggle(OptionInstance<?> option) {
        String id = option.toString();
        if (!FAVORITES.add(id)) {
            FAVORITES.remove(id);
        }
        save();
    }

    public static Set<OptionInstance<?>> getFavorites() {
        indexMinecraftOptions();

        Set<OptionInstance<?>> favoriteOptions = new HashSet<>();
        for (String id : FAVORITES) {
            OptionInstance<?> option = ID_TO_OPTION.get(id);
            if (option != null) {
                favoriteOptions.add(option);
            }
        }
        for (String id : sodiumElments) {
            OptionInstance<?> option = ID_TO_OPTION.get(id);
            if (option != null) {
                favoriteOptions.add(option);
            }
        }
        return favoriteOptions;
    }

    public static boolean isAvailable(String id) {
        if(ID_TO_OPTION.isEmpty()){
            indexMinecraftOptions();
        }


        OptionInstance<?> option = ID_TO_OPTION.get(id);
        if (option != null) {
            return true;
        }
        return false;
    }

    public static List<String> getSodiumElments() {
        return sodiumElments;
    }
}