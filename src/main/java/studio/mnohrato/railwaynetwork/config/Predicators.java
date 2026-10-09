package studio.mnohrato.railwaynetwork.config;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Predicators
{
	public static boolean testPositiveInt( Object o )
	{
		return o instanceof Integer i && i >= 0;
	}
	
	public static boolean testDimHeight( Object o )
	{
		if( !( o instanceof String s ) ) return false;
		
		String[] parts = s.split( "=" );
		
		if( parts.length != 2 ) return false;
		
		try
		{
			Integer.parseInt( parts[ 1 ] );
			return ResourceLocation.tryParse( parts[ 0 ] ) != null;
		}
		catch( RuntimeException ignored )
		{
			return false;
		}
	}
}
