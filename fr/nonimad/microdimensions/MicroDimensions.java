package fr.nonimad.microdimensions;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.nonimad.microdimensions.blocks.AnchorBlock;
import fr.nonimad.microdimensions.blocks.DimTransporterBlock;
import fr.nonimad.microdimensions.dimension.BiomeGenMicroDim;
import fr.nonimad.microdimensions.items.AnchorItemBlock;
import fr.nonimad.microdimensions.tiles.TileEntityAnchorBlock;

@Mod(modid = MicroDimensions.MOD_ID, name = MicroDimensions.MOD_NAME, version = MicroDimensions.MOD_VERSION)
public class MicroDimensions 
{
	// -- Constants -- //
	public static final String MOD_ID = "MicroDimensions";
	public static final String MOD_VERSION = "Alpha 0_0";
	public static final String MOD_NAME = "MicroDimensions";
	
	public static final CreativeTabs tabMicroDimensions = new CreativeTabMicroDimensions("MicroDimensions");
	
	@SidedProxy(clientSide = "fr.nonimad.microdimensions.ClientProxy", serverSide = "fr.nonimad.microdimensions.CommonProxy")
    public static CommonProxy proxy;
	
    @Instance("MicroDimensions")
    public static MicroDimensions instance;

	// -- Blocks -- //
	public static Block anchorBlock;
	public static Block dimTransporterBlock;
	
	// -- Biome -- //
	public static BiomeGenBase microDimBiome;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		instance = this;
		
		// -- Blocks Declaration -- //
		anchorBlock =  new AnchorBlock();
		dimTransporterBlock = new DimTransporterBlock();
		microDimBiome = new BiomeGenMicroDim(53).setBiomeName("MicroDim Biome");
		
		// -- Blocks Registry -- //
		GameRegistry.registerBlock(anchorBlock, AnchorItemBlock.class, "anchorblock");
		GameRegistry.registerBlock(dimTransporterBlock, "dimtransporterblock");
		
		GameRegistry.registerTileEntity(TileEntityAnchorBlock.class, "anchorblocktile");
		
		proxy.registerRenders();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { }
}