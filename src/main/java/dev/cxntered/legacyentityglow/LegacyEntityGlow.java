package dev.cxntered.legacyentityglow;

import dev.cxntered.legacyentityglow.config.FlatJsonConfigSerializer;
import dev.cxntered.legacyentityglow.config.ModConfig;
import net.ornithemc.osl.config.api.ConfigManager;
import net.ornithemc.osl.config.api.serdes.config.ConfigSerializers;
import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class LegacyEntityGlow implements ModInitializer {
    @Override
    public void init() {
        ConfigSerializers.register(ModConfig.SERIALIZER_TYPE, new FlatJsonConfigSerializer());
        ConfigManager.register(new ModConfig());
    }
}
