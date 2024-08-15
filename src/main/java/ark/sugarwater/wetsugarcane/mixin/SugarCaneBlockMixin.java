package ark.sugarwater.wetsugarcane.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin (SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin extends Block implements Waterloggable {
	
	@Unique private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	public SugarCaneBlockMixin (Settings settings) {
		super(settings);
	}
	
	@Inject (method = "<init>", at = @At ("TAIL"))
	private void addWaterloggedDefaultState (Settings settings, CallbackInfo ci) {
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
	}
	
	// Determine if waterlogged on placement
	@Override
	public BlockState getPlacementState (ItemPlacementContext ctx) {
		boolean isWater = ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER);
		return this.getDefaultState().with(WATERLOGGED, isWater);
	}
	
	// Tick fluids if waterlogged
	@Inject (method = "getStateForNeighborUpdate", at = @At ("TAIL"))
	private void tickFluidIfWaterlogged (BlockState state, Direction direction, BlockState neighborState,
			WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
		if (state.get(WATERLOGGED))
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
	}
	
	// Skip water adjacency check if the target is a water source
	@Inject (method = "canPlaceAt", at = @At (value = "INVOKE_ASSIGN", ordinal = 1, target = "Lnet/minecraft/util/math/BlockPos;down()Lnet/minecraft/util/math/BlockPos;"), cancellable = true)
	private void standAloneAsWaterlogged (BlockState state, WorldView world, BlockPos pos,
			CallbackInfoReturnable<Boolean> cir) {
		if (state.getFluidState().isEqualAndStill(Fluids.WATER))
			cir.setReturnValue(true);
	}
	
	@Inject (method = "appendProperties", at = @At ("TAIL"))
	private void appendWaterlogged (StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
		builder.add(WATERLOGGED);
	}
	
	// Display water if waterlogged
	@Override
	@SuppressWarnings ("deprecation")
	public FluidState getFluidState (BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : state.getFluidState();
	}
}
