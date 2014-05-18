package fr.nonimad.microdimensions.dimension;

import java.util.List;

import fr.nonimad.microdimensions.MicroDimensions;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderMicroDim implements IChunkProvider
{
    /** Reference to the World object. */
    private World worldObj;

    /** The biome that is used to generate the chunk */
    @SuppressWarnings("unused")
	private BiomeGenBase biome;

    public ChunkProviderMicroDim(World par1World)
    {
        this.worldObj = par1World;
    }

    /**
     * Generates the shape of the terrain for the chunk though its all stone though the water is frozen if the
     * temperature is low enough
     */
    public void generateTerrain(int par1, int par2, Block[] par3ArrayOfByte)
    {    	
		/*ChunkCoordinates spawn = this.worldObj.getSpawnPoint();
		int chunkMinX = (spawn.posX - 16) >> 4 ;
		int chunkMaxX = (spawn.posX + 15) >> 4;
		int chunkMinZ = (spawn.posZ - 16) >> 4;
		int chunkMaxZ = (spawn.posZ + 15) >> 4;
		
		if(par1 >= chunkMinX && par1 <= chunkMaxX && par2 >= chunkMinZ && par2 <= chunkMaxZ) {
			for(int i = 0; i < 16; i++) {
				for(int k = 0; k < 16; k++) {
					for(int j = 64; j < 96; j++) {
						if(j != 64 && j != 95) {
			        		if(((i == 15 && par1 == chunkMinX)
		        	        	|| (i == 0 && par1 == chunkMaxX)
		        	        	|| (k == 15 && par2 == chunkMinZ)
		        	        	|| (k == 0 && par2 == chunkMaxZ))
		        	        	&& !(i == 15 && par1 == chunkMaxX)
		        	        	&& !(i == 0 && par1 == chunkMinX)
		        	        	&& !(k == 15 && par2 == chunkMaxZ)
		        	        	&& !(k == 0 && par2 == chunkMinZ))
			        			continue;
		        		}
						
			        	if(i == 0 || i == 15 || j == 64 || j == 95 || k == 0 || k == 15) {
			        		par3ArrayOfByte[i << 11 | k << 7 | j] = Block.getBlockFromName("bedrock");
			        	}
		        	}
		    	}
			}
		}*/
		
    	int size = ((WorldMicroDimProvider)worldObj.provider).getAnchorTile().getSize();
    	
		if(par1 == 0 && par2 == 0) {
			for(int i = 0; i < size; i++) {
				for(int k = 0; k < size; k++) {
					for(int j = 64; j < 64 + size; j++) {
			        	if(((i == 0 || i == size - 1) && (j == 64 || j == 63 + size))
			        		|| ((i == 0 || i == size - 1) && (k == 0 || k == size - 1))
			        		|| ((j == 64 || j == 63 + size) && (k == 0 || k == size - 1))) {
			        		par3ArrayOfByte[i << 11 | k << 7 | j] = Block.getBlockFromName("bedrock");
			        	} else if(i == 0 || i == size - 1 || j == 64 || j == 63 + size || k == 0 || k == size - 1) {
			        		par3ArrayOfByte[i << 11 | k << 7 | j] = MicroDimensions.dimTransporterBlock;
			        	}
		        	}
		    	}
			}
		}
    }

    /**
     * Replaces the stone that was placed in with blocks that match the biome
     */
    public void replaceBlocksForBiome(int par1, int par2, byte[] par3ArrayOfByte, BiomeGenBase[] par4ArrayOfBiomeGenBase)
    {
        
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int par1, int par2)
    {
        return this.provideChunk(par1, par2);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int par1, int par2)
    {
        Block[] var3 = new Block[32768];
        this.generateTerrain(par1, par2, var3);

        Chunk var4 = new Chunk(this.worldObj, var3, par1, par2);
        var4.generateSkylightMap();
        return var4;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int par1, int par2)
    {
    	if(par1 == 0 && par2 == 0)
    		return true;
    	return false;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
    {

    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }

    /**
     * Unloads the 100 oldest chunks from memory, due to a bug with chunkSet.add() never being called it thinks the list
     * is always empty and will not remove any chunks.
     */
    public boolean unload100OldestChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "RandomLevelSource";
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    @SuppressWarnings("rawtypes")
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5)
    {
        return null;
    }

    public int getLoadedChunkCount() {
        return 0;
    }

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}
	
	@Override
	public ChunkPosition func_147416_a(World var1, String var2, int var3, int var4, int var5) {
		return null;
	}

	@Override
	public void recreateStructures(int var1, int var2) {}

	@Override
	public void saveExtraData() {}
}