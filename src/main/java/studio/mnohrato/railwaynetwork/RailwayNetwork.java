package studio.mnohrato.railwaynetwork;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import studio.mnohrato.railwaynetwork.cache.TemplateCache;
import studio.mnohrato.railwaynetwork.config.Config;
import studio.mnohrato.railwaynetwork.content.RWNFeatures;
import studio.mnohrato.railwaynetwork.content.RWNPlacementModifiers;

@Mod(
	value = RailwayNetwork.MOD_ID
)
public class RailwayNetwork
{
	public static final String MOD_ID = "railwaynetwork";
	public static final Logger LOGGER = LogUtils.getLogger( );
	
	public RailwayNetwork( @NotNull FMLJavaModLoadingContext ctx )
	{
		ctx.registerConfig( ModConfig.Type.COMMON, Config.SPEC( ) , MOD_ID + "/common.toml" );
		IEventBus modEventBus = ctx.getModEventBus( );
		
		RWNPlacementModifiers.PLACEMENT_MODIFIERS.register( modEventBus );
		RWNFeatures.FEATURES.register( modEventBus );
	}
	
	@NotNull
	@Contract(
		value = "_ -> new"
	)
	public static ResourceLocation rl( String s )
	{
		return ResourceLocation.fromNamespaceAndPath( MOD_ID , s );
	}
	
	@NotNull
	@Contract(
		value = "-> new"
	)
	public static ResourceLocation emptyRl( )
	{
		return rl( "empty" );
	}
}
