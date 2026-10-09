package studio.mnohrato.railwaynetwork.config;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public record JSONConfig( Bridge bridgeX, Bridge bridgeZ, Bridge bridgeC, Pier pier, WorldGen generation )
{
	private static final Codec< BlockState > BLOCKSTATE_STRING_CODEC = Codec.STRING.comapFlatMap( JSONConfig::parseBlockState, StateHolder::toString );
	
	public record Bridge( Map< String, Map< BlockState, Integer > > palette, List< List< String > > slice )
	{
		public static final Codec< Bridge > CODEC = RecordCodecBuilder.create( instance -> instance.group(
			Codec.unboundedMap( Codec.STRING, Codec.unboundedMap( BLOCKSTATE_STRING_CODEC, Codec.INT ) ).fieldOf( "palette" ).forGetter( Bridge::palette ),
			Codec.list( Codec.list( Codec.STRING ) ).fieldOf( "slice" ).forGetter( Bridge::slice )
		).apply( instance, Bridge::new ) );
	}
	
	public record WorldGen( int y, int xOff, int zOff, int xCycle, int zCycle, List< Integer > xChunkPos, List< Integer > zChunkPos )
	{
		public static final Codec< WorldGen > CODEC = RecordCodecBuilder.create( instance -> instance.group(
			Codec.INT.fieldOf( "y" ).forGetter( WorldGen::y ),
			Codec.INT.fieldOf( "x_off" ).forGetter( WorldGen::xOff ),
			Codec.INT.fieldOf( "z_off" ).forGetter( WorldGen::zOff ),
			Codec.INT.fieldOf( "x_cycle" ).forGetter( WorldGen::zOff ),
			Codec.INT.fieldOf( "z_cycle" ).forGetter( WorldGen::zOff ),
			Codec.INT.listOf( ).fieldOf( "x_chunk_pos" ).forGetter( WorldGen::xChunkPos ),
			Codec.INT.listOf( ).fieldOf( "z_chunk_pos" ).forGetter( WorldGen::zChunkPos )
		).apply( instance, WorldGen::new ) );
	}
	
	public record Pier( int maxD, PierType type, Map< BlockState, Integer > material )
	{
		public static final Codec< Pier > CODEC = RecordCodecBuilder.create( instance -> instance.group(
			Codec.INT.fieldOf( "max_distance" ).forGetter( Pier::maxD ),
			Codec.STRING.xmap( PierType::valueOf, PierType::name ).fieldOf( "pier_type" ).forGetter( Pier::type ),
			Codec.unboundedMap( BLOCKSTATE_STRING_CODEC, Codec.INT ).fieldOf( "material" ).forGetter( Pier::material )
		).apply( instance, Pier::new ) );
		
		public enum PierType
		{
			FRUSTUM, PILLAR, FLOATER, NONE;
		}
	}
	
	@NotNull
	private static DataResult< BlockState > parseBlockState( String s )
	{
		try
		{
			return DataResult.success( BlockStateParser.parseForBlock( BuiltInRegistries.BLOCK.asLookup( ), s, false ).blockState( ) );
		}
		catch( CommandSyntaxException e )
		{
			throw new RuntimeException( e );
		}
	}
}
