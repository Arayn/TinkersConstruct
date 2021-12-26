package slimeknights.tconstruct.tools.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import slimeknights.mantle.loot.builder.GenericLootModifierBuilder;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;

import javax.annotation.Nonnull;
import java.util.List;

/** Global loot modifier for modifiers */
public class ModifierLootModifier extends LootModifier {
  protected ModifierLootModifier(ILootCondition[] conditionsIn) {
    super(conditionsIn);
  }

  /** Creates a builder for datagen */
  public static GenericLootModifierBuilder<ModifierLootModifier> builder() {
    return GenericLootModifierBuilder.builder(TinkerModifiers.modifierLootModifier.get(), ModifierLootModifier::new);
  }

  @Nonnull
  @Override
  protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
    // tool is for harvest
    ItemStack stack = context.getParamOrNull(LootParameters.TOOL);
    // if null, try entity held item
    if (stack == null) {
      Entity entity = context.getParamOrNull(LootParameters.KILLER_ENTITY);
      if (entity instanceof LivingEntity) {
        LivingEntity living = (LivingEntity) entity;
        stack = living.getItemBySlot(ModifierLootingHandler.getLootingSlot(living));
      }
    }
    // hopefully one of the two worked
    if (stack != null) {
      ToolStack tool = ToolStack.from(stack);
      if (!tool.isBroken()) {
        for (ModifierEntry entry : tool.getModifierList()) {
          generatedLoot = entry.getModifier().processLoot(tool, entry.getLevel(), generatedLoot, context);
        }
      }
    }
    return generatedLoot;
  }

  public static class Serializer extends GlobalLootModifierSerializer<ModifierLootModifier> {
    @Override
    public ModifierLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
      return new ModifierLootModifier(conditions);
    }

    @Override
    public JsonObject write(ModifierLootModifier instance) {
      return makeConditions(instance.conditions);
    }
  }
}
