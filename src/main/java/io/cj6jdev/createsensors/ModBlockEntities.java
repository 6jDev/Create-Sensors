package io.cj6jdev.createsensors;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CreateSensors.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ImpactSensorBlockEntity>> IMPACT_SENSOR_BE =
            BLOCK_ENTITIES.register("impact_sensor_be", () ->
                    BlockEntityType.Builder.of(ImpactSensorBlockEntity::new,
                            ModBlocks.IMPACT_SENSOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ImpactFuzeBlockEntity>> IMPACT_FUZE_BE =
            BLOCK_ENTITIES.register("impact_fuze_be", () ->
                    BlockEntityType.Builder.of(ImpactFuzeBlockEntity::new,
                            ModBlocks.IMPACT_FUZE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ProximitySensorBlockEntity>> PROXIMITY_SENSOR_BE =
            BLOCK_ENTITIES.register("proximity_sensor_be", () ->
                    BlockEntityType.Builder.of(ProximitySensorBlockEntity::new,
                            ModBlocks.PROXIMITY_SENSOR.get()).build(null));
}