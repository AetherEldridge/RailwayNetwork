package studio.mnohrato.railwaynetwork;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(
	value = Railwaynetwork.MODID
)
public class Railwaynetwork
{
	public static final String MODID = "railwaynetwork";
	public static final Logger LOGGER = LogUtils.getLogger( );
	
	public Railwaynetwork( @NotNull FMLJavaModLoadingContext ctx )
	{
		IEventBus modEventBus = ctx.getModEventBus( );
		
		MinecraftForge.EVENT_BUS.register( this );
		
		ctx.registerConfig( ModConfig.Type.COMMON, Config.SPEC( ) );
	}
}
