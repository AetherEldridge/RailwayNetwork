package studio.mnohrato.railwaynetwork.cache;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import studio.mnohrato.railwaynetwork.RailwayNetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateCache
{
	public static final Map< ResourceLocation, StructureTemplate > CACHE = new HashMap<>( );
	
	public static void reloadCache( ServerLevel level )
	{
		CACHE.clear( );
		for( String id : List.of( "rail_x", "rail_z", "rail_c", "empty" ) )
		{
			ResourceLocation rl = RailwayNetwork.rl( id );
			level.getStructureManager( ).get( rl ).ifPresent( t -> CACHE.put( rl, t ) );
		}
	}
}
