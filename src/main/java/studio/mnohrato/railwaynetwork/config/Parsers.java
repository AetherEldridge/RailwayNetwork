package studio.mnohrato.railwaynetwork.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.RailwayNetwork;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;

public class Parsers
{
	public static RailwayPlacementConfig parsePlacement( String fileName )
	{
		String target = "%s/%s/placements/%s.json".formatted( FMLPaths.CONFIGDIR.get( ).toString( ), RailwayNetwork.MOD_ID, fileName );
		String resource = "/data/%s/%s/%s.json".formatted( RailwayNetwork.MOD_ID, RailwayNetwork.MOD_ID, fileName );
		try
		{
			JsonElement je = new Gson( ).fromJson(
				new FileReader( target ),
				JsonElement.class
			);
			return RailwayPlacementConfig.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
		}
		catch( FileNotFoundException fileNotFoundException )
		{
			copyDefaultConfig( resource , target );
			
			try
			{
				JsonElement je = new Gson( ).fromJson(
					new FileReader( target ),
					JsonElement.class
				);
				return RailwayPlacementConfig.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
			}
			catch( FileNotFoundException fileNotFoundAgain )
			{
				throw new RuntimeException( fileNotFoundAgain );
			}
		}
	}
	
	public static void copyDefaultConfig( String resourcePath, String targetFileName )
	{
		Path configDir = FMLPaths.CONFIGDIR.get( );
		Path target = configDir.resolve( targetFileName );
		
		if( Files.exists( target ) ) return;
		
		try( InputStream in = Parsers.class.getResourceAsStream( resourcePath ) )
		{
			if( in == null )
			{
				System.err.println( "Resource not found: " + resourcePath );
				return;
			}
			Files.createDirectories( target.getParent( ) );
			Files.copy( in, target, StandardCopyOption.REPLACE_EXISTING );
		}
		catch( IOException e )
		{
			e.printStackTrace( );
		}
	}
	
	@NotNull
	public static HashMap< ResourceLocation, Integer > parseResourceLocationInteger( List< ? extends String > mapStrings )
	{
		HashMap< ResourceLocation, Integer > heightsInLevel = new HashMap<>( );
		
		if( mapStrings != null )
		{
			mapStrings.forEach( ( s ) -> {
				String[] parts = s.split( "=" );
				if( parts.length != 2 ) return;
				
				try
				{
					heightsInLevel.put( ResourceLocation.parse( parts[ 0 ] ), Integer.parseInt( parts[ 1 ] ) );
				}
				catch( RuntimeException ignored )
				{
					RailwayNetwork.LOGGER.warn( "Cannot parse config string: >>> {} <<<", s );
				}
			} );
		}
		return heightsInLevel;
	}
}
