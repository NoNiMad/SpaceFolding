package fr.nonimad.microdimensions.dimension;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import fr.nonimad.microdimensions.MicroDimensions;
import fr.nonimad.microdimensions.tiles.TileEntityAnchorBlock;

public class WorldMicroDimProvider extends WorldProvider
{
	private TileEntityAnchorBlock anchorTile;
	
	public TileEntityAnchorBlock getAnchorTile() {
		return anchorTile;
	}

	public void setAnchorTile(TileEntityAnchorBlock anchorTile) {
		this.anchorTile = anchorTile;
	}
	
	@SuppressWarnings("rawtypes")
	public void ejectPlayersFromDimension() {
		List playerList = this.worldObj.playerEntities;
		for(Iterator i = playerList.iterator(); i.hasNext();) {
			EntityPlayerMP player = (EntityPlayerMP) i.next();
			if(player.riddenByEntity != null) { player.riddenByEntity.mountEntity(null); }
			if(player.ridingEntity != null) { player.mountEntity(null); }
			
			if (this.dimensionId != anchorTile.getOriginDimensionId()) {				
				i.remove();
				player.mcServer.getConfigurationManager().transferPlayerToDimension(player, anchorTile.getOriginDimensionId(), new TeleporterLeavingMicroDim(new ChunkCoordinates(anchorTile.xCoord, anchorTile.yCoord + 5, anchorTile.zCoord), player.mcServer.worldServerForDimension(anchorTile.getOriginDimensionId())));
			}
		}
	}
	
	@Override
	public String getDimensionName() {
		return "Micro Dimension " + this.dimensionId;
	}

	@Override
	public boolean canRespawnHere()
	{
		return false;
	}

	@Override
	public void registerWorldChunkManager()
	{
		this.worldChunkMgr = new WorldChunkManagerHell(MicroDimensions.microDimBiome, 0);
	}
	
	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkProviderMicroDim(worldObj);
	}

	@Override
	public long getWorldTime() {
		return 6000;
	}
}
