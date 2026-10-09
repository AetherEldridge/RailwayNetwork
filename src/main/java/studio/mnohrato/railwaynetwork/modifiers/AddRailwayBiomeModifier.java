package studio.mnohrato.railwaynetwork.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import studio.mnohrato.railwaynetwork.content.RWNBiomeModifiers;

public record AddRailwayBiomeModifier( HolderSet< PlacedFeature > features, GenerationStep.Decoration step ) implements BiomeModifier
{
	public static final Codec< AddRailwayBiomeModifier > CODEC = RecordCodecBuilder.create( builder -> builder
		.group( PlacedFeature.LIST_CODEC.fieldOf( "features" ).forGetter( i -> i.features ), GenerationStep.Decoration.CODEC.fieldOf( "step" ).forGetter( i -> i.step ) )
		.apply( builder, AddRailwayBiomeModifier::new ) );
	
	@Override
	public void modify( Holder< Biome > biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder )
	{
		if( phase == Phase.ADD )
		{
			BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings( );
			this.features.forEach( holder -> generationSettings.addFeature( this.step, holder ) );
		}
	}
	
	@NotNull
	@Override
	public Codec< ? extends BiomeModifier > codec( )
	{
		return RWNBiomeModifiers.ADD_RAILWAYS.get( );
	}
} 