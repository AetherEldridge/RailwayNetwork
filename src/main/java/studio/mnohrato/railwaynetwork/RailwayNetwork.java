package studio.mnohrato.railwaynetwork;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import studio.mnohrato.railwaynetwork.config.ForgeTOMLConfig;
import studio.mnohrato.railwaynetwork.content.RWNBiomeModifiers;
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
		ctx.registerConfig( ModConfig.Type.COMMON, ForgeTOMLConfig.SPEC( ) , MOD_ID + "/common.toml" );
		IEventBus modEventBus = ctx.getModEventBus( );
		
		RWNBiomeModifiers.BIOME_MODIFIERS.register( modEventBus );
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
}
