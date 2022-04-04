# 我对 ModdedBE 未来的构想
以下是我对 ModdedBE 未来的构想，希望可以实现。  

ModdedBE 应先实现**启动原版游戏**的功能，然后是才是**加载各种模组（包括 NMod）**。  
ModdedBE 的发展方向有待商榷，因为 Horizon 在模组方面目前已经做的很好了，而且 listerily 目前不活跃。  

## 框架
EnderCore 是启动器的逻辑核心，将来可以不仅仅可以用来启动 Minecraft，还可以用来启动其它游戏（如：世界征服者4）。EnderCore 主要分为以下三部分：  
 - `Manager`（名称待定，备选名称 `Keeper`）：管理游戏包和各种模组包。
 - `Importer`（备选名称 `Cacher`）：解压缩游戏包和各种模组包到缓存中，并按照 NMod 中的配置对游戏的各种资源文件【普通文本（如 `lang`）、`json`、`xml`】打补丁。
 - `Loader`（备选名称 `Launcher`）：把缓存中的所有内容加载到内存中。

ModdedBE 是调用 EnderCore 的应用程序。  

## 启动原版游戏
 - 管理：`GameManager` 负责读取游戏安装包（支持 apks），`GameListManager`（备选名称 `GameRepoManager`）负责管理多版本。
 - 缓存：由于我们仅仅启动原版游戏，所以可以仅提取 dex 和 so 文件，assets、res 不用管，直接把所有安装包加入到 assetsPath 中。（当然，也可以解压缩整个安装包，见下。）
 - 启动：动态加载解压出来的 dex 和 so 文件，然后调用 `AgentMainActivity` 来启动游戏。

## 加载各种模组
模组有很多，以下按照时间顺序列出，可能不完全。实现起来可以先易而后难。  
 - PTPMod
 - ModPE
 - BlockLauncher 插件
 - ICMod
 - Inner Core 原生模组
 - NMod
 - 新版 NMod（名称待定）（全新的标准，支持多平台）

以下以加载 NMod 为例：  
 - 管理：`NModManager` 负责读取单个 NMod 包，`GameListManager`（备选名称 `NModRepoManager`）负责管理 NMod 列表。
 - 缓存：dex（如果有）和 so 的缓存比较简单。关键在于 assets 修改：我们需要 `Overrider` 这种东西来实现普通文本的前插后插、json 的合并等复杂功能，这也意味着我们需要解压缩安装包。
 - 启动：`Loader` 会一股脑加载所有文件，所以我们不需要多管，交给它就好了。

目前 ModdedBE 仅支持 zip 格式的 NMod。我希望这一点能恢复到当初的 apk/zip 的形式，因为 apk 便于调试。  
