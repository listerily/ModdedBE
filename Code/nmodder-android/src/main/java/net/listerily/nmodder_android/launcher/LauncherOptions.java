package net.listerily.nmodder_android.launcher;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
        {
            createNewFile();
            optionsData = new OptionsBean();
            return;
        }
        optionsData = gson.fromJson(new InputStreamReader(new FileInputStream(optionsFile)),OptionsBean.class);
        if(optionsData == null)
        {
            createNewFile();
            optionsData = new OptionsBean();
        }
    }

    private void createNewFile() throws IOException
    {
        File dataDir = launcher.getContext().getDir(Launcher.DIR_ROOT,0);
        File optionsFile = new File(dataDir,Launcher.FILE_OPTIONS);
        Gson gson = new Gson();
        optionsFile.getParentFile().mkdirs();
        optionsFile.createNewFile();
        new FileOutputStream(optionsFile).write(gson.toJson(new OptionsBean()).getBytes());
    }


    private void saveFile() throws IOException
    {
        File dataDir = launcher.getContext().getDir(Launcher.DIR_ROOT,0);
        File optionsFile = new File(dataDir,Launcher.FILE_OPTIONS);
        Gson gson = new Gson();
        new FileOutputStream(optionsFile).write(gson.toJson(optionsData).getBytes());
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

    public final static class OptionsBean
    {
        public boolean safe_mode = false;
        public String game_package_name = GameManager.PACKAGE_NAME;
    }
}
