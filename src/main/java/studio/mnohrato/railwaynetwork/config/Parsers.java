package studio.mnohrato.railwaynetwork.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mnohrato.railwaynetwork.RailwayNetwork;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;

public class Parsers
{
	@NotNull
	public static JSONConfig parsePlacement( String levelName )
	{
		JSONConfig.Bridge x = Parsers.parseBridge( levelName, "rail_x" );
		JSONConfig.Bridge z = Parsers.parseBridge( levelName, "rail_z" );
		JSONConfig.Bridge c = Parsers.parseBridge( levelName, "rail_c" );
		JSONConfig.Pier p = Parsers.parsePier( levelName );
		JSONConfig.WorldGen g = Parsers.parseWorldGen( levelName );
		
		return new JSONConfig( x, z, c, p, g );
	}
	
	@Nullable
	public static JSONConfig.Bridge parseBridge( String levelName, String fileName )
	{
		String target = "%s/%s/%s/%s.json".formatted( FMLPaths.CONFIGDIR.get( ).toString( ), RailwayNetwork.MOD_ID, levelName, fileName );
		String resource = "/data/%s/%s/%s/%s.json".formatted( RailwayNetwork.MOD_ID, RailwayNetwork.MOD_ID, levelName, fileName );
		try
		{
			JsonElement je = new Gson( ).fromJson( new FileReader( target ), JsonElement.class );
			return JSONConfig.Bridge.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
		}
		catch( RuntimeException | FileNotFoundException rt )
		{
			copyDefaultConfig( resource, target );
			
			try
			{
				JsonElement je = new Gson( ).fromJson( new FileReader( target ), JsonElement.class );
				return JSONConfig.Bridge.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
			}
			catch( FileNotFoundException fnf )
			{
				RailwayNetwork.LOGGER.warn( "File not found: ", fnf );
			}
			catch( RuntimeException rrt )
			{
				RailwayNetwork.LOGGER.warn( "Failed to parse: ", rrt );
			}
			return null;
		}
	}
	
	
	@Nullable
	public static JSONConfig.WorldGen parseWorldGen( String levelName )
	{
		String target = "%s/%s/%s/gen.json".formatted( FMLPaths.CONFIGDIR.get( ).toString( ), RailwayNetwork.MOD_ID, levelName );
		String resource = "/data/%s/%s/%s/gen.json".formatted( RailwayNetwork.MOD_ID, RailwayNetwork.MOD_ID, levelName );
		try
		{
			JsonElement je = new Gson( ).fromJson( new FileReader( target ), JsonElement.class );
			return JSONConfig.WorldGen.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
		}
		catch( RuntimeException | FileNotFoundException rt )
		{
			copyDefaultConfig( resource, target );
			
			try
			{
				JsonElement je = new Gson( ).fromJson( new FileReader( target ), JsonElement.class );
				return JSONConfig.WorldGen.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
			}
			catch( FileNotFoundException fnf )
			{
				RailwayNetwork.LOGGER.warn( "File not found: ", fnf );
			}
			catch( RuntimeException rrt )
			{
				RailwayNetwork.LOGGER.warn( "Failed to parse: ", rrt );
			}
			return null;
		}
	}
	
	@Nullable
	public static JSONConfig.Pier parsePier( String levelName )
	{
		String target = "%s/%s/%s/pier.json".formatted( FMLPaths.CONFIGDIR.get( ).toString( ), RailwayNetwork.MOD_ID, levelName );
		String resource = "/data/%s/%s/%s/pier.json".formatted( RailwayNetwork.MOD_ID, RailwayNetwork.MOD_ID, levelName );
		try
		{
			JsonElement je = new Gson( ).fromJson( new FileReader( target ), JsonElement.class );
			return JSONConfig.Pier.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
		}
		catch( RuntimeException | FileNotFoundException rt )
		{
			copyDefaultConfig( resource, target );
			
			try
			{
				JsonElement je = new Gson( ).fromJson( new FileReader( target ), JsonElement.class );
				return JSONConfig.Pier.CODEC.parse( JsonOps.INSTANCE, je ).get( ).orThrow( );
			}
			catch( FileNotFoundException rnf )
			{
				RailwayNetwork.LOGGER.warn( "File not found: ", rnf );
			}
			catch( RuntimeException rrt )
			{
				RailwayNetwork.LOGGER.warn( "Failed to parse: ", rrt );
			}
			return null;
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
		catch( IOException io )
		{
			RailwayNetwork.LOGGER.warn( "Failed to copy default config: ", io );
		}
	}
	
	@NotNull
	public static HashMap< ResourceLocation, String > parseProfile( List< ? extends String > mapStrings )
	{
		HashMap< ResourceLocation, String > heightsInLevel = new HashMap<>( );
		
		if( mapStrings != null )
		{
			mapStrings.forEach( ( s ) -> {
				String[] parts = s.split( "=" );
				if( parts.length != 2 ) return;
				
				try
				{
					heightsInLevel.put( ResourceLocation.parse( parts[ 0 ] ), parts[ 1 ] );
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
