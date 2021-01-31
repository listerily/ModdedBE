## AgentMainActivity

This is the source code for `AgentMainActivity.dex`. This class is used for patching AssetManager and Resources for Minecraft Game.  
This project is only compiled to class files by default. If you want to **re**generate the dex file, you need to use the build tool `dx` in Android SDK.  

### Tutorial for Windows
Build app, run the following commands, and build again.  
```
set SDK_DIR=path\to\android\sdk
set ROOT_DIR=path\to\ModdedBE-Android

%SDK_DIR%\build-tools\30.0.3\dx.bat --dex --no-strict --output %ROOT_DIR%\endercore\src\main\assets\endercore\android\AgentMainActivity.dex %ROOT_DIR%\agent_main\build\intermediates\javac\debug\classes\com\mojang\minecraftpe\AgentMainActivity.class
```
