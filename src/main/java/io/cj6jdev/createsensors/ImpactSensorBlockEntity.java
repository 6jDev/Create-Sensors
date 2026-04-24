package io.cj6jdev.createsensors;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class ImpactSensorBlockEntity extends SmartBlockEntity {

    public ScrollValueBehaviour threshold;

    public ImpactSensorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IMPACT_SENSOR_BE.get(), pos, state);
    }

    public static class ThresholdSlot extends ValueBoxTransform.Sided {
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
        threshold = new ScrollValueBehaviour(
                Component.literal("Deceleration Threshold"),
                this,
                new ThresholdSlot()
        );
        threshold.between(1, 20);
        threshold.setValue(5); // default = 0.5 (stored as int, divide by 10)
        threshold.withFormatter(v -> v + " m/t²");
        behaviours.add(threshold);
    }



    public static void tick(Level level, BlockPos pos, BlockState state, ImpactSensorBlockEntity be) {
        if (level.isClientSide()) return;

        be.tick();

        SubLevelAccess ownSubLevel = SableCompanion.INSTANCE.getContaining(level, pos);

        Vec3 worldPos;
        if (ownSubLevel != null) {
            worldPos = ownSubLevel.logicalPose().transformPosition(Vec3.atCenterOf(pos));
        } else {
            be.lastPos = null;
            be.lastVelocity = Vec3.ZERO;
            return;
        }

        if (be.lastPos == null) {
            be.lastPos = worldPos;
            return;
        }

        Vec3 currentVelocity = worldPos.subtract(be.lastPos);
        double decel = be.lastVelocity.length() - currentVelocity.length();

        double thresholdValue = be.threshold.getValue() / 10.0;

        if (decel >= thresholdValue) {
            level.setBlock(pos, state.setValue(ImpactSensorBlock.POWERED, true), 3);
            level.scheduleTick(pos, state.getBlock(), 10);
        }

        be.lastPos = worldPos;
        be.lastVelocity = currentVelocity;
    }

    private Vec3 lastPos = null;
    private Vec3 lastVelocity = Vec3.ZERO;
}