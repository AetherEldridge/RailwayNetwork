package studio.mnohrato.railwaynetwork.config;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;

import java.util.List;
import java.util.Map;

public record RailwayPlacementConfigSet( RailwayPlacementConfig railX, RailwayPlacementConfig railZ, RailwayPlacementConfig railC, RailwayGenerationConfig generation )
{
	public record RailwayPlacementConfig( Map< String, Map< BlockState, Integer > > palette, List< List< String > > slice )
	{
		public static final Codec< BlockState > BLOCKSTATE_STRING_CODEC = Codec.STRING.comapFlatMap(
			s -> {
				try
				{
					return DataResult.success( BlockStateParser.parseForBlock( BuiltInRegistries.BLOCK.asLookup( ), s, false ).blockState( ) );
				}
				catch( CommandSyntaxException e )
				{
					throw new RuntimeException( e );
				}
			}, StateHolder::toString
		);
		
		public static final Codec< RailwayPlacementConfig > CODEC = RecordCodecBuilder.create( instance -> instance.group(
			Codec.unboundedMap( Codec.STRING, Codec.unboundedMap( BLOCKSTATE_STRING_CODEC, Codec.INT ) ).fieldOf( "palette" ).forGetter( i -> i.palette ),
			Codec.list( Codec.list( Codec.STRING ) ).fieldOf( "slice" ).forGetter( i -> i.slice )
		).apply( instance, RailwayPlacementConfig::new ) );
	}
}
