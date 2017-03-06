package zmaster587.advancedRocketry.tile;

import zmaster587.advancedRocketry.api.AdvancedRocketryBlocks;
import zmaster587.libVulpes.block.BlockFullyRotatable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileForceFieldProjector extends TileEntity implements ITickable {

	private short extensionRange;
	private static short MAX_RANGE = 32;

	public TileForceFieldProjector() {
		extensionRange = 0;
	}


	public void destroyField(EnumFacing facing) {
		while(extensionRange > 0) {
			BlockPos nextPos = pos.offset(facing, extensionRange);

			if(worldObj.getBlockState(nextPos).getBlock() == AdvancedRocketryBlocks.blockForceField)
				worldObj.setBlockToAir(nextPos);
			extensionRange--;
		}
	}

	@Override
	public void update() {

		if(worldObj.getTotalWorldTime() % 5 == 0) {
			if(worldObj.isBlockPowered(getPos())) {
				if(extensionRange < MAX_RANGE) {
					if(extensionRange == 0)
						extensionRange = 1;

					IBlockState state = worldObj.getBlockState(getPos());
					if(state.getBlock() == AdvancedRocketryBlocks.blockForceFieldProjector) {
						EnumFacing facing = BlockFullyRotatable.getFront(state);
						BlockPos nextPos = pos.offset(facing, extensionRange);
						if(worldObj.getBlockState(nextPos).getBlock().isReplaceable(worldObj, nextPos) ) {
							worldObj.setBlockState(nextPos, AdvancedRocketryBlocks.blockForceField.getDefaultState());
							extensionRange++;
						} else if(worldObj.getBlockState(nextPos).getBlock() == AdvancedRocketryBlocks.blockForceField) {
							extensionRange++;
						}
					}
				}
			}
			else if(extensionRange > 0) {

				IBlockState state = worldObj.getBlockState(getPos());
				if(state.getBlock() == AdvancedRocketryBlocks.blockForceFieldProjector) {
					EnumFacing facing = BlockFullyRotatable.getFront(state);
					BlockPos nextPos = pos.offset(facing, extensionRange);

					if(worldObj.getBlockState(nextPos).getBlock() == AdvancedRocketryBlocks.blockForceField)
						worldObj.setBlockToAir(nextPos);
					extensionRange--;
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("ext", extensionRange);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		extensionRange = nbt.getShort("ext");
		super.readFromNBT(nbt);
	}

}
