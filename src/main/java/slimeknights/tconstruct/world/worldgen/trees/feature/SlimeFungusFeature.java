package slimeknights.tconstruct.world.worldgen.trees.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.HugeFungusConfig;
import net.minecraft.world.gen.feature.HugeFungusFeature;
import slimeknights.tconstruct.world.worldgen.trees.config.SlimeFungusConfig;

import java.util.Random;

public class SlimeFungusFeature extends HugeFungusFeature {
  public SlimeFungusFeature(Codec<HugeFungusConfig> codec) {
    super(codec);
  }

  @Override
  public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, HugeFungusConfig config) {
    if (!(config instanceof SlimeFungusConfig)) {
      return super.place(reader, generator, rand, pos, config);
    }
    // must be on the right ground
    if (!reader.getBlockState(pos.below()).is(((SlimeFungusConfig) config).getGroundTag())) {
      return false;
    }
    // ensure not too tall
    int height = MathHelper.nextInt(rand, 4, 13);
    if (rand.nextInt(12) == 0) {
      height *= 2;
    }
    if (!config.planted && pos.getY() + height + 1 >= generator.getGenDepth()) {
      return false;
    }
    // actual generation
    boolean flag = !config.planted && rand.nextFloat() < 0.06F;
    reader.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
    this.placeStem(reader, rand, config, pos, height, flag);
    this.placeHat(reader, rand, config, pos, height, flag);
    return true;
  }
}
