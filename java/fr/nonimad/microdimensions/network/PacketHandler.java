package fr.nonimad.microdimensions.network;

import fr.nonimad.microdimensions.MicroDimensions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.EnumMap;

import net.minecraft.network.Packet;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Sharable
public enum PacketHandler {
	INSTANCE;
	
    private EnumMap<Side, FMLEmbeddedChannel> channels;
	
	private PacketHandler() {
        this.channels = NetworkRegistry.INSTANCE.newChannel("SF|RegisterDimId", new Codec());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            addClientHandler();
        }
	}
	
    @SideOnly(Side.CLIENT)
    private void addClientHandler() {
        FMLEmbeddedChannel clientChannel = this.channels.get(Side.CLIENT);
        String codec = clientChannel.findChannelHandlerNameForType(Codec.class);
        clientChannel.pipeline().addAfter(codec, "ClientHandler", new MessageHandler());
    }
	
    private static class MessageHandler extends SimpleChannelInboundHandler<Message>
    {
    	@Override
    	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        	if(MicroDimensions.dimensions == null)
        		MicroDimensions.dimensions = new ArrayList<Integer>();
        	for(int i = 0; i < msg.lenght; i++) {
        		if(!MicroDimensions.dimensions.contains(msg.dimIds[i])) {
        			MicroDimensions.dimensions.add(msg.dimIds[i]);
        			DimensionManager.registerDimension(msg.dimIds[i], MicroDimensions.providerId);
        		}
        	}
    	}
    }
    
    public static class Message
    {
        int lenght;
        int[] dimIds;
    }
    
    private class Codec extends FMLIndexedMessageToMessageCodec<Message>
    {
        public Codec()
        {
            addDiscriminator(0, Message.class);
        }
        
        @Override
        public void encodeInto(ChannelHandlerContext ctx, Message msg, ByteBuf target) throws Exception
        {
        	target.writeInt(msg.lenght);
        	for(int i = 0; i < msg.lenght; i++) {
        		target.writeInt(msg.dimIds[i]);
        	}
        }

        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, Message msg)
        {
        	msg.lenght = dat.readInt();
        	msg.dimIds = new int[msg.lenght];
        	for(int i = 0; i < msg.lenght; i++) {
        		msg.dimIds[i] = dat.readInt();
        	}
        }
    }
    
    public static Packet getPacket(int[] dimensionsId)
    {
    	Message msg = new Message();
    	msg.lenght = dimensionsId.length;
    	msg.dimIds = dimensionsId;
    	
        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
    
    public static Packet getPacket(int dimension)
    {
    	Message msg = new Message();
    	msg.lenght = 1;
    	msg.dimIds = new int[]{dimension};
    	
        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
}