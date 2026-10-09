package studio.mnohrato.railwaynetwork.content;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.RailwayNetwork;
import studio.mnohrato.railwaynetwork.feature.RailwayFeature;
import studio.mnohrato.railwaynetwork.modifiers.NetworkPlacementModifiers;

import java.util.function.Supplier;

public class RWNFeatures
{
	public static final DeferredRegister< Feature< ? > > FEATURES = DeferredRegister.create( ForgeRegistries.FEATURES, RailwayNetwork.MOD_ID );
	
	public static final FeatureSets< NoneFeatureConfiguration > NORMAL_RAILWAYS = register( "railways", ( ) -> new RailwayFeature( NoneFeatureConfiguration.CODEC ) );
	
	public record FeatureSets< FC extends FeatureConfiguration >(
		ResourceKey< PlacedFeature > placed, ResourceKey< ConfiguredFeature< ?, ? > > configured, RegistryObject< Feature< FC > > object
	)
	{ }
	
	public static void placedFeatureBootstrap( @NotNull BootstapContext< PlacedFeature > context )
	{
		HolderGetter< ConfiguredFeature< ?, ? > > features = context.lookup( Registries.CONFIGURED_FEATURE );
		
		PlacementUtils.register( context, NORMAL_RAILWAYS.placed( ), features.getOrThrow( NORMAL_RAILWAYS.configured( ) ), new NetworkPlacementModifiers( ) );
	}
	
	public static void configuredFeatureBootstrap( @NotNull BootstapContext< ConfiguredFeature< ?, ? > > context )
	{
		FeatureUtils.register( context, NORMAL_RAILWAYS.configured( ), NORMAL_RAILWAYS.object( ).get( ), NoneFeatureConfiguration.INSTANCE );
	}
	
	@NotNull
	@Contract(
		value = "_, _ -> new"
	)
	public static < FC extends FeatureConfiguration > RWNFeatures.FeatureSets< FC > register( String name, Supplier< Feature< FC > > supplier )
	{
		return register( name, name, name, supplier );
	}
	
	@NotNull
	@Contract(
		value = "_, _, _ -> new"
	)
	public static < FC extends FeatureConfiguration > RWNFeatures.FeatureSets< FC > register( String objectName, String keyName, Supplier< Feature< FC > > supplier )
	{
		return register( objectName, keyName, keyName, supplier );
	}
	
	@NotNull
	@Contract(
		value = "_, _, _, _ -> new"
	)
	public static < FC extends FeatureConfiguration > RWNFeatures.FeatureSets< FC > register(
		String objectName,
		String placedName,
		String configuredName,
		Supplier< Feature< FC > > supplier
	)
	{
		RegistryObject< Feature< FC > > object = FEATURES.register( objectName, supplier );
		ResourceKey< PlacedFeature > placed = ResourceKey.create( Registries.PLACED_FEATURE, RailwayNetwork.rl( placedName ) );
		ResourceKey< ConfiguredFeature< ?, ? > > configured = ResourceKey.create( Registries.CONFIGURED_FEATURE, RailwayNetwork.rl( configuredName ) );
		return new FeatureSets<>( placed, configured, object );
	}
}
