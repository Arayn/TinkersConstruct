package slimeknights.tconstruct.tools.modifiers.slotless;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.tools.capability.TinkerDataKeys;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;

import java.util.Arrays;
import java.util.Comparator;

public class NearsightedModifier extends IncrementalModifier {
  private final ResourceLocation[] SLOT_KEYS = Arrays.stream(EquipmentSlotType.values())
                                                     .sorted(Comparator.comparing(EquipmentSlotType::getFilterFlag))
                                                     .map(slot -> TConstruct.getResource("nearsighted_" + slot.getName()))
                                                     .toArray(ResourceLocation[]::new);
  public NearsightedModifier() {
    super(0x796571);
  }

  @Override
  public void onEquip(IModifierToolStack tool, int level, EquipmentChangeContext context) {
    if (!tool.isBroken()) {
      ResourceLocation key = SLOT_KEYS[context.getChangedSlot().getFilterFlag()];
      context.getTinkerData().ifPresent(data -> data.computeIfAbsent(TinkerDataKeys.FOV_MODIFIER).set(key, 1 + 0.05f * level));
    }
  }

  @Override
  public void onUnequip(IModifierToolStack tool, int level, EquipmentChangeContext context) {
    if (!tool.isBroken()) {
      ResourceLocation key = SLOT_KEYS[context.getChangedSlot().getFilterFlag()];
      context.getTinkerData().ifPresent(data -> data.computeIfAbsent(TinkerDataKeys.FOV_MODIFIER).remove(key));
    }
  }
}
