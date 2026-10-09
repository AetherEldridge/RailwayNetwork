package studio.mnohrato.railwaynetwork;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.cache.TemplateCache;

@Mod.EventBusSubscriber(
	modid = RailwayNetwork.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class RWNEvents
{
	@SubscribeEvent
	public static void onWorldLoad( @NotNull LevelEvent.Load event )
	{
		if( !TemplateCache.CACHE.isEmpty() ) return;
		ServerLevel sl = ( ServerLevel )event.getLevel();
		TemplateCache.reloadCache( sl );
	}
}
