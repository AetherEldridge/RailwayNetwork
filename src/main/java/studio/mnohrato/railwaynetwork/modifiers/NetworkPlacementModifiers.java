package studio.mnohrato.railwaynetwork.modifiers;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.config.ForgeTOMLConfig;
import studio.mnohrato.railwaynetwork.config.JSONConfig;
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
		JSONConfig set = ForgeTOMLConfig.get( ctx.getLevel( ).getLevel( ).dimension( ).location( ) );
		if( set == null ) return Stream.empty();
		JSONConfig.WorldGen cfg = set.generation( );
		if( !cfg.xChunkPos( ).contains( ( pos.getX( ) >> 4 ) % cfg.xCycle( ) ) &&
			!cfg.zChunkPos( ).contains( ( pos.getZ( ) >> 4 ) % cfg.zCycle( ) ) &&
			!cfg.xChunkPos( ).contains( -( pos.getX( ) >> 4 ) % cfg.xCycle( ) ) &&
			!cfg.zChunkPos( ).contains( -( pos.getZ( ) >> 4 ) % cfg.zCycle( ) ) ) return Stream.empty( );
		return Stream.of( new BlockPos( pos.getX( ) >> 4 << 4, cfg.y( ), pos.getZ( ) >> 4 << 4 ) );
	}
	
	@NotNull
	@Override
	public PlacementModifierType< ? > type( )
	{
		return RWNPlacementModifiers.NETWORK_PLACEMENT.get( );
	}
}
