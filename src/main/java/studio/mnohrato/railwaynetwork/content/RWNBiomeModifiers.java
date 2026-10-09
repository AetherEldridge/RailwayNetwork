package studio.mnohrato.railwaynetwork.content;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.RailwayNetwork;

public class RWNBiomeModifiers
{
	
	public static void biomeModifierBootstrap( @NotNull BootstapContext< BiomeModifier > context )
	{
		HolderGetter< Biome > biomes = context.lookup( Registries.BIOME );
		HolderGetter< PlacedFeature > placed = context.lookup( Registries.PLACED_FEATURE );
		
		context.register(
			ResourceKey.create( ForgeRegistries.Keys.BIOME_MODIFIERS, RailwayNetwork.rl( "add_railways" ) ),
			new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow( BiomeTags.IS_OVERWORLD ),
				HolderSet.direct( placed.getOrThrow( RWNFeatures.NORMAL_RAILWAYS.placed( ) ) ),
				GenerationStep.Decoration.LOCAL_MODIFICATIONS
			)
		);
	}
}
