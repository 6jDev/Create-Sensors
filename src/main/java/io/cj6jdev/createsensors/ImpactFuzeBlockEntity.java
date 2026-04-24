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

public class ImpactFuzeBlockEntity extends SmartBlockEntity {

    public ScrollValueBehaviour threshold;

    public ImpactFuzeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IMPACT_FUZE_BE.get(), pos, state);
    }

    public static class ThresholdSlot extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return new Vec3(0.5, 0.5, 1.0);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return true;
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
        threshold.setValue(5);
        threshold.withFormatter(v -> v + " m/t²");
        behaviours.add(threshold);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ImpactFuzeBlockEntity be) {
        if (level.isClientSide()) return;

        be.tick();

        SubLevelAccess subLevel = SableCompanion.INSTANCE.getContaining(level, pos);
        if (subLevel == null) {
            be.lastPos = null;
            be.lastVelocity = Vec3.ZERO;
            return;
        }

        Vec3 currentPos = subLevel.logicalPose().transformPosition(Vec3.atCenterOf(pos));

        if (be.lastPos == null) {
            be.lastPos = currentPos;
            return;
        }

        Vec3 currentVelocity = currentPos.subtract(be.lastPos);
        double decel = be.lastVelocity.length() - currentVelocity.length();

        double thresholdValue = be.threshold.getValue() / 10.0;

        if (decel >= thresholdValue) {
            level.explode(
                    null,
                    currentPos.x,
                    currentPos.y,
                    currentPos.z,
                    6.0f,
                    Level.ExplosionInteraction.TNT
            );
        }

        be.lastPos = currentPos;
        be.lastVelocity = currentVelocity;
    }

    private Vec3 lastPos = null;
    private Vec3 lastVelocity = Vec3.ZERO;
}