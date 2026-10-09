package studio.mnohrato.railwaynetwork.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mnohrato.railwaynetwork.config.Config;
import studio.mnohrato.railwaynetwork.config.RailwayGenerationConfig;
import studio.mnohrato.railwaynetwork.config.RailwayPlacementConfigSet;
import studio.mnohrato.railwaynetwork.util.AliasMethod;

import java.util.List;
import java.util.Map;

public class RailwayFeature extends Feature< NoneFeatureConfiguration >
{
	public RailwayFeature( Codec< NoneFeatureConfiguration > codec )
	{
		super( codec );
	}
	
	@Override
	public boolean place( @NotNull FeaturePlaceContext< NoneFeatureConfiguration > ctx )
	{
		WorldGenLevel level = ctx.level( );
		BlockPos origin = ctx.origin( );
		RandomSource rand = ctx.random( );
		RailwayPlacementConfigSet cfg = Config.get( level.getLevel( ).dimension( ).location( ) );
		RailwayGenerationConfig generation = cfg.generation( );
		
		int chunkX = origin.getX( ) >> 4;
		int chunkZ = origin.getZ( ) >> 4;
		if( !isAlongX( generation, chunkX ) && !isAlongZ( generation, chunkZ ) ) return false;
		
		int height = generation.y( );
		BlockPos newOrigin = new BlockPos( ( chunkX << 4 ) + generation.xOff( ), height, ( chunkZ << 4 ) + generation.zOff( ) );
		BlockPos.MutableBlockPos mutable = newOrigin.mutable( );
		
		RailwayPlacementConfigSet.RailwayPlacementConfig type = getType( generation, level.getLevel( ), chunkX, chunkZ );
		if( type == null ) return false;
		
		Map< String, Map< BlockState, Integer > > palette = type.palette( );
		List< List< String > > slices = type.slice( );
		
		for( int dy = 0; dy < slices.size( ); dy++ )
		{
			List< String > slice = slices.get( dy );
			if( slice.isEmpty( ) )
			{
				for( int dx = 0; dx < 16; dx++ )
				{
					for( int dz = 0; dz < 16; dz++ )
					{
						mutable.set( newOrigin.getX( ) + dx, newOrigin.getY( ) + dy, newOrigin.getZ( ) + dz );
						level.setBlock( mutable, Blocks.AIR.defaultBlockState( ), 3 );
					}
				}
			}
			else
			{
				for( int dx = slice.size( ) - 1; dx >= 0; dx-- )
				{
					char[] sliced = slice.get( dx ).toCharArray( );
					for( int dz = 0; dz < sliced.length; dz++ )
					{
						char symbol = sliced[ dz ];
						Map< BlockState, Integer > weightTable = palette.get( "" + symbol );
						if( weightTable.isEmpty( ) ) continue;
						
						BlockState state = AliasMethod.getRandom( weightTable );
						BlockState target = level.getBlockState( mutable );
						if( state.is( Blocks.STRUCTURE_VOID ) || target.is( Blocks.BEDROCK ) ) continue;
						mutable.set( newOrigin.getX( ) + dx, newOrigin.getY( ) + dy, newOrigin.getZ( ) + dz );
						level.setBlock( mutable, state, Config.blockPlacingFlag );
					}
				}
			}
		}
		
		return true;
	}
	
	@Nullable
	private static RailwayPlacementConfigSet.RailwayPlacementConfig getType( RailwayGenerationConfig cfg, @NotNull ServerLevel level, int chunkX, int chunkZ )
	{
		RailwayPlacementConfigSet cfgSet = Config.get( level.dimension( ).location( ) );
		
		if( isAlongX( cfg, chunkX ) && isAlongZ( cfg, chunkZ ) ) return cfgSet.railC( );
		if( isAlongX( cfg, chunkX ) ) return cfgSet.railX( );
		if( isAlongZ( cfg, chunkZ ) ) return cfgSet.railZ( );
		return null;
	}
	
	public static boolean isAlongX( @NotNull RailwayGenerationConfig cfg, int cX )
	{
		return cfg.xChunkPos( ).contains( cX % cfg.xCycle( ) ) || cfg.xChunkPos( ).contains( -cX % cfg.xCycle( ) );
	}
	
	public static boolean isAlongZ( @NotNull RailwayGenerationConfig cfg, int cZ )
	{
		return cfg.zChunkPos( ).contains( cZ % cfg.zCycle( ) ) || cfg.zChunkPos( ).contains( -cZ % cfg.zCycle( ) );
	}
}