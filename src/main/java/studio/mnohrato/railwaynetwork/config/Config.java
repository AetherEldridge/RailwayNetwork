package studio.mnohrato.railwaynetwork.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import studio.mnohrato.railwaynetwork.RailwayNetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(
	modid = RailwayNetwork.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.MOD
)
public class Config
{
	private static final ForgeConfigSpec.Builder BD = new ForgeConfigSpec.Builder( );
	private static final ForgeConfigSpec.ConfigValue< List< ? extends String > > DIMENSION_PROFILES = BD.define(
		"profiles",
		List.of( "minecraft:overworld=overworld", "minecraft:the_nether=the_nether", "minecraft:the_end=the_end" ),
		Predicators::testDimProfile
	);
	private static final ForgeConfigSpec.IntValue BLOCK_PLACING_FLAG = BD.defineInRange( "block_placing_flag", 2, 1, 127 );
	private static final ForgeConfigSpec SPEC = BD.build( );
	
	private static final Map< ResourceLocation, String > PROFILES = new HashMap<>( );
	private static final Map< String, RailwayPlacementConfigSet > CONFIG_SET = new HashMap<>( );
	
	public static int blockPlacingFlag;
	
	public static RailwayPlacementConfigSet get( ResourceLocation loc )
	{
		return CONFIG_SET.get( PROFILES.get( loc ) );
	}
	
	public static ForgeConfigSpec SPEC( )
	{
		return SPEC;
	}
	
	@SubscribeEvent
	public static void onLoad( ModConfigEvent event )
	{
		blockPlacingFlag = BLOCK_PLACING_FLAG.get( );
		
		HashMap< ResourceLocation, String > result = Parsers.parseProfile( DIMENSION_PROFILES.get( ) );
		result.forEach( ( loc, prof ) -> {
			PROFILES.put( loc, prof );
			CONFIG_SET.put( prof, Parsers.parsePlacement( prof ) );
		} );
	}
}
