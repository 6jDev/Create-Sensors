package io.cj6jdev.createsensors;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;

@Mod(CreateSensors.MODID)
public class CreateSensors {
    public static final String MODID = "createsensors";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MUNITIONS_TAB = CREATIVE_MODE_TABS.register("sensors_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.createsensors"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModBlocks.IMPACT_SENSOR_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.IMPACT_SENSOR_ITEM.get());
                        output.accept(ModBlocks.IMPACT_FUZE_ITEM.get());
                        output.accept(ModBlocks.PROXIMITY_SENSOR_ITEM.get());
                    })
                    .build());

    public CreateSensors(IEventBus modEventBus, ModContainer modContainer) {
        // Touch ModBlocks to force its static fields to initialize
        ModBlocks.IMPACT_SENSOR.getClass();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
    }
}