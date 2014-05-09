package fr.nonimad.microdimensions.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import fr.nonimad.microdimensions.dimension.WorldMicroDimProvider;

public class TileEntityAnchorBlock extends TileEntity {
	private int dimensionId;
	private byte size;
	
	public TileEntityAnchorBlock() {
		dimensionId = 0;
		size = 16;
	}
	
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("dimId", this.dimensionId);
        nbt.setByte("size", this.size);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.dimensionId = nbt.getInteger("dimId");
        this.size = nbt.getByte("size");
        
		if(!DimensionManager.isDimensionRegistered(dimensionId)) {
			DimensionManager.registerProviderType(dimensionId, WorldMicroDimProvider.class, true);
			DimensionManager.registerDimension(dimensionId, dimensionId);
			
			((WorldMicroDimProvider)DimensionManager.getProvider(dimensionId)).setAnchorTile(this);
		}
		
		if(DimensionManager.getWorld(dimensionId) == null)
			DimensionManager.initDimension(dimensionId);
    }
	
    public void setSize(byte s) {
    	this.size = s;
    	if(this.size > 16)
    		this.size = 16;
    	else if(this.size < 5)
    		this.size = 5;
    }
    
    public byte getSize() {
    	return this.size;
    }
    
    public void setDimensionId(int dimId) {
    	this.dimensionId = dimId;
    }
    
	public int getDimensionId() {
		if(this.dimensionId == 0)
			this.dimensionId = DimensionManager.getNextFreeDimId();
		return this.dimensionId;
	}
	
	public int getOriginDimensionId() {
		return this.worldObj.provider.dimensionId;
	}
	
	@Override
	public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbt);
    }
    
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }
}
