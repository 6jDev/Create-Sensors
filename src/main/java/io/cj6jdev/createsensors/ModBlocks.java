package io.cj6jdev.createsensors;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModBlocks {
    public static final DeferredBlock<ImpactSensorBlock> IMPACT_SENSOR =
            CreateSensors.BLOCKS.registerBlock("impact_sensor", props -> new ImpactSensorBlock());

    public static final DeferredItem<BlockItem> IMPACT_SENSOR_ITEM =
            CreateSensors.ITEMS.registerSimpleBlockItem("impact_sensor", IMPACT_SENSOR);

    public static final DeferredBlock<ImpactFuzeBlock> IMPACT_FUZE =
            CreateSensors.BLOCKS.registerBlock("impact_fuze", props -> new ImpactFuzeBlock());

    public static final DeferredItem<BlockItem> IMPACT_FUZE_ITEM =
            CreateSensors.ITEMS.registerSimpleBlockItem("impact_fuze", IMPACT_FUZE);

    public static final DeferredBlock<ProximitySensorBlock> PROXIMITY_SENSOR =
            CreateSensors.BLOCKS.registerBlock("proximity_sensor", props -> new ProximitySensorBlock());

    public static final DeferredItem<BlockItem> PROXIMITY_SENSOR_ITEM =
            CreateSensors.ITEMS.registerSimpleBlockItem("proximity_sensor", PROXIMITY_SENSOR);
}