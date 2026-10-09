package studio.mnohrato.railwaynetwork.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.RailwayNetwork;
import studio.mnohrato.railwaynetwork.datagen.data.RWNRegistryGenerator;

@Mod.EventBusSubscriber(
	modid = RailwayNetwork.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.MOD
)
public class Gatherer
{
	@SubscribeEvent
	public static void onGather( @NotNull GatherDataEvent event )
	{
		DataGenerator generator = event.getGenerator( );
		PackOutput packOutput = generator.getPackOutput( );
		boolean includeServer = event.includeServer( );
		generator.addProvider( includeServer, new RWNRegistryGenerator( packOutput, event.getLookupProvider( ) ) );
	}
}
