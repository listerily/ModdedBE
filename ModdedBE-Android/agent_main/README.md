## AgentMainActivity

This is the source code for `AgentMainActivity.dex`. This class is used for patching AssetManager and Resources for Minecraft Game.  
This project is only compiled to class files by default. If you want to **re**generate the dex file, you need to use the build tool `dx` in Android SDK.  

### Guide for Windows
Build `app`, run the following commands, and build again.  
`BUILD_TOOLS_VERSION` should be as same as `buildToolsVersion` in `build.gradle`.  
```bat
SET SDK_DIR=path\to\android-sdk
SET ROOT_DIR=path\to\ModdedBE-Android
SET BUILD_TOOLS_VERSION=30.0.3

"%SDK_DIR%\build-tools\%BUILD_TOOLS_VERSION%\dx.bat" --dex --no-strict --output "%ROOT_DIR%\endercore\src\main\assets\endercore\android\AgentMainActivity.dex" "%ROOT_DIR%\agent_main\build\intermediates\javac\debug\classes\com\mojang\minecraftpe\AgentMainActivity.class"
```
