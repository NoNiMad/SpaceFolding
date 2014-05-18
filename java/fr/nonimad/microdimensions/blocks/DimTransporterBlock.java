package fr.nonimad.microdimensions.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import fr.nonimad.microdimensions.MicroDimensions;
import fr.nonimad.microdimensions.dimension.TeleporterLeavingMicroDim;
import fr.nonimad.microdimensions.dimension.WorldMicroDimProvider;
import fr.nonimad.microdimensions.tiles.TileEntityAnchorBlock;

public class DimTransporterBlock extends Block //BlockContainer
{
	public DimTransporterBlock() {
		super(Material.rock);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setCreativeTab(MicroDimensions.tabMicroDimensions);
		this.setBlockName("DimTransporterBlock");
		this.setBlockTextureName("stone");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float dx, float dy, float dz) {
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().stackSize > 0) {
			return false;
		}
		
		WorldProvider provider = world.provider;

		if(provider instanceof WorldMicroDimProvider && player.ridingEntity == null && player.riddenByEntity == null && player instanceof EntityPlayerMP) {
			TileEntityAnchorBlock tileAnchor = ((WorldMicroDimProvider)provider).getAnchorTile();
			
			if(tileAnchor == null) {
				return false;
			}
				
			int destination = tileAnchor.getOriginDimensionId();
			EntityPlayerMP thePlayer = (EntityPlayerMP) player;
			
			if (provider.dimensionId != destination) {
				thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, destination, new TeleporterLeavingMicroDim(new ChunkCoordinates(tileAnchor.xCoord, tileAnchor.yCoord + 1, tileAnchor.zCoord), thePlayer.mcServer.worldServerForDimension(destination)));
			}
		}
		
		return true;
	}
	
	/*@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntity();
	}*/
}
