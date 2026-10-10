# Mnohrato 的铁路网络

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green)
![Loader](https://img.shields.io/badge/Forge-47.4.0-orange)
![License](https://img.shields.io/badge/License-GPLv3+-yellow)

Read in [English](README.md)

## 简介

此模组向 Minecraft 中添加了铁路网络，能让玩家在没有传送的情况下，能够优雅而便捷地长途旅行。

## 快速开始

1. 将模组 Jar 放入 `mods/` 文件夹
2. 启动游戏，模组会自动生成默认配置
3. 默认配置下即可正常游玩，无需任何额外设置

> 虽然但是，目前这个模组还没有发 Release 的打算，所以可能需要麻烦大家下载源码来编译一下了
>
> (^人^)

## 兼容性

- 此模组使用 Forge 47.4.0 与 Minecraft 1.20.1 编写
- 需要前置模组 Create 6.0.8
- 版本相近的相关依赖项虽然理应能够正常运行，但不保证能避免一切严重问题。
- 暂时没有发现不兼容模组

## 构建

1. 使用 Git 克隆此仓库或下载源代码解压
2. 使用 IntelliJ IDEA 打开
3. 导入完成后，运行

```bash
gradlew build
```

4. 项目生成的 `build` 目录下，可以找到生成的 Jar

## 版权

- 本项目使用 GNU 通用公共许可证第三版及更高版本开源，详情请阅读 [LICENSE](LICENSE) 文件。
- 对于整合包作者，您可以自由地将此模组放入您的整合包中，除许可证规定的行为外，无需将整合包以相同许可证开源。

## 配置说明

此模组首次运行时将生成这样的配置结构：
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
`common.toml` 是基本的维度生成描述，而其余每个文件夹都描述了一个维度如何生成和生成什么样式的铁路网络。

### `common.toml` 文件

其内容应该是这样的：
```toml
profiles = ["minecraft:overworld=overworld", "minecraft:the_nether=the_nether", "minecraft:the_end=the_end"]
#Range: 1 ~ 127
block_placing_flag = 2
```

`profiles` 键存储了维度 ID 和样式文件名的对应关系。
每一个键都应该有 `namespace:dimension=profile` 的格式，它表示
> 维度 `namespace:dimension` 使用 `profile` 文件夹下定义的样式

`block_placing_flag` 键表示放置铁路往结构的方块时，需要更改哪些内容，其值表示要下列更改的内容的常量值的和。
```java
// 这些是独立的值
public static final int UPDATE_NEIGHBORS = 1;
public static final int UPDATE_CLIENTS = 2;
public static final int UPDATE_INVISIBLE = 4;
public static final int UPDATE_IMMEDIATE = 8;
public static final int UPDATE_KNOWN_SHAPE = 16;
public static final int UPDATE_SUPPRESS_DROPS = 32;
public static final int UPDATE_MOVE_BY_PISTON = 64;
// 这些是常用值的组合
public static final int UPDATE_NONE = 4;
public static final int UPDATE_ALL = 3;
public static final int UPDATE_ALL_IMMEDIATE = 11;
```
它会影响周围方块的行为，进而导致性能差异，如果您实在无法理解，可以问问 AI，或者直接不改这个值。

### 样式文件夹

每个样式文件夹下包含五个文件，其中 `rail_c.json` `rail_x.json` `rail_z.json` 定义了铁路本身的内容；`pier.json` 描述桥墩，而 `gen.json` 表示生成规则。

您也可以自己新建文件夹，添加这五个文件并定义自己的铁路。公路桥，蓝冰滑道之类，不一定有铁轨的网络也是可以的，只要您不觉得破坏游戏平衡性即可。

> 注意，`overworld`, `the_nether`, `the_end` 默认指代原版维度，有可能被默认配置覆盖，因此推荐您避免使用这三个名称。


#### `gen.json`

```json5
{
  // 固定生成的高度，单位：方块
  "y": 70,
  // 生成的具体区块位置，在 X，Z 方向上，要在哪些位置生成一条铁路，单位：区块
  "x_chunk_pos": [ 300, 200, 500 ],
  "z_chunk_pos": [ 700, 900, 910 ],
  // 上述位置以此处的值为周期反复生成，单位：区块
  "x_cycle": 640,
  "z_cycle": 1000,
  // 生成时的偏移，此值为 0 时，每一节铁路将被严格按照区块边界生成，单位：方块
  "x_off": 0,
  "z_off": 0,
}
```

### `pier.json`

```json5
{
  // 桥墩之间的最大生成间隔，单位：区块
  "max_distance": 6,
  // 桥墩类型，
  // PILLAR 即为 6x6 的柱子；
  // FRUSTUM 和 PILLAR 相似，但上端稍小；
  // FLOATER 很短的桥墩，如果是设定上没有重力的维度，可以当作浮力器；
  // NONE 不生成桥墩；
  "pier_type": "PILLAR",
  // 材料表，此处的键值对中，键表示一个方块状态，值表示权重，使用的方块将在此表中随机选取。
  "material": {
    "minecraft:polished_andesite": 10,
    "minecraft:andesite": 1
  }
}
```

### `rail_c.json` `rail_x.json` `rail_z.json`

它们的结构相同：
```json5
{
  // 调色板，定义了每个字符对应了什么样的材料，含义和上方桥墩的定义相同。
  "palette": {
    "A": {
      "minecraft:polished_andesite_slab[type=top]": 10,
      "minecraft:andesite_slab[type=top]": 1
    },
    // ...
    // 注：定义结构空位时，表示这个位置原有的方块不作替换。
    "V": {
      "minecraft:structure_void": 1
    }
  },
  // 切片，每个 16x16 的字符矩阵表示每一节轨道相同 Y 坐标的一层，字符表示上方所定义的方块材料
  // 从下到上排列。空矩阵表示一整层都是空气。
  // 注：我们暂时无法提供转换结构为切片的方法，因此此处切片需要手写。
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
    [],
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