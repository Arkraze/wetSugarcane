package ark.sugarwater.wetsugarcane;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

public class DuckweedBlock extends FlowerbedBlock {
	
	public DuckweedBlock (Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(FLOWER_AMOUNT, 1));
	}
	
	// Check 9x9x3 box for water
	private static boolean isWaterNearby (BlockView world, BlockPos pos) {
		
		for (BlockPos blockPos : BlockPos.iterate(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
			if (!world.getFluidState(blockPos).isIn(FluidTags.WATER)) continue;
			return true;
		}
		return false;
	}
	
	// Break when invalid
	@Override public void scheduledTick (BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) world.breakBlock(pos, true);
	}
	
	// Plantable on water, farmland, normal plantable blocks
	@Override protected boolean canPlantOnTop (BlockState floor, BlockView world, BlockPos pos) {
		FluidState fluidStateUp = world.getFluidState(pos.up());
		return fluidStateUp.isEmpty() && isWaterNearby(world, pos) && (super.canPlantOnTop(floor, world, pos) || floor.isOf(Blocks.FARMLAND) || isWater(world, pos));
	}
	
	//
	private boolean isWater (BlockView world, BlockPos pos) {
		return world.getFluidState(pos).isEqualAndStill(Fluids.WATER);
	}
	
	@Override protected void appendProperties (StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
	}
}
