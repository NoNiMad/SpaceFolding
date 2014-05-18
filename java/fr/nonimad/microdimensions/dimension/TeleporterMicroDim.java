package fr.nonimad.microdimensions.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterMicroDim extends Teleporter {
	private int size;
	
	public TeleporterMicroDim(int size, WorldServer par1WorldServer) {
		  super(par1WorldServer);
		  this.size = size;
	}
	
	/**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
    	par1Entity.setLocationAndAngles(size / 2, 65, size / 2, 0, 0);
    }
}