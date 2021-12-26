package slimeknights.tconstruct.tools.modifiers.upgrades.melee;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;

import javax.annotation.Nullable;
import java.util.List;

/** Shared logic for all modifiers that boost damage against a creature type */
public class ScaledTypeDamageModifier extends IncrementalModifier {
  private final CreatureAttribute type;
  public ScaledTypeDamageModifier(int color, CreatureAttribute type) {
    super(color);
    this.type = type;
  }

  /**
   * Method to check if this modifier is effective on the given entity
   * @param target  Entity
   * @return  True if effective
   */
  protected boolean isEffective(LivingEntity target) {
    return target.getMobType() == type;
  }

  @Override
  public float getEntityDamage(IModifierToolStack tool, int level, ToolAttackContext context, float baseDamage, float damage) {
    LivingEntity target = context.getLivingTarget();
    if (target != null && isEffective(target)) {
      damage += getScaledLevel(tool, level) * 2.5f * tool.getModifier(ToolStats.ATTACK_DAMAGE);
    }
    return damage;
  }

  @Override
  public void addInformation(IModifierToolStack tool, int level, @Nullable PlayerEntity player, List<ITextComponent> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
    addDamageTooltip(tool, level, 2.5f, tooltip);
  }
}
