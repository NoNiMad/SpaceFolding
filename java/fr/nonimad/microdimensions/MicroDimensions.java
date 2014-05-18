package fr.nonimad.microdimensions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.nonimad.microdimensions.blocks.AnchorBlock;
import fr.nonimad.microdimensions.blocks.DimTransporterBlock;
import fr.nonimad.microdimensions.dimension.BiomeGenMicroDim;
import fr.nonimad.microdimensions.dimension.WorldMicroDimProvider;
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
	
	public static int providerId = 42;
	public static ArrayList<Integer> dimensions;
	
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
		
		DimensionManager.registerProviderType(providerId, WorldMicroDimProvider.class, true);
		
		proxy.registerRenders();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { }
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		MinecraftServer srv = event.getServer();
		registerDimensions(new File(srv.getEntityWorld().getSaveHandler().getWorldDirectory(), "microdims.list"));
	}
	
	public void registerDimensions(File dimFile) {
		try {
			if(!dimFile.exists())
				dimFile.createNewFile();
			
			/*PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dimFile)));
			writer.println();*/
			
			Scanner scanner= new Scanner(dimFile);
			
			while (scanner.hasNextLine()) {
			    int dim = Integer.valueOf(scanner.nextLine());
			    dimensions.add(dim);
			    DimensionManager.registerDimension(dim, providerId);
			}
			 
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unregisterDimensions() {
		
	}
}