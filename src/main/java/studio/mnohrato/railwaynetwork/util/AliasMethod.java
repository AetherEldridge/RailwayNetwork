package studio.mnohrato.railwaynetwork.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AliasMethod< K >
{
	private final List< K > keys;
	private final double[] prob;
	private final int[] alias;
	
	public AliasMethod( @NotNull Map< K, Integer > weights )
	{
		keys = new ArrayList<>( weights.keySet( ) );
		int n = keys.size( );
		prob = new double[ n ];
		alias = new int[ n ];
		
		long total = 0;
		for( int w : weights.values( ) ) total += w;
		
		double[] scaled = new double[ n ];
		Deque< Integer > small = new ArrayDeque<>( );
		Deque< Integer > large = new ArrayDeque<>( );
		
		for( int i = 0; i < n; i++ )
		{
			scaled[ i ] = ( double )weights.get( keys.get( i ) ) * n / total;
			if( scaled[ i ] < 1.0 ) small.add( i );
			else large.add( i );
		}
		
		while( !small.isEmpty( ) && !large.isEmpty( ) )
		{
			int s = small.poll( );
			int l = large.poll( );
			prob[ s ] = scaled[ s ];
			alias[ s ] = l;
			scaled[ l ] = scaled[ l ] + scaled[ s ] - 1.0;
			if( scaled[ l ] < 1.0 ) small.add( l );
			else large.add( l );
		}
		while( !large.isEmpty( ) ) prob[ large.poll( ) ] = 1.0;
		while( !small.isEmpty( ) ) prob[ small.poll( ) ] = 1.0;
	}
	
	public K random( )
	{
		int n = keys.size( );
		int i = ThreadLocalRandom.current( ).nextInt( n );
		return ThreadLocalRandom.current( ).nextDouble( ) < prob[ i ] ? keys.get( i ) : keys.get( alias[ i ] );
	}
	
	public static < E > E getRandom( @NotNull Map< E, Integer > weights )
	{
		return ( new AliasMethod<>( weights ) ).random( );
	}
}