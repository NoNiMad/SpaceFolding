package fr.nonimad.microdimensions.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterLeavingMicroDim extends Teleporter {
	private ChunkCoordinates dest;
	
	public TeleporterLeavingMicroDim(ChunkCoordinates tpTo, WorldServer par1WorldServer) {
		  super(par1WorldServer);
		  this.dest = tpTo;
	}
	
	/**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
    	par1Entity.setLocationAndAngles(dest.posX + 0.5D, dest.posY, dest.posZ + 0.5D, 0, 0);
    }
}