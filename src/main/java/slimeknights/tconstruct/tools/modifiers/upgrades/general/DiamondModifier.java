package slimeknights.tconstruct.tools.modifiers.upgrades.general;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import slimeknights.tconstruct.library.modifiers.SingleLevelModifier;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.HarvestLevels;

import static slimeknights.tconstruct.common.TinkerTags.Items.ARMOR;
import static slimeknights.tconstruct.common.TinkerTags.Items.DURABILITY;
import static slimeknights.tconstruct.common.TinkerTags.Items.HARVEST;
import static slimeknights.tconstruct.common.TinkerTags.Items.MELEE_OR_UNARMED;

public class DiamondModifier extends SingleLevelModifier {
  public DiamondModifier() {
    super(0x8cf4e2);
  }

  @Override
  public void addVolatileData(ToolRebuildContext context, int level, ModDataNBT volatileData) {
    IModifiable.setRarity(volatileData, Rarity.UNCOMMON);
  }

  @Override
  public void addToolStats(ToolRebuildContext context, int level, ModifierStatsBuilder builder) {
    Item item = context.getItem();
    if (item.is(DURABILITY)) {
      ToolStats.DURABILITY.add(builder, level * 500);
    }
    if (item.is(MELEE_OR_UNARMED)) {
      ToolStats.ATTACK_DAMAGE.add(builder, level * 1f);
    }
    if (item.is(HARVEST)) {
      ToolStats.MINING_SPEED.add(builder, level * 1f);
      ToolStats.HARVEST_LEVEL.set(builder, HarvestLevels.DIAMOND);
    }
    if (item.is(ARMOR)) {
      ToolStats.ARMOR.add(builder, level);
    }
  }
}
