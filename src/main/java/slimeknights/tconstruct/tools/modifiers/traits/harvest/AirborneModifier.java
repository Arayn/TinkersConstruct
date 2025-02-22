package slimeknights.tconstruct.tools.modifiers.traits.harvest;

import net.minecraft.core.Direction;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import slimeknights.tconstruct.library.modifiers.impl.SingleUseModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class AirborneModifier extends SingleUseModifier {
  @Override
  public int getPriority() {
    return 75; // runs after other modifiers
  }

  @Override
  public void onBreakSpeed(IToolStackView tool, int level, BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
    // the speed is reduced when not on the ground, cancel out
    if (!event.getEntity().isOnGround()) {
      event.setNewSpeed(event.getNewSpeed() * 5);
    }
  }
}
