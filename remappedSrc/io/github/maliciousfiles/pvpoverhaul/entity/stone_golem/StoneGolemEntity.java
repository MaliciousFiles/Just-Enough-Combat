package io.github.maliciousfiles.pvpoverhaul.entity.stone_golem;

import EntityNavigation;
import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.entity.CustomEntityStatuses;
import net.minecraft.block.Material;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class StoneGolemEntity extends HostileEntity {

    private static final int INACTIVE_DURATION = 100;
    private static final int WAKEUP_DURATION = 60;

    public final AnimationState sleepingAnimationState = new AnimationState();

    private int inactiveTicks = 0;
    private HibernateGoal hibernateGoal;
    private FindStoneGoal findStoneGoal;
    protected boolean animationReverse;

    public StoneGolemEntity(EntityType<? extends StoneGolemEntity> entityType, World world) {
        super(entityType, world);

        this.stepHeight = 1f;
    }

    public static DefaultAttributeContainer.Builder createStoneGolemAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10).add(EntityAttributes.GENERIC_ARMOR, 10.0).add(EntityAttributes.GENERIC_MAX_HEALTH, 40).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 5);
    }

    protected void initGoals() {
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, false) {
            @Override
            protected void findClosestTarget() {
                this.targetPredicate.setBaseMaxDistance(getFollowRange());
                super.findClosestTarget();
            }
        });

        this.hibernateGoal = new HibernateGoal(this);
        this.findStoneGoal = new FindStoneGoal(this, 1.5);
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, hibernateGoal);
        this.goalSelector.add(3, findStoneGoal);
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1, false) {
            @Override
            protected double getSquaredMaxAttackDistance(LivingEntity entity) {
                return 4 + entity.getWidth();
            }
        });
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));

    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new MobNavigation(this, world) {
            @Override
            protected void continueFollowingPath() {
                if (this.currentPath == null) return;

                if (!findStoneGoal.isPathFinding() || this.currentPath.getCurrentNodeIndex() < this.currentPath.getLength()-1) {
                    super.continueFollowingPath();
                } else {
                    currentPath.setNode(currentPath.getCurrentNodeIndex(), currentPath.getNode(currentPath.getCurrentNodeIndex()).copyWithNewPosition(getTargetPos().getX(), getTargetPos().getY(), getTargetPos().getZ()));

                    Vec3d targetPos = new Vec3d(getTargetPos().getX()+0.5, getTargetPos().getY(), getTargetPos().getZ()+0.5);
                    if (entity.getPos().distanceTo(targetPos) < 2.2) {
                        entity.move(MovementType.SELF, targetPos.subtract(entity.getPos()).normalize());
                        entity.setPosition(targetPos);
                    }
                }
            }
        };
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!world.isClient) this.setDormant(false);

        return super.damage(source, amount);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("Dormant", isDormant());
        nbt.putInt("inactiveTicks", getInactiveTicks());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.setDormant(nbt.getBoolean("Dormant"));
        this.inactiveTicks = nbt.getInt("inactiveTicks");
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);

        if (status == CustomEntityStatuses.SLEEP_STATUS) {
            if (world.isClient) {
                int age = this.age - (int) Math.ceil(Math.max(sleepingAnimationState.getTimeRunning() / 1000f * 20, 0));
                sleepingAnimationState.start(age);
                sleepingAnimationState.update(this.age, 1);

                animationReverse = false;
            }
        } else if (status == CustomEntityStatuses.WAKEUP_STATUS) {
            if (world.isClient) {
                int age = this.age - (int) Math.ceil(Math.min(sleepingAnimationState.getTimeRunning() / 1000f * 20, StoneGolemEntityModel.SLEEP_ANIMATION.lengthInSeconds() * 20));
                sleepingAnimationState.start(age);
                sleepingAnimationState.update(this.age, 1);

                animationReverse = true;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isClient) {
            if (isDormant()) {
                snapToGrid();
            } else {
                if (getTarget() != null) {
                    inactiveTicks = 0;
                } else {
                    inactiveTicks++;
                }
            }
        }
    }

    @Override
    public void setVelocity(Vec3d velocity) {
        if (!world.isClient) {
            if (!isDormant()) super.setVelocity(velocity);
            else super.setVelocity(new Vec3d(0, velocity.getY(), 0));
        }
    }

    public boolean isDormant() {
        return this.hibernateGoal.isActive();
    }

    public int getInactiveTicks() {
        return inactiveTicks;
    }

    private void snapToGrid() {
        float maxDegrees = 1.25f;

        float yaw = this.getBodyYaw();
        float targetYaw = Math.round(yaw / 90) * 90;
        float diff = MathHelper.subtractAngles(targetYaw, yaw);

        this.setBodyYaw(MathHelper.clamp(diff, -maxDegrees, maxDegrees));

        this.setPosition(this.getBlockX()+0.5, this.getBlockY(), this.getBlockZ()+0.5);
    }

    public void setDormant(boolean dormant) {
        if (dormant) {
            this.hibernateGoal.start();
        } else {
            this.hibernateGoal.stop();
        }
    }

    private boolean isEligiblePos(WorldView world, BlockPos pos) {
        if (world.getBlockState(pos).getMaterial() != Material.STONE) return false;

        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
                BlockPos pos2 = pos.add(i, 0, k);
                if (!world.getBlockState(pos2).isOpaque() || !world.getBlockState(pos2).isSolidBlock(world, pos2) || (i != 0 && k != 0 && !world.isAir(pos2.up())) || !world.isAir(pos2.up(2))) return false;
            }
        }

        Path path = this.getNavigation().findPathTo(pos, 1);
        return path != null && path.reachesTarget();
    }

    private static class FindStoneGoal extends MoveToTargetPosGoal {
        private final StoneGolemEntity golem;
        private boolean active = false;

        public FindStoneGoal(StoneGolemEntity golem, double speed) {
            super(golem, speed, 20, 5);
            this.golem = golem;
        }

        public boolean canStart() {
            return golem.getInactiveTicks() > 100 && golemEligible() && super.canStart();
        }

        private boolean golemEligible() {
            return this.golem.getTarget() == null;
        }

        public boolean shouldContinue() {
            return golemEligible() && super.shouldContinue();
        }

        @Override
        public void start() {
            super.start();

            this.active = true;
        }

        @Override
        public void stop() {
            super.stop();

            this.active = false;
        }

        public void tick() {
            super.tick();

            PvPOverhaul.LOGGER.info("tick");

            if (hasReached()) {
                this.golem.setDormant(true);
            }
        }

        @Override
        public double getDesiredDistanceToTarget() {
            return 1;
        }

        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            return golem.isEligiblePos(world, pos);
        }

        public boolean isPathFinding() {
            return isActive() && !hasReached();
        }

        public boolean isActive() {
            return this.active;
        }

        @Override
        protected int getInterval(PathAwareEntity mob) {
            return golem.isDormant() ? 0 : super.getInterval(mob);
        }
    }

    private static class HibernateGoal extends Goal {

        private final StoneGolemEntity golem;
        private boolean active = false;
        private int wakeUpCountdown = 0;
        private boolean waking = false;

        public HibernateGoal(StoneGolemEntity golem) {
            this.golem = golem;

            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
        }

        @Override
        public boolean canStart() {
            return golem.getTarget() == null && golem.isEligiblePos(golem.world, golem.getBlockPos().down()) && golem.getInactiveTicks() > INACTIVE_DURATION;
        }

        @Override
        public boolean shouldContinue() {
            if (waking) {
                PvPOverhaul.LOGGER.info(wakeUpCountdown+"");
            }
            return !waking || wakeUpCountdown != 0;
        }

        @Override
        public void tick() {
//            golem.setHeadYaw(0);
//            golem.setPitch(0);

            if (!canStart()) {
                if (!waking) {
                    wakeUpCountdown = WAKEUP_DURATION / 2;
                    waking = true;

                    golem.world.sendEntityStatus(golem, CustomEntityStatuses.WAKEUP_STATUS);
                }

                --wakeUpCountdown;
            } else {
                if (waking) golem.world.sendEntityStatus(golem, CustomEntityStatuses.SLEEP_STATUS);

                wakeUpCountdown = 0;
                waking = false;
            }
        }

        @Override
        public void start() {
            super.start();
            this.active = true;

            golem.world.sendEntityStatus(golem, CustomEntityStatuses.SLEEP_STATUS);

            golem.getNavigation().stop();
            golem.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(12.5);
        }

        @Override
        public void stop() {
            super.stop();
            this.active = false;
            this.waking = false;
            golem.inactiveTicks = 0;
            this.wakeUpCountdown = 0;

            golem.world.sendEntityStatus(golem, CustomEntityStatuses.WAKEUP_STATUS);

            golem.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(35);
        }

        public boolean isActive() {
            return this.active;
        }
    }
}
