package net.listerily.moddedbe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.EnderCoreOptions;
import net.listerily.endercore.android.operator.FileManager;
import net.listerily.endercore.android.operator.GamePackageManager;
import net.listerily.endercore.android.utils.CPUArch;

public class FatalActivity extends AppCompatActivity {

    public static final String TAG_FATAL_MESSAGES = "FATAL_MESSAGES";
    private String message;
    private String appVersionName;
    private String gameVersionName;
    private int safeMode;
    private String abisFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fatal);

        message = getIntent().getStringExtra(TAG_FATAL_MESSAGES);
        if (message == null)
            return;
        GamePackageManager gamePackageManager = EnderCore.instance.getGamePackageManager();
        EnderCoreOptions options = EnderCore.instance.getEnderCoreOptions();
        appVersionName = getString(R.string.app_version_name);
        gameVersionName = gamePackageManager.getVersionName();
        safeMode = options.isSafeMode() ? 1 : 0;
        String abis;
        StringBuilder builder = new StringBuilder();
        StringBuilder builderFull = new StringBuilder();
        for(String abi : CPUArch.getSupportedAbis())
        {
            String text;
            if(abi.equals("armeabi"))
                text = "arm";
            else if(abi.startsWith("arm") && abi.endsWith("v7a"))
                text = "v7a";
            else if(abi.startsWith("arm") && abi.endsWith("v8a"))
                text = "arm64";
            else if(abi.equals("arm64"))
                text = "arm64";
            else if(abi.equals("x86"))
                text = "x86";
            else if(abi.startsWith("x86") && abi.endsWith("64"))
                text = "x64";
            else
                text = abi;
            builder.append(text).append(" ");
            builderFull.append(abi).append(" ");
        }
        abis = builder.toString();
        abisFull = builderFull.toString();

        ((TextView) findViewById(R.id.textViewFatalMessage)).setText(message);
        ((TextView) findViewById(R.id.textViewAppVersion)).setText(getString(R.string.app_fatal_version_name, getString(R.string.app_version_name)));
        ((TextView) findViewById(R.id.textViewGameVersion)).setText(getString(R.string.app_fatal_game_version, gamePackageManager.getVersionName()));
        ((TextView) findViewById(R.id.textViewEnderCoreSdk)).setText(getString(R.string.app_fatal_endercore_sdk, EnderCore.SDK_VERSION));
        ((TextView) findViewById(R.id.textViewOSSdk)).setText(getString(R.string.app_fatal_os_sdk, Build.VERSION.SDK_INT));
        ((TextView) findViewById(R.id.textViewBrand)).setText(getString(R.string.app_fatal_brand, Build.BRAND));
        ((TextView) findViewById(R.id.textViewModel)).setText(getString(R.string.app_fatal_model, Build.MODEL));
        ((TextView) findViewById(R.id.textViewSafeMode)).setText(getString(R.string.app_fatal_safe_mode,(options.isSafeMode() ? 1 : 0)));
        ((TextView) findViewById(R.id.textViewSupportedABIS)).setText(getString(R.string.app_fatal_abi,abis));
    }

    public void onExitClicked(View view) {
        finish();
    }

    public void onCopyClicked(View view){
        String messageHead = "-----------------------\nA fatal error occurred in ModdedBE game initializing.\nVersion Name: " + appVersionName + "\nGame Version: " + gameVersionName + "\nEnderCore SDK: " + EnderCore.SDK_VERSION + "\nOS SDK: " + Build.VERSION.SDK_INT + "\nBrand: " + Build.BRAND + "\nModel: " + Build.MODEL + "\nSafe Mode: " + safeMode + "\nABI:" + abisFull + "\n-----------------------\n";
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("ModdedBE Error Message", messageHead + message);
        cm.setPrimaryClip(mClipData);
        Toast.makeText(this, R.string.app_copied, Toast.LENGTH_LONG).show();
    }

    public void onClearClicked(View view) {
        EnderCore.instance.destroy();
        FileManager fileManager = new FileManager(this);
        fileManager.removeEnderCoreData();
        Toast.makeText(this, R.string.app_app_data_cleared, Toast.LENGTH_LONG).show();
    }
}