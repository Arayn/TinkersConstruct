package slimeknights.tconstruct.smeltery.tileentity;

import net.minecraft.item.ItemStack;

import java.util.Arrays;

import slimeknights.mantle.tileentity.TileInventory;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

/** Represents a structure that has an inventory where it heats its items. Like a smeltery. */
public abstract class TileHeatingStructure extends TileInventory {

  protected int fuel; // Ticks left until the current fuel is depleted and fuel is taken from the tanks. Depletes every tick
  protected int temperature; // internal temperature of the smeltery == speed of the smeltery
  protected boolean needsFuel; // If the last tick executed an operation that required fuel.

  protected int[] itemTemperatures; // current temperature of each item in the corresponding slot
  protected int[] itemTempRequired; // Temperature where the items want to goooooo

  public TileHeatingStructure(String name, int inventorySize, int maxStackSize) {
    super(name, inventorySize, maxStackSize);

    itemTemperatures = new int[0];
    itemTempRequired = new int[0];
  }

  @Override
  public void resize(int size) {
    super.resize(size);
    this.itemTemperatures = Arrays.copyOf(itemTemperatures, size);
    this.itemTempRequired = Arrays.copyOf(itemTempRequired, size);
  }

  protected void heatItems() {
    for(int i = 0; i < getSizeInventory(); i++) {
      ItemStack stack = getStackInSlot(i);
      if(stack != null) {
        // already has a temperature?
        if(itemTempRequired[i] <= 0) {
          // find out the required temperature
          MeltingRecipe melting = TinkerRegistry.getMelting(stack);
          if(melting != null) {
            itemTempRequired[i] = melting.output.getFluid().getTemperature(melting.output);
            // not hot enough. error. error. error. :(
            if(itemTempRequired[i] > temperature) {
              itemTempRequired[i] = -1;
            }
          }
        }

        // heat item if possible
        if(itemTempRequired[i] > 0) {
          // fuel is present, turn up the heat
          if(fuel > 0) {
            itemTemperatures[i] += heatSlot(i);

            if(itemTemperatures[i] >= itemTempRequired[i]) {
              if(onItemFinishedHeating(stack, i)) {
                itemTemperatures[i] = 0;
                itemTempRequired[i] = 0;
              }
            }
          }
          else {
            // can't heat. no fuel. abort and try to get fuel for next tick
            this.needsFuel = true;
            return;
          }
        }
      }
    }
  }

  protected int heatSlot(int i) {
    return 1 + temperature/50;
  }


  /**
   * Called when an item finished heating up.
   * Return true if the processing was successful, then the heating data will be cleared.
   */
  protected abstract boolean onItemFinishedHeating(ItemStack stack, int slot);

  /**
   * Consume fuel if possible and increase the fuel-value accordingly
   */
  protected abstract void consumeFuel();

  protected void addFuel(int fuel, int newTemperature) {
    this.fuel += fuel;
    this.needsFuel = false;
    this.temperature = newTemperature;
  }

  public boolean hasFuel() {
    return fuel > 0;
  }
}
