package net.listerily.nmodder_android.launcher;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LauncherOptions {
    private Launcher launcher;
    private OptionsBean optionsData;
    public LauncherOptions(Launcher launcher) throws IOException
    {
        this.launcher = launcher;

        loadFromFile();
    }

    private void loadFromFile() throws IOException
    {
        File dataDir = launcher.getContext().getDir(Launcher.DIR_ROOT,0);
        File optionsFile = new File(dataDir,Launcher.FILE_OPTIONS);
        Gson gson = new Gson();
        if(!optionsFile.exists())
            createNewFile();
        optionsData = gson.fromJson(new InputStreamReader(new FileInputStream(optionsFile)),OptionsBean.class);
    }

    private void createNewFile() throws IOException
    {
        File dataDir = launcher.getContext().getDir(Launcher.DIR_ROOT,0);
        File optionsFile = new File(dataDir,Launcher.FILE_OPTIONS);
        Gson gson = new Gson();
        optionsFile.createNewFile();
        gson.toJson(new OptionsBean(),new OutputStreamWriter(new FileOutputStream(optionsFile)));
    }


    private void saveFile() throws IOException
    {
        File dataDir = launcher.getContext().getDir(Launcher.DIR_ROOT,0);
        File optionsFile = new File(dataDir,Launcher.FILE_OPTIONS);
        Gson gson = new Gson();
        gson.toJson(new OptionsBean(),new OutputStreamWriter(new FileOutputStream(optionsFile)));
    }

    public void setSafeMode(boolean safeMode) {
        optionsData.safe_mode = safeMode;
        try {
            saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPackageName(String packageName) {
        optionsData.game_package_name = packageName;
        try {
            saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPackageName() {
        return optionsData.game_package_name;
    }

    public boolean isSafeMode() {
        return optionsData.safe_mode;
    }

    private final static class OptionsBean
    {
        boolean safe_mode = false;
        String game_package_name = GameManager.PACKAGE_NAME;
    }
}
