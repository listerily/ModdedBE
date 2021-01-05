# ModdedBE-Android File Structure
This description was updated on **Jan 5** .It may not be corresponding with the latest source code file structure.

```
app [Module app contains user interfaces for the launcher.]
├── build.gradle
└── src
    └── main
        ├── AndroidManifest.xml [Manifest for the app itself. There are more activities and permissions registered in the Manifest of endercore.]
        ├── ic_launcher-playstore.png
        ├── java
        │   └── net
        │       └── listerily
        │           └── moddedbe
        │               ├── FatalActivity.java [Activity shown when a fatal error occurred.Details for the error will be collected in this page.]
        │               ├── InitializingActivity.java [Activity shown when the game is initializing.MainActivity -> InitializingActivity -> AgentMainActivity]
        │               ├── MainActivity.java [Main page.]
        │               ├── ManageNModsActivity.java [Activity for managing nmods.Not completed yet.]
        │               ├── MyApplication.java [Endercore is registered here.]
        │               ├── OptionsActivity.java [Activity for options.In options, users could set launcher settings and nmods settings.]
        │               └── ui
        │                   └── LoadingView.java [An custom android progressing view.Shown in the Initializing Activity.]
        └── res [App resources]
            ├── drawable
            │   ├── activity_background.png
            │   ├── ic_launcher_background.xml
            │   ├── ic_logo.xml
            │   ├── repeat_activity_background.xml
            │   ├── text_background_selector.xml
            │   └── text_selector.xml
            ├── drawable-v24
            │   └── ic_launcher_foreground.xml
            ├── layout
            │   ├── activity_fatal.xml
            │   ├── activity_initializing.xml
            │   ├── activity_main.xml
            │   ├── activity_manage_nmods.xml
            │   └── activity_options.xml
            ├── mipmap-anydpi-v26
            │   ├── ic_launcher_round.xml
            │   └── ic_launcher.xml
            ├── mipmap-hdpi
            │   ├── ic_launcher_background.png
            │   ├── ic_launcher_foreground.png
            │   ├── ic_launcher.png
            │   └── ic_launcher_round.png
            ├── mipmap-mdpi
            │   ├── ic_launcher_background.png
            │   ├── ic_launcher_foreground.png
            │   ├── ic_launcher.png
            │   └── ic_launcher_round.png
            ├── mipmap-xhdpi
            │   ├── ic_launcher_background.png
            │   ├── ic_launcher_foreground.png
            │   ├── ic_launcher.png
            │   └── ic_launcher_round.png
            ├── mipmap-xxhdpi
            │   ├── ic_launcher_background.png
            │   ├── ic_launcher_foreground.png
            │   ├── ic_launcher.png
            │   └── ic_launcher_round.png
            ├── mipmap-xxxhdpi
            │   ├── ic_launcher_background.png
            │   ├── ic_launcher_foreground.png
            │   ├── ic_launcher.png
            │   └── ic_launcher_round.png
            ├── values
            │   ├── strings.xml
            │   └── styles.xml
            ├── values-ru
            │   └── strings.xml
            ├── values-zh
            │   └── strings.xml
            ├── values-zh-rCN
            │   └── strings.xml
            └── xml
                └── root_preferences.xml
endercore
├── build.gradle
├── consumer-rules.pro
├── proguard-rules.pro
└── src
    └── main
        ├── AndroidManifest.xml [Launcher activities and basic permissions for the game are registered here.]
        ├── assets
        │   └── endercore
        │       └── android
        │           ├── AgentMainActivity.dex [This activity is compiled in advance (See AgentMainActivity if you're interested in it.). It extends to com.mojang.minecraftpe.MainActivity and it is designed for patching assets and overriding Activity.getAssets().]
        │           └── CrackedLicense.dex [This class is compiled in advance. The source code for it is a copy of Minecraft app source code and we made the method isLicensed() returns true.]
        ├── cpp [Source code for endercore native library.This library does nothing yet.]
        │   ├── CMakeLists.txt
        │   └── endercore.cpp
        ├── java
        │   └── net
        │       └── listerily
        │           └── endercore
        │               └── android
        │                   ├── EnderCore.java [Main class for EnderCore. The initialization method is defined here. Developers could get Launcher,EnderCoreOptions,GamePackageManager,NModManager from this class.]
        │                   ├── EnderCoreOptions.java [Options for EnderCore.]
        │                   ├── exception [Exceptions to be thrown when there are errors.]
        │                   │   ├── EnderCoreException.java
        │                   │   ├── LauncherException.java
        │                   │   ├── NModException.java
        │                   │   └── NModWarning.java
        │                   ├── nmod
        │                   │   ├── NMod.java [Object for installed NMod.]
        │                   │   ├── NModOptions.java [NModOptions is used to save the data of NModManager.]
        │                   │   ├── NModPackage.java [Object for NMod packages.]
        │                   │   └── operator [Operators during the NMod patching process.They're not done yet.There are not designed for developers.]
        │                   │       ├── FileOverrider.java
        │                   │       ├── I18nReader.java
        │                   │       ├── JsonOverrider.java
        │                   │       └── TextOverrider.java
        │                   ├── operator [Operators for Developers.]
        │                   │   ├── FileManager.java [Tells us where a certain file is located or should be located.]
        │                   │   ├── GamePackageManager.java [Tells us the game info.(Versions, library locations, package locations, etc.)
        │                   │   ├── Launcher.java [For launchering the game.Developers should first invoke Launcher.initializeGame (Copying and extracting game files into out private dir) then invoke Launcher.launchGame (start the AgentMainActivity).]
        │                   │   └── NModManager.java [For NMods management (Install,uninstall,enable,disable,patch order).]
        │                   └── utils
        │                       ├── CPUArch.java [Tells us the cpu arch of this device.]
        │                       ├── FileUtils.java [Copy,cut,paste and move.]
        │                       ├── NModData.java [GSONBeans]
        │                       └── Patcher.java [Patching methods for dex, assets and native libraries.]
        ├── jniLibs [Libraries for hook methods.]
        │   ├── arm64-v8a
        │   │   ├── libsubstrate.so
        │   │   └── libyurai.so
        │   ├── armeabi
        │   │   └── libsubstrate.so
        │   ├── armeabi-v7a
        │   │   ├── libsubstrate.so
        │   │   └── libyurai.so
        │   ├── x86
        │   │   ├── libsubstrate.so
        │   │   └── libyurai.so
        │   └── x86_64
        │       ├── libsubstrate.so
        │       └── libyurai.so
        └── res
            ├── drawable
            │   └── icon.png
            ├── drawable-ldpi
            │   ├── icon.png
            │   └── msa_ms_logo.png
            ├── values
            │   ├── colors.xml
            │   ├── strings.xml
            │   └── styles.xml
            └── xml
                └── splits0.xml

```
