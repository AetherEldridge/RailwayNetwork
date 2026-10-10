# Mnohrato's Railway Network

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green)
![Loader](https://img.shields.io/badge/Forge-47.4.0-orange)
![License](https://img.shields.io/badge/License-GPLv3+-yellow)

以[中文](README_zh.md)阅读

## Introduction

This mod adds a railway network to Minecraft, allowing players to travel long distances smoothly and conveniently
without teleporting.

## Quick Start

1. Put the mod Jar into the `mods/` folder
2. Launch the game, and the mod will automatically generate the default configuration
3. You can play normally with the default configuration, no extra setup needed

> However, at the moment, this mod doesn’t have plans for a Release version, so you might have to go through the hassle
> of downloading the source code and compiling it yourself
>
> (^人^)

## Compatibility

- This mod is made with Forge 47.4.0 and Minecraft 1.20.1
- Requires the mod Create 6.0.8
- Related dependencies with similar versions should work fine, but we can't guarantee it'll avoid all serious issues.
- No incompatible mods have been found so far

## Build

1. Clone this repository using Git or download the source code and unzip it
2. Open it with IntelliJ IDEA
3. Once imported, run

```bash
gradlew build
```

4. You can find the generated Jar in the `build` directory of the project

## License

- This project is open-source under the GNU General Public License version 3 or later. For details, please read
  the [LICENSE](LICENSE) file.
- For modpack creators, you are free to include this mod in your pack. Other than the actions specified by the license,
  you don’t need to open-source your modpack under the same license.

## Configuration Instructions

This module will generate a configuration structure like this the first time it runs:

```text
railwaynetwork
│  common.toml
│  
├─overworld
│      gen.json
│      pier.json
│      rail_c.json
│      rail_x.json
│      rail_z.json
│      
├─the_end
│      gen.json
│      pier.json
│      rail_c.json
│      rail_x.json
│      rail_z.json
│      
└─the_nether
        gen.json
        pier.json
        rail_c.json
        rail_x.json
        rail_z.json
```

`common.toml` is the basic description for dimension generation, while each of the other folders describes how a dimension is generated and what style of railway network it produces.

### `common.toml` File

Its content should look like this:
```toml
profiles = ["minecraft:overworld=overworld", "minecraft:the_nether=the_nether", "minecraft:the_end=the_end"]
#Range: 1 ~ 127
block_placing_flag = 2
```

The `profiles` key stores the mapping between dimension IDs and style file names. Each entry should be in the format `namespace:dimension=profile`, which means:
> Dimension `namespace:dimension` uses the style defined in the `profile` folder

The `block_placing_flag` key indicates which aspects should change when placing rail blocks in structures. Its value is the sum of constants representing the changes below:
```java
// These are individual values
public static final int UPDATE_NEIGHBORS = 1;
public static final int UPDATE_CLIENTS = 2;
public static final int UPDATE_INVISIBLE = 4;
public static final int UPDATE_IMMEDIATE = 8;
public static final int UPDATE_KNOWN_SHAPE = 16;
public static final int UPDATE_SUPPRESS_DROPS = 32;
public static final int UPDATE_MOVE_BY_PISTON = 64;
// These are common value combinations
public static final int UPDATE_NONE = 4;
public static final int UPDATE_ALL = 3;
public static final int UPDATE_ALL_IMMEDIATE = 11;
```
It affects how neighboring blocks behave, which can impact performance. If you really don’t understand it, you can ask an AI, or just leave this value as it is.

### Style Folder

Each style folder contains five files, where `rail_c.json`, `rail_x.json`, and `rail_z.json` define the railway itself; `pier.json` describes the bridge piers, and `gen.json` represents the generation rules.

You can also create your own folder, add these five files, and define your own railway. Roads, blue ice tracks, etc., don’t necessarily need a rail network, as long as you don’t think it breaks the game balance.

> Note, `overworld`, `the_nether`, and `the_end` refer to the default game dimensions and may be overridden by default configurations, so it’s recommended to avoid using these three names.

#### `gen.json`

```json5
{
  // Fixed generation height, in blocks
  "y": 70,
  // Specific chunk positions for generation, indicating at which X and Z positions a railway will be generated, in chunks
  "x_chunk_pos": [ 300, 200, 500 ],
  "z_chunk_pos": [ 700, 900, 910 ],
  // The above positions are repeated with the cycle defined here, in chunks
  "x_cycle": 640,
  "z_cycle": 1000,
  // Offset for generation; when this value is 0, each railway segment will be generated exactly along chunk borders, in blocks
  "x_off": 0,
  "z_off": 0,
}
```

### `pier.json`

```json5
{
  // Maximum spacing between bridge piers, in blocks
  "max_distance": 6,
  // Type of bridge pier,
  // PILLAR is a 6x6 column;
  // FRUSTUM is similar to PILLAR but slightly smaller at the top;
  // FLOATER is a very short pier, which can act as a floatation device in dimensions without gravity;
  // NONE means no bridge piers will be generated;
  "pier_type": "PILLAR",
  // Material list, where the key is a block state and the value is its weight; the blocks used will be randomly selected from this list.
  "material": {
    "minecraft:polished_andesite": 10,
    "minecraft:andesite": 1
  }
}
```

### `rail_c.json` `rail_x.json` and `rail_z.json`

They have the same structure:
```json5
{
  // Palette, defines what material each character corresponds to, similar to the definition of the bridge piers above.
  "palette": {
    "A": {
      "minecraft:polished_andesite_slab[type=top]": 10,
      "minecraft:andesite_slab[type=top]": 1
    },
    // ...
    // Note: When defining a structure void, it means the block originally at that position is not replaced.
    "V": {
      "minecraft:structure_void": 1
    }
  },
  // Slices, each 16x16 character matrix represents a layer at the same Y coordinate of each rail section, 
  // with the characters representing the block materials defined above.
  // Arranged from bottom to top. The empty matrix represents that all the blocks in this layer is air.
  // Note: We currently can't provide a method to convert structures into slices, so these slices need to be written manually.
  "slice": [
    [
      "  AA########AA  ",
      "  AA########AA  ",
      "AAAA########AAAA",
      "AAAA########AAAA",
      "################",
      "################",
      "################",
      "################",
      "################",
      "################",
      "################",
      "################",
      "AAAA########AAAA",
      "AAAA########AAAA",
      "  AA########AA  ",
      "  AA########AA  "
    ],
    // ...
    [ ],
    [
      "V              V",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "                ",
      "V              V"
    ]
  ]
}
```

