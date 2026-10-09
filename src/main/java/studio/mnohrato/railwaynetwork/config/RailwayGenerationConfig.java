package studio.mnohrato.railwaynetwork.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record RailwayGenerationConfig( int y, int xOff, int zOff, int xCycle, int zCycle, List< Integer > xChunkPos, List< Integer > zChunkPos )
{
	public static final Codec< RailwayGenerationConfig > CODEC = RecordCodecBuilder.create( instance -> instance.group(
		Codec.INT.fieldOf( "y" ).forGetter( i -> i.y ),
		Codec.INT.fieldOf( "x_off" ).forGetter( i -> i.xOff ),
		Codec.INT.fieldOf( "z_off" ).forGetter( i -> i.zOff ),
		Codec.INT.fieldOf( "x_cycle" ).forGetter( i -> i.zOff ),
		Codec.INT.fieldOf( "z_cycle" ).forGetter( i -> i.zOff ),
		Codec.INT.listOf( ).fieldOf( "x_chunk_pos" ).forGetter( i -> i.xChunkPos ),
		Codec.INT.listOf( ).fieldOf( "z_chunk_pos" ).forGetter( i -> i.zChunkPos )
	).apply( instance, RailwayGenerationConfig::new ) );
}
