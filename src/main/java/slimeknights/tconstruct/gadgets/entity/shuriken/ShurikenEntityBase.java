package slimeknights.tconstruct.gadgets.entity.shuriken;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public abstract class ShurikenEntityBase extends ProjectileItemEntity implements IEntityAdditionalSpawnData {

  public ShurikenEntityBase(EntityType<? extends ShurikenEntityBase> type, World worldIn) {
    super(type, worldIn);
  }

  public ShurikenEntityBase(EntityType<? extends ShurikenEntityBase> type, double x, double y, double z, World worldIn) {
    super(type, x, y, z, worldIn);
  }

  public ShurikenEntityBase(EntityType<? extends ShurikenEntityBase> type, LivingEntity livingEntityIn, World worldIn) {
    super(type, livingEntityIn, worldIn);
  }

    /**
   * Get damage dealt by Shuriken
   * Should be <= 20.0F
   * @return float damage
   */
  public abstract float getDamage();

  /**
   * Get knockback dealt by Shuriken
   * Should be <= 1.0F, Minecraft
   * typically uses values from 0.2F-0.6F
   * @return float knockback
   */
  public abstract float getKnockback();

  @Override
  protected void onHit(RayTraceResult result) {
    super.onHit(result);

    if (!this.level.isClientSide) {
      this.level.broadcastEntityEvent(this, (byte) 3);
      this.remove();
    }
  }

  @Override
  protected void onHitBlock(BlockRayTraceResult result) {
    super.onHitBlock(result);

    this.spawnAtLocation(getDefaultItem());
  }

  @Override
  protected void onHitEntity(EntityRayTraceResult result) {
    Entity entity = result.getEntity();
    entity.hurt(DamageSource.thrown(this, this.getOwner()), this.getDamage());

    if (!level.isClientSide() && entity instanceof LivingEntity) {
      Vector3d motion = this.getDeltaMovement().normalize();
      ((LivingEntity) entity).knockback(this.getKnockback(), -motion.x, -motion.z);
    }
  }

  @Override
  public void writeSpawnData(PacketBuffer buffer) {
    buffer.writeItem(this.getItemRaw());
  }

  @Override
  public void readSpawnData(PacketBuffer additionalData) {
    this.setItem(additionalData.readItem());
  }

  @Nonnull
  @Override
  public IPacket<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
