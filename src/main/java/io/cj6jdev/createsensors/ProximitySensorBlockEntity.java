package io.cj6jdev.createsensors;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ProximitySensorBlockEntity extends SmartBlockEntity {

    public ScrollValueBehaviour detectionRadius;

    public ProximitySensorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PROXIMITY_SENSOR_BE.get(), pos, state);
    }

    public static class RadiusSlot extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return new Vec3(0.5, 0.5, 1.0);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction != state.getValue(ImpactSensorBlock.FACING).getOpposite();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        detectionRadius = new ScrollValueBehaviour(
                Component.literal("Detection Radius"),
                this,
                new RadiusSlot()
        );
        detectionRadius.between(1, 32);
        detectionRadius.setValue(8);
        detectionRadius.withFormatter(v -> v + " blocks");
        behaviours.add(detectionRadius);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ProximitySensorBlockEntity be) {
        if (level.isClientSide()) return;

        be.tick();

        SubLevelAccess ownSubLevel = SableCompanion.INSTANCE.getContaining(level, pos);

        Vec3 worldPos;
        if (ownSubLevel != null) {
            worldPos = ownSubLevel.logicalPose().transformPosition(Vec3.atCenterOf(pos));
        } else {
            worldPos = Vec3.atCenterOf(pos);
        }

        double radius = be.detectionRadius.getValue();

        BoundingBox3d searchBox = new BoundingBox3d(
                worldPos.x - radius, worldPos.y - radius, worldPos.z - radius,
                worldPos.x + radius, worldPos.y + radius, worldPos.z + radius
        );

        Iterable<? extends SubLevelAccess> nearby = SableCompanion.INSTANCE.getAllIntersecting(level, searchBox);

        for (SubLevelAccess other : nearby) {
            if (ownSubLevel != null && other == ownSubLevel) continue;

            if (!state.getValue(ProximitySensorBlock.POWERED)) {
                level.setBlock(pos, state.setValue(ProximitySensorBlock.POWERED, true), 3);
                level.updateNeighborsAt(pos, state.getBlock());
            }
            return;
        }

        if (state.getValue(ProximitySensorBlock.POWERED)) {
            level.setBlock(pos, state.setValue(ProximitySensorBlock.POWERED, false), 3);
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }
}