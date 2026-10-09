package studio.mnohrato.railwaynetwork.config;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Predicators
{
	public static boolean testDimProfile( Object o )
	{
		if( !( o instanceof List< ? > list ) ) return false;
		
		for( Object e : list )
		{
			if( !( e instanceof String s ) ) return false;
			String[] parts = s.split( "=" );
			if( parts.length != 2 ) return false;
			if( ResourceLocation.tryParse( parts[ 0 ] ) == null ) return false;
		}
		return true;
	}
}
