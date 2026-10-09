package studio.mnohrato.railwaynetwork.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import studio.mnohrato.railwaynetwork.RailwayNetwork;
import studio.mnohrato.railwaynetwork.modifiers.NetworkPlacementModifiers;

public class RWNPlacementModifiers
{
	public static final DeferredRegister< PlacementModifierType< ? > > PLACEMENT_MODIFIERS = DeferredRegister.create( Registries.PLACEMENT_MODIFIER_TYPE, RailwayNetwork.MOD_ID );
	public static final RegistryObject< PlacementModifierType< NetworkPlacementModifiers > > NETWORK_PLACEMENT = PLACEMENT_MODIFIERS.register(
		"network",
		( ) -> ( ) -> NetworkPlacementModifiers.CODEC
	);
}
