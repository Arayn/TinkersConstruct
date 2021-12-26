package slimeknights.tconstruct.tools.modifiers.traits.general;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType.Group;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.shared.TinkerCommons;

import java.util.List;

public class TastyModifier extends Modifier {
  private static final ResourceLocation IS_EATING = TConstruct.getResource("eating_tasty");
  public TastyModifier() {
    super(0xF0A8A4);
  }

  @Override
  public ActionResultType onToolUse(IModifierToolStack tool, int level, World world, PlayerEntity player, Hand hand, EquipmentSlotType slotType) {
    if (slotType.getType() == Group.HAND) {
      if (!tool.isBroken() && player.canEat(false)) {
        player.startUsingItem(hand);
        // mark tool as eating as use action is only stack sensitive
        tool.getPersistentData().putBoolean(IS_EATING, true);
        return ActionResultType.CONSUME;
      } else {
        // clear is eating boolean if we cannot eat, prevents messing with other modifier's animations
        tool.getPersistentData().remove(IS_EATING);
      }
    }
    return ActionResultType.PASS;
  }

  @Override
  public boolean onStoppedUsing(IModifierToolStack tool, int level, World world, LivingEntity entity, int timeLeft) {
    tool.getPersistentData().remove(IS_EATING);
    return false;
  }

  @Override
  public boolean onFinishUsing(IModifierToolStack tool, int level, World world, LivingEntity entity) {
    // remove is eating tag to prevent from messing with other modifiers
    ModDataNBT persistentData = tool.getPersistentData();
    boolean wasEating = persistentData.getBoolean(IS_EATING);
    persistentData.remove(IS_EATING);

    if (!tool.isBroken() && wasEating && entity instanceof PlayerEntity) {
      // clear eating marker
      PlayerEntity player = (PlayerEntity) entity;
      if (player.canEat(false)) {
        // eat the food
        player.getFoodData().eat(level, level * 0.1f);
        player.awardStat(Stats.ITEM_USED.get(tool.getItem()));
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundCategory.NEUTRAL, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);

        // 15 damage for a bite per level, does not process reinforced/overslime, your teeth are tough
        if (ToolDamageUtil.directDamage(tool, 15 * level, player, player.getUseItem())) {
          player.broadcastBreakEvent(player.getUsedItemHand());
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public UseAction getUseAction(IModifierToolStack tool, int level) {
    return tool.getPersistentData().getBoolean(IS_EATING) ? UseAction.EAT : UseAction.NONE;
  }

  @Override
  public int getUseDuration(IModifierToolStack tool, int level) {
    return tool.getPersistentData().getBoolean(IS_EATING) ? 16 : 0;
  }

  @Override
  public List<ItemStack> processLoot(IModifierToolStack tool, int level, List<ItemStack> generatedLoot, LootContext context) {
    // if no damage source, probably not a mob
    // otherwise blocks breaking (where THIS_ENTITY is the player) start dropping bacon
    if (!context.hasParam(LootParameters.DAMAGE_SOURCE)) {
      return generatedLoot;
    }

    // must have an entity
    Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
    if (entity != null && TinkerTags.EntityTypes.BACON_PRODUCER.contains(entity.getType())) {
      // at tasty 1, 2, 3, and 4 its a 2%, 4.15%, 6.25%, 8% per level
      int looting = context.getLootingModifier();
      if (RANDOM.nextInt(48 / level) <= looting) {
        // bacon
        generatedLoot.add(new ItemStack(TinkerCommons.bacon));
      }
    }
    return generatedLoot;
  }
}
