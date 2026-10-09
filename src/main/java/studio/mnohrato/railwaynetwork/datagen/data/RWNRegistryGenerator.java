package studio.mnohrato.railwaynetwork.datagen.data;

import com.simibubi.create.infrastructure.worldgen.AllBiomeModifiers;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import studio.mnohrato.railwaynetwork.RailwayNetwork;
import studio.mnohrato.railwaynetwork.content.RWNBiomeModifiers;
import studio.mnohrato.railwaynetwork.content.RWNFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RWNRegistryGenerator extends DatapackBuiltinEntriesProvider
{
	public static final RegistrySetBuilder BUILDER;
	
	static
	{
		BUILDER = new RegistrySetBuilder( );
		BUILDER.add( Registries.CONFIGURED_FEATURE, RWNFeatures::configuredFeatureBootstrap );
		BUILDER.add( Registries.PLACED_FEATURE, RWNFeatures::placedFeatureBootstrap );
		BUILDER.add( ForgeRegistries.Keys.BIOME_MODIFIERS, RWNBiomeModifiers::biomeModifierBootstrap );
	}
	
	public RWNRegistryGenerator( PackOutput output, CompletableFuture< HolderLookup.Provider > registries )
	{
		super( output, registries, BUILDER, Set.of( RailwayNetwork.MOD_ID ) );
	}
}
