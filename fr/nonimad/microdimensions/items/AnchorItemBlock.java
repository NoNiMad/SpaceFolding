package fr.nonimad.microdimensions.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.nonimad.microdimensions.tiles.TileEntityAnchorBlock;

public class AnchorItemBlock extends ItemBlock {
	public AnchorItemBlock(Block block) {
		super(block);
		
		this.setMaxStackSize(1);
		this.setTextureName("bedrock");
	}
    
    @SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        if(par1ItemStack.hasTagCompound()) {
        	NBTTagCompound tag = par1ItemStack.getTagCompound();
        	par3List.add("Destination : " + tag.getInteger("dimId"));
        	par3List.add("Size : " + tag.getByte("size"));
        }
    }
    
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side, float dx, float dy, float dz) {
        if (side == 0) { --y; }
        if (side == 1) { ++y; }
        if (side == 2) { --z; }
        if (side == 3) { ++z; }
        if (side == 4) { --x; }
        if (side == 5) { ++x; }
		
		if (!par3World.setBlock(x, y, z, field_150939_a, side, 3))
            return false;

        if (par3World.getBlock(x, y, z) == field_150939_a)
        {
            field_150939_a.onBlockPlacedBy(par3World, x, y, z, par2EntityPlayer, par1ItemStack);
            field_150939_a.onPostBlockPlaced(par3World, x, y, z, side);
        }
		
		TileEntityAnchorBlock tile = (TileEntityAnchorBlock)par3World.getTileEntity(x, y, z);		
		
		if(tile != null) {
			if(par1ItemStack.hasTagCompound()) {
				NBTTagCompound tag = par1ItemStack.getTagCompound();
				
				tile.setDimensionId(tag.getInteger("dimId"));
				tile.setSize(tag.getByte("size"));
			}
		}
		
		par2EntityPlayer.destroyCurrentEquippedItem();
		
		return true;
	}
}
