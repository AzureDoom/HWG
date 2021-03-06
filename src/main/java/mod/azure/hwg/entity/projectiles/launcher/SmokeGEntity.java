package mod.azure.hwg.entity.projectiles.launcher;

import org.jetbrains.annotations.Nullable;

import mod.azure.hwg.util.packet.EntityPacket;
import mod.azure.hwg.util.registry.HWGItems;
import mod.azure.hwg.util.registry.ProjectilesEntityRegister;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SmokeGEntity extends PersistentProjectileEntity implements IAnimatable {

	protected int timeInAir;
	protected boolean inAir;
	protected String type;
	private int ticksInAir;

	public SmokeGEntity(EntityType<? extends SmokeGEntity> entityType, World world) {
		super(entityType, world);
		this.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
	}

	public SmokeGEntity(World world, LivingEntity owner) {
		super(ProjectilesEntityRegister.SMOKE_GRENADE_S, owner, world);
	}

	protected SmokeGEntity(EntityType<? extends SmokeGEntity> type, double x, double y, double z, World world) {
		this(type, world);
	}

	protected SmokeGEntity(EntityType<? extends SmokeGEntity> type, LivingEntity owner, World world) {
		this(type, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ(), world);
		this.setOwner(owner);
		if (owner instanceof PlayerEntity) {
			this.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
		}

	}

	public SmokeGEntity(World world, double x, double y, double z, ItemStack stack) {
		super(ProjectilesEntityRegister.SMOKE_GRENADE_S, world);
		this.updatePosition(x, y, z);
	}

	public SmokeGEntity(World world, @Nullable Entity entity, double x, double y, double z, ItemStack stack) {
		this(world, x, y, z, stack);
		this.setOwner(entity);
	}

	public SmokeGEntity(World world, ItemStack stack, LivingEntity shooter) {
		this(world, shooter, shooter.getX(), shooter.getY(), shooter.getZ(), stack);
	}

	public SmokeGEntity(World world, ItemStack stack, double x, double y, double z, boolean shotAtAngle) {
		this(world, x, y, z, stack);
	}

	public SmokeGEntity(World world, ItemStack stack, Entity entity, double x, double y, double z,
			boolean shotAtAngle) {
		this(world, stack, x, y, z, shotAtAngle);
		this.setOwner(entity);
	}

	private AnimationFactory factory = new AnimationFactory(this);

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("bullet", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<SmokeGEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return EntityPacket.createPacket(this);
	}

	@Override
	public void remove() {
		AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(),
				this.getZ());
		areaeffectcloudentity.setParticleType(ParticleTypes.LARGE_SMOKE);
		areaeffectcloudentity.setRadius(5.0F);
		areaeffectcloudentity.setDuration(120);
		areaeffectcloudentity.addEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 1));
		areaeffectcloudentity.updatePosition(this.getX(), this.getEyeY(), this.getZ());
		this.world.spawnEntity(areaeffectcloudentity);
		super.remove();
	}

	@Override
	public void age() {
		++this.ticksInAir;
		if (this.ticksInAir >= 80) {
			this.remove();
		}
	}

	@Override
	public void setVelocity(double x, double y, double z, float speed, float divergence) {
		super.setVelocity(x, y, z, speed, divergence);
		this.ticksInAir = 0;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putShort("life", (short) this.ticksInAir);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		this.ticksInAir = tag.getShort("life");
	}

	@Override
	public void tick() {
		super.tick();
		if (this.age >= 80) {
			this.remove();
		}

		setNoGravity(false);
		Vec3d vec3d = this.getVelocity();
		vec3d = this.getVelocity();
		this.setVelocity(vec3d.multiply((double) 0.99F));
		Vec3d vec3d5 = this.getVelocity();
		this.setVelocity(vec3d5.x, vec3d5.y - 0.05000000074505806D, vec3d5.z);
	}

	public void initFromStack(ItemStack stack) {
		if (stack.getItem() == HWGItems.G_SMOKE) {
		}
	}

	public SoundEvent hitSound = this.getHitSound();

	@Override
	public void setSound(SoundEvent soundIn) {
		this.hitSound = soundIn;
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.ENTITY_GENERIC_EXPLODE;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		if (!this.world.isClient) {
			this.remove();
		}
		this.setSound(SoundEvents.ENTITY_GENERIC_EXPLODE);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (!this.world.isClient) {
			this.remove();
		}
	}

	@Override
	public ItemStack asItemStack() {
		return new ItemStack(HWGItems.G_SMOKE);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		return true;
	}

}