package studio.mnohrato.railwaynetwork.content;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.RailwayNetwork;
import studio.mnohrato.railwaynetwork.modifiers.AddRailwayBiomeModifier;

public class RWNBiomeModifiers
{
	public static final DeferredRegister< Codec< ? extends BiomeModifier > > BIOME_MODIFIERS = DeferredRegister.create(
		ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS,
		RailwayNetwork.MOD_ID
	);
	
	public static final RegistryObject< Codec< AddRailwayBiomeModifier > > ADD_RAILWAYS = BIOME_MODIFIERS.register( "add_railways", ( ) -> AddRailwayBiomeModifier.CODEC );
	
	public static class Keys
	{
		public static final ResourceKey< BiomeModifier > ADD_RAILWAYS = ResourceKey.create( ForgeRegistries.Keys.BIOME_MODIFIERS, RailwayNetwork.rl( "add_railways" ) );
	}
	
	public static void biomeModifierBootstrap( @NotNull BootstapContext< BiomeModifier > context )
	{
		HolderGetter< PlacedFeature > placed = context.lookup( Registries.PLACED_FEATURE );
		
		context.register(
			Keys.ADD_RAILWAYS,
			new AddRailwayBiomeModifier( HolderSet.direct( placed.getOrThrow( RWNFeatures.NORMAL_RAILWAYS.placed( ) ) ), GenerationStep.Decoration.SURFACE_STRUCTURES )
		);
	}
}
