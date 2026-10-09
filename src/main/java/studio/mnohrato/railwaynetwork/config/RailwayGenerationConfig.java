package studio.mnohrato.railwaynetwork.config;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public record RailwayGenerationConfig(
	int xOff, int zOff, int xCycle, int zCycle, List< Integer > xChunkPos, List< Integer > zChunkPos, Map< ResourceLocation, Integer > levelHeightMap
)
{ }
