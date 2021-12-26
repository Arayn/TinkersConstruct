package slimeknights.tconstruct.world.worldgen.islands.variants;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Plane;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerStructures;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

/**
 * Nether slime island variant that spawns in lava oceans
 */
public class BloodSlimeIslandVariant extends AbstractSlimeIslandVariant {
  public BloodSlimeIslandVariant(int index) {
    super(index, SlimeType.ICHOR, SlimeType.BLOOD);
  }

  @Override
  public ResourceLocation getStructureName(String variantName) {
    return TConstruct.getResource("slime_islands/blood/" + variantName);
  }

  @Override
  protected SlimeType getCongealedSlimeType(Random random) {
    return random.nextBoolean() ? SlimeType.BLOOD : SlimeType.ICHOR;
  }

  @Override
  public BlockState getLakeFluid() {
    return Objects.requireNonNull(TinkerFluids.magma.getBlock()).defaultBlockState();
  }

  @Nullable
  @Override
  public ConfiguredFeature<?,?> getTreeFeature(Random random) {
    return TinkerStructures.BLOOD_SLIME_ISLAND_FUNGUS;
  }

  @Override
  public StructureProcessor getStructureProcessor() {
    return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
  }

  private static boolean isLava(ISeedReader world, BlockPos pos) {
    return world.isEmptyBlock(pos) || world.getBlockState(pos).getBlock() == Blocks.LAVA;
  }

  @Override
  public boolean isPositionValid(ISeedReader world, BlockPos pos, ChunkGenerator generator) {
    BlockPos up = pos.above();
    if (isLava(world, up)) {
      for (Direction direction : Plane.HORIZONTAL) {
        if (!isLava(world, up.relative(direction))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
}
