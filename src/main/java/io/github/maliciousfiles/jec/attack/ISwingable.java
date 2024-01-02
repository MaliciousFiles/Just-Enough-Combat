package io.github.maliciousfiles.jec.attack;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public interface ISwingable {
    SwingType jec$getSwingType();
    float jec$getSwingDuration();

    enum SwingType {
        SLASH(true) {
            @Override
            protected void charge(ModelPart arm, float progress) {
                arm.zRot = Mth.lerp(progress, 0, 1.3707963268f); // PI/2 - 0.2
                arm.xRot = Mth.lerp(progress, 0, -2.156194525f); // 0.2 - 3*PI/4
            }

            @Override
            protected void swing(ModelPart arm, float progress) {
                arm.zRot = 1.3707963268f; // PI/2 - 0.2
                arm.xRot = Mth.lerp(progress, -2.156194525f, 0.2f); // 0.2 - 3*PI/4
            }

            @Override
            public void makeParticle(LivingEntity entity, ServerLevel level) {
                double d0 = -Mth.sin(entity.getYRot() * Mth.DEG_TO_RAD);
                double d1 = Mth.cos(entity.getYRot() * Mth.DEG_TO_RAD);
                level.sendParticles(ParticleTypes.SWEEP_ATTACK, entity.getX() + d0, entity.getY(0.5), entity.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            }
        },
        STAB(true) {
            @Override
            protected void charge(ModelPart arm, float progress) {
                arm.xRot = Mth.lerp(progress, 0, 0.5235987756f); // PI/6
            }

            @Override
            protected void swing(ModelPart arm, float progress) {
                arm.xRot = Mth.lerp(progress, 0.5235987756f, -1.0471975512f); // PI/6, -PI/3
            }

            // TODO: make particle
        },
        OVERHEAD(true) {
            @Override
            protected void charge(ModelPart arm, float progress) {
                arm.xRot = Mth.lerp(progress, 0, -3.0415926536f); // 0.1 - PI
            }

            @Override
            protected void swing(ModelPart arm, float progress) {
                arm.xRot = Mth.lerp(progress, -3.0415926536f, -1.2707963268f); // 0.1 - PI, 0.3 - PI/2
            }

            @Override
            public void makeParticle(LivingEntity entity, ServerLevel level) {
                double d0 = -Mth.sin(entity.getYRot() * Mth.DEG_TO_RAD);
                double d1 = Mth.cos(entity.getYRot() * Mth.DEG_TO_RAD);
                level.sendParticles(ParticleTypes.SWEEP_ATTACK, entity.getX(), entity.getY(0.5)-d1, entity.getZ(), 0, 0.0, d0+d1, 0.0, 0.0);
            }
        },
        PUNCH(false);

        public static final float CHARGE_TIME = 3/4f;

        private final boolean overrideModel;

        SwingType(boolean overrideModel) {
            this.overrideModel = overrideModel;
        }

        // TODO: atm these are just lerping between two values (with x and/or z), abstract to constructor?
        protected void charge(ModelPart arm, float progress) {}
        protected void swing(ModelPart arm, float progress) {}

        public final boolean swingModel(ModelPart arm, float progress) {
            if (progress < CHARGE_TIME) {
                charge(arm, progress / CHARGE_TIME);
            } else {
                swing(arm, (progress-CHARGE_TIME) / (1-CHARGE_TIME));
            }
            return overrideModel;
        }

        // TODO: frankly should just custom render on client rather than trying to use a sweeping edge particle
        public void makeParticle(LivingEntity entity, ServerLevel level) {}
    }
}
