package studio.mnohrato.railwaynetwork.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import studio.mnohrato.railwaynetwork.RailwayNetwork;

import java.util.List;

@Mod.EventBusSubscriber(
	modid = RailwayNetwork.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.MOD
)
public class Config
{
	public static final ForgeConfigSpec.Builder BD;
	public static final ForgeConfigSpec.ConfigValue< List< ? extends String > > DIM_HEIGHT;
	public static final ForgeConfigSpec.IntValue X_CYCLE;
	public static final ForgeConfigSpec.IntValue X_OFFSET;
	public static final ForgeConfigSpec.ConfigValue< List< ? extends Integer > > X_AT;
	public static final ForgeConfigSpec.IntValue Z_CYCLE;
	public static final ForgeConfigSpec.IntValue Z_OFFSET;
	public static final ForgeConfigSpec.ConfigValue< List< ? extends Integer > > Z_AT;
	public static final ForgeConfigSpec SPEC;
	
	static
	{
		BD = new ForgeConfigSpec.Builder( );
		BD.push( "generation" );
		{
			DIM_HEIGHT = BD.define( "height", List.of( "minecraft:overworld=64" ), Predicators::testDimHeight );
			BD.push( "x_direction" );
			{
				X_CYCLE = BD.defineInRange( "cycle_in_chunk", 64, 0, Integer.MAX_VALUE );
				X_OFFSET = BD.defineInRange( "offset_in_block", 0, -16, 16 );
				X_AT = BD.defineList( "at_chunk", List.of( 30, 20, 50 ), Predicators::testPositiveInt );
			}
			BD.pop( );
			BD.push( "z_direction" );
			{
				Z_CYCLE = BD.defineInRange( "cycle_in_chunk", 100, 0, Integer.MAX_VALUE );
				Z_OFFSET = BD.defineInRange( "offset_in_block", 0, -16, 16 );
				Z_AT = BD.defineList( "at_chunk", List.of( 70, 90, 91 ), Predicators::testPositiveInt );
			}
			BD.pop( );
		}
		BD.pop( );
		SPEC = BD.build( );
	}
	
	public static ForgeConfigSpec SPEC( )
	{
		return SPEC;
	}
	
	public static RailwayGenerationConfig rwpc;
	public static RailwayPlacementConfig rwmcX;
	public static RailwayPlacementConfig rwmcZ;
	public static RailwayPlacementConfig rwmcC;
	
	@SubscribeEvent
	public static void onLoad( ModConfigEvent event )
	{
		rwpc = new RailwayGenerationConfig(
			X_OFFSET.get( ),
			Z_OFFSET.get( ),
			X_CYCLE.get( ),
			Z_CYCLE.get( ),
			ImmutableList.copyOf( X_AT.get( ) ),
			ImmutableList.copyOf( Z_AT.get( ) ),
			ImmutableMap.copyOf( Parsers.parseResourceLocationInteger( DIM_HEIGHT.get( ) ) )
		);
		
		rwmcX = Parsers.parsePlacement( "rail_x" );
		rwmcZ = Parsers.parsePlacement( "rail_z" );
		rwmcC = Parsers.parsePlacement( "rail_c" );
	}
	
}
