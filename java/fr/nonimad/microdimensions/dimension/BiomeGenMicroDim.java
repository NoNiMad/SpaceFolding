package fr.nonimad.microdimensions.dimension;

import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenMicroDim extends BiomeGenBase {
    public BiomeGenMicroDim(int par1)
    {
        super(par1);
        this.spawnableCreatureList.clear();
        this.topBlock = Block.getBlockFromName("bedrock");
        this.fillerBlock = Block.getBlockFromName("bedrock");
    }
}
