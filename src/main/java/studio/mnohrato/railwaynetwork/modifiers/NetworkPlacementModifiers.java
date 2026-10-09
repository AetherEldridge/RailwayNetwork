package studio.mnohrato.railwaynetwork.modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.config.Config;
import studio.mnohrato.railwaynetwork.config.RailwayGenerationConfig;
import studio.mnohrato.railwaynetwork.content.RWNPlacementModifiers;

import java.util.stream.Stream;

public class NetworkPlacementModifiers extends PlacementModifier
{
	public static final Codec< NetworkPlacementModifiers > CODEC = Codec.unit( NetworkPlacementModifiers::new );
	
	public NetworkPlacementModifiers( )
	{ }
	
	@NotNull
	@Override
	public Stream< BlockPos > getPositions( @NotNull PlacementContext ctx, @NotNull RandomSource rand, @NotNull BlockPos pos )
	{
		RailwayGenerationConfig cfg = Config.rwpc;
		
		if( !cfg.xChunkPos( ).contains( ( pos.getX( ) >> 4 ) % cfg.xCycle( ) ) &&
			!cfg.zChunkPos( ).contains( ( pos.getZ( ) >> 4 ) % cfg.zCycle( ) ) &&
			!cfg.xChunkPos( ).contains( -( pos.getX( ) >> 4 ) % cfg.xCycle( ) ) &&
			!cfg.zChunkPos( ).contains( -( pos.getZ( ) >> 4 ) % cfg.zCycle( ) ) ) return Stream.empty( );
		
		ServerLevel level = ctx.getLevel( ).getLevel( );
		Integer y = cfg.levelHeightMap( ).get( level.dimension( ).location( ) );
		if( y == null ) return Stream.empty( );
		
		return Stream.of( new BlockPos( pos.getX( ) >> 4 << 4, y, pos.getZ( ) >> 4 << 4 ) );
	}
	
	@NotNull
	@Override
	public PlacementModifierType< ? > type( )
	{
		return RWNPlacementModifiers.NETWORK_PLACEMENT.get( );
	}
}
