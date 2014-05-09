package fr.nonimad.microdimensions.blocks;

import java.util.ArrayList;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import fr.nonimad.microdimensions.MicroDimensions;
import fr.nonimad.microdimensions.dimension.TeleporterMicroDim;
import fr.nonimad.microdimensions.dimension.WorldMicroDimProvider;
import fr.nonimad.microdimensions.tiles.TileEntityAnchorBlock;

public class AnchorBlock extends BlockContainer
{
	public AnchorBlock() 
	{
		super(Material.iron);
		//this.setResistance(6000000.0F);
		this.setCreativeTab(MicroDimensions.tabMicroDimensions);
		this.setBlockName("AnchorBlock");
		this.setBlockTextureName("obsidian");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,	int y, int z, EntityPlayer player, int side, float dx, float dy, float dz) {
		if(world.isRemote) {
			return false;
		}
		
		TileEntityAnchorBlock tile = (TileEntityAnchorBlock)world.getTileEntity(x, y, z);
		if(tile == null) {
			return false;
		}
		
		int dimId = tile.getDimensionId();
		
		if(!DimensionManager.isDimensionRegistered(dimId)) {
			DimensionManager.registerProviderType(dimId, WorldMicroDimProvider.class, true);
			DimensionManager.registerDimension(dimId, dimId);
			
			MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(tile.getDescriptionPacket());
		}
		
		if(DimensionManager.getWorld(dimId) == null)
			DimensionManager.initDimension(dimId);
		
		((WorldMicroDimProvider)DimensionManager.getProvider(dimId)).setAnchorTile(tile);
		
		if (player.ridingEntity == null && player.riddenByEntity == null)
		{
			EntityPlayerMP thePlayer = (EntityPlayerMP) player;
			if (player.dimension != dimId)
			{
				thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, dimId, new TeleporterMicroDim(tile.getSize(), thePlayer.mcServer.worldServerForDimension(dimId)));
			}
		}
		
		return false;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntityAnchorBlock tile = (TileEntityAnchorBlock)world.getTileEntity(x, y, z);
		if(tile == null) {
			return;
		}
		
		if(!world.isRemote) {
			((WorldMicroDimProvider)DimensionManager.getWorld(tile.getDimensionId()).provider).ejectPlayersFromDimension();
		}
		DimensionManager.unloadWorld(tile.getDimensionId());
		
        ItemStack drop = new ItemStack(Item.getItemFromBlock(block));
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("dimId", tile.getDimensionId());
        nbt.setByte("size", tile.getSize());
        drop.setTagCompound(nbt);
		
		float f = 0.7F;
        double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, drop);
        entityitem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityitem);
        
        super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,	int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>(); return ret;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityAnchorBlock();
	}
}