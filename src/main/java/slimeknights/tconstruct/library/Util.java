package slimeknights.tconstruct.library;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.registry.GameData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

import slimeknights.mantle.util.RecipeMatchRegistry;

public class Util {

  public static final String MODID = "TConstruct";
  public static final String RESOURCE = MODID.toLowerCase(Locale.US);

  public static Logger getLogger(String type) {
    String log = MODID;

    return LogManager.getLogger(log + "-" + type);
  }


  /**
   * Removes all whitespaces from the given string and makes it lowerspace.
   */
  public static String sanitizeLocalizationString(String string) {
    return string.toLowerCase(Locale.US).replaceAll(" ", "");
  }

  /**
   * Returns the given Resource prefixed with tinkers resource location. Use this function instead of hardcoding
   * resource locations.
   */
  public static String resource(String res) {
    return String.format("%s:%s", RESOURCE, res);
  }

  public static ResourceLocation getResource(String res) {
    return new ResourceLocation(RESOURCE, res);
  }

  public static ModelResourceLocation getModelResource(String res, String variant) {
    return new ModelResourceLocation(resource(res), variant);
  }

  /**
   * Prefixes the given unlocalized name with tinkers prefix. Use this when passing unlocalized names for a uniform
   * namespace.
   */
  public static String prefix(String name) {
    return String.format("%s.%s", RESOURCE, name.toLowerCase(Locale.US));
  }

  public static String translate(String key, String... pars) {
    // translates twice to allow rerouting/alias
    return StatCollector.translateToLocal(StatCollector.translateToLocal(String.format(key, (Object[]) pars)).trim()).trim();
  }

  public static String convertNewlines(String line) {
    if(line == null)
      return null;
    int j;
    while((j = line.indexOf("\\n")) >= 0)
    {
      line = line.substring(0, j) + '\n' + line.substring(j+2);
    }

    return line;
  }

  public static ResourceLocation getItemLocation(Item item) {
    // get the registered name for the object
    Object o = GameData.getItemRegistry().getNameForObject(item);

    // are you trying to add an unregistered item...?
    if(o == null) {
      TinkerRegistry.log.error("Item %s is not registered!" + item.getUnlocalizedName());
      // bad boi
      return null;
    }

    return (ResourceLocation) o;
  }

  public static ItemStack[] copyItemStackArray(ItemStack[] in) {
    return RecipeMatchRegistry.copyItemStackArray(in);
  }
}
