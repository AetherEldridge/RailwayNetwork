package studio.mnohrato.railwaynetwork.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mnohrato.railwaynetwork.config.ForgeTOMLConfig;
import studio.mnohrato.railwaynetwork.config.JSONConfig;
import studio.mnohrato.railwaynetwork.util.AliasMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
		JSONConfig cfg = ForgeTOMLConfig.get( level.getLevel( ).dimension( ).location( ) );
		JSONConfig.WorldGen genCfg = cfg.generation( );
		JSONConfig.Pier pierCfg = cfg.pier( );
		
		int chunkX = origin.getX( ) >> 4;
		int chunkZ = origin.getZ( ) >> 4;
		if( !isAlongX( genCfg, chunkX ) && !isAlongZ( genCfg, chunkZ ) ) return false;
		
		int height = genCfg.y( );
		BlockPos newOrigin = new BlockPos( ( chunkX << 4 ) + genCfg.xOff( ), height, ( chunkZ << 4 ) + genCfg.zOff( ) );
		BlockPos.MutableBlockPos mutable = newOrigin.mutable( );
		
		JSONConfig.Bridge bridgeCfg = getType( genCfg, level.getLevel( ), chunkX, chunkZ );
		if( bridgeCfg == null ) return false;
		
		Map< String, Map< BlockState, Integer > > palette = bridgeCfg.palette( );
		List< List< String > > slices = bridgeCfg.slice( );
		
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
						level.setBlock( mutable, Blocks.AIR.defaultBlockState( ), ForgeTOMLConfig.blockPlacingFlag );
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
						Map< BlockState, Integer > weightTable = palette.get( "" + sliced[ dz ] );
						if( weightTable.isEmpty( ) ) continue;
						
						mutable.set( newOrigin.getX( ) + dx, newOrigin.getY( ) + dy, newOrigin.getZ( ) + dz );
						BlockState state = AliasMethod.getRandom( weightTable );
						BlockState target = level.getBlockState( mutable );
						if( state.is( Blocks.STRUCTURE_VOID ) || target.is( Blocks.BEDROCK ) ) continue;
						level.setBlock( mutable, state, ForgeTOMLConfig.blockPlacingFlag );
					}
				}
			}
		}
		
		if( pierCfg == null || !hasPier( pierCfg , genCfg , chunkX , chunkZ ) || pierCfg.type() == JSONConfig.Pier.PierType.NONE ) return true;
		
		boolean isFloaterType = pierCfg.type( ) == JSONConfig.Pier.PierType.FLOATER;
		boolean isFrustumType = pierCfg.type( ) == JSONConfig.Pier.PierType.FRUSTUM;
		boolean isPillarType = pierCfg.type( ) == JSONConfig.Pier.PierType.PILLAR;
		if( isPillarType || isFrustumType || isFloaterType )
		{
			mutable.set( newOrigin.getX( ), newOrigin.getY( ), newOrigin.getZ( ) );
			boolean keepPlacingPillar = true;
			for( int dy = -1; keepPlacingPillar; dy-- )
			{
				boolean placedOnceInCurrentLayer = false;
				
				int[] range = isFloaterType || ( isFrustumType && dy < 2 ) ? new int[]{ 6, 7, 8, 9 } : new int[]{ 5, 6, 7, 8, 9, 10 };
				
				for( int dx : range )
				{
					for( int dz : range )
					{
						Map< BlockState, Integer > weightTable = pierCfg.material( );
						if( weightTable.isEmpty( ) ) continue;
						
						mutable.set( newOrigin.getX( ) + dx, newOrigin.getY( ) + dy, newOrigin.getZ( ) + dz );
						BlockState state = AliasMethod.getRandom( weightTable );
						BlockState target = level.getBlockState( mutable );
						if( target.isSolid( ) ) continue;
						level.setBlock( mutable, state, ForgeTOMLConfig.blockPlacingFlag );
						placedOnceInCurrentLayer = true;
					}
				}
				
				if( isFloaterType && dy < -1 ) keepPlacingPillar = false;
				if( !placedOnceInCurrentLayer ) keepPlacingPillar = false;
			}
		}
		
		return true;
	}
	
	@Nullable
	private static JSONConfig.Bridge getType( JSONConfig.WorldGen cfg, @NotNull ServerLevel level, int chunkX, int chunkZ )
	{
		JSONConfig cfgSet = ForgeTOMLConfig.get( level.dimension( ).location( ) );
		
		if( isAlongX( cfg, chunkX ) && isAlongZ( cfg, chunkZ ) ) return cfgSet.bridgeC( );
		if( isAlongX( cfg, chunkX ) ) return cfgSet.bridgeX( );
		if( isAlongZ( cfg, chunkZ ) ) return cfgSet.bridgeZ( );
		return null;
	}
	
	private static boolean isAlongX( @NotNull JSONConfig.WorldGen cfg, int cX )
	{
		return cfg.xChunkPos( ).contains( cX % cfg.xCycle( ) ) || cfg.xChunkPos( ).contains( -cX % cfg.xCycle( ) );
	}
	
	private static boolean isAlongZ( @NotNull JSONConfig.WorldGen cfg, int cZ )
	{
		return cfg.zChunkPos( ).contains( cZ % cfg.zCycle( ) ) || cfg.zChunkPos( ).contains( -cZ % cfg.zCycle( ) );
	}
	
	private static boolean hasPier( @NotNull JSONConfig.Pier pierCfg, JSONConfig.WorldGen genCfg, int chunkX, int chunkZ )
	{
		if( isAlongX( genCfg, chunkX ) && isAlongZ( genCfg, chunkZ ) ) return true;
		if( isAlongX( genCfg, chunkX ) ) return shouldPlacePier( genCfg.zChunkPos( ), genCfg.zCycle( ), pierCfg.maxD( ), chunkZ );
		if( isAlongZ( genCfg, chunkZ ) ) return shouldPlacePier( genCfg.xChunkPos( ), genCfg.xCycle( ), pierCfg.maxD( ), chunkX );
		return false;
	}
	
	private static boolean shouldPlacePier( @NotNull List< Integer > hasPierAt, int cycle, int dist, int toBePlaced )
	{
		if( hasPierAt.isEmpty( ) ) return false;
		
		int left = Integer.MIN_VALUE;
		int right = Integer.MAX_VALUE;
		
		for( int base : hasPierAt )
		{
			int k = Math.floorDiv( toBePlaced - base, cycle );
			int candidateLeft = base + k * cycle;
			if( candidateLeft <= toBePlaced && candidateLeft > left ) left = candidateLeft;
			
			int candidateRight = base + ( k + 1 ) * cycle;
			if( candidateRight >= toBePlaced && candidateRight < right ) right = candidateRight;
			
			if( candidateLeft == toBePlaced ) return false;
		}
		
		int gap = right - left;
		int maxGap = dist + 1;
		
		if( gap <= maxGap ) return false;
		
		int n = ( int )Math.ceil( ( double )gap / maxGap ) - 1;
		if( n <= 0 ) return false;
		
		for( int k = 1; k <= n; k++ )
		{
			int pos = left + ( int )Math.ceil( ( double )k * gap / ( n + 1 ) );
			if( pos == toBePlaced ) return true;
		}
		
		return false;
	}
}