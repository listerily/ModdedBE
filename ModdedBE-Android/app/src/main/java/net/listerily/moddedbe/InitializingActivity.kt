package net.listerily.moddedbe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.operator.Launcher;
import net.listerily.endercore.android.exception.LauncherException;
import net.listerily.endercore.android.nmod.NMod;

import java.io.PrintWriter;
import java.io.StringWriter;

public class InitializingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initializing);

        new Thread()
        {
            @Override
            public void run() {
                super.run();
                EnderCore.instance.getLauncher().setGameInitializationListener(new Launcher.GameInitializationListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onLoadGameFilesStart() {

                    }

                    @Override
                    public void onLoadNativeLibrariesStart() {

                    }

                    @Override
                    public void onLoadNativeLibrary(String name) {

                    }

                    @Override
                    public void onLoadNativeLibrariesFinish() {

                    }

                    @Override
                    public void onLoadJavaLibrariesStart() {

                    }

                    @Override
                    public void onLoadJavaLibrary(String name) {

                    }

                    @Override
                    public void onLoadJavaLibrariesFinish() {

                    }

                    @Override
                    public void onLoadResourcesStart() {

                    }

                    @Override
                    public void onLoadAppAsset(String name) {

                    }

                    @Override
                    public void onLoadAppResource(String name) {

                    }

                    @Override
                    public void onLoadResourcesFinish() {

                    }

                    @Override
                    public void onLoadGameFilesFinish() {

                    }

                    @Override
                    public void onLoadNModsStart() {

                    }

                    @Override
                    public void onLoadNMod(NMod nmod) {

                    }

                    @Override
                    public void onLoadNModNativeLibrary(NMod nmod, String name) {

                    }

                    @Override
                    public void onLoadNModJavaLibrary(NMod nmod, String name) {

                    }

                    @Override
                    public void onLoadNModAsset(String name) {

                    }

                    @Override
                    public void onLoadNModsFinish() {

                    }

                    @Override
                    public void onArrange() {

                    }

                    @Override
                    public void onFinish() {
                        Message finishMessage = new Message();
                        finishMessage.what = LAUNCH_FINISH;
                        handler.sendMessage(finishMessage);
                    }

                    @Override
                    public void onSuspend() {

                    }
                });

                try {
                    EnderCore.instance.getLauncher().initializeGame(InitializingActivity.this);
                    Message finishMessage = new Message();
                    finishMessage.what = LAUNCH_FINISH;
                    handler.sendMessage(finishMessage);
                } catch (LauncherException e) {
                    Message errorMessage = new Message();
                    errorMessage.what = LAUNCH_SUSPEND;
                    errorMessage.obj = e;
                    handler.sendMessage(errorMessage);
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.app_loading_summary,Toast.LENGTH_LONG).show();
    }

    public void startGameActivity()
    {
        try {
            EnderCore.instance.getLauncher().startGame(this);
            finish();
        } catch (LauncherException e) {
            startFatalActivity(e);
        }
    }

    public void startFatalActivity(LauncherException exception)
    {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        Intent activityIntent = new Intent(this,FatalActivity.class);
        activityIntent.putExtra(FatalActivity.TAG_FATAL_MESSAGES,writer.toString());
        startActivity(activityIntent);
        exception.printStackTrace();
        finish();
    }


    private final InitializingActivity.MHandler handler = new InitializingActivity.MHandler(this);
    private final static int LAUNCH_FINISH = 0;
    private final static int LAUNCH_SUSPEND = 1;
    private static  class MHandler extends Handler
    {
        private final InitializingActivity context;
        MHandler(InitializingActivity context)
        {
            super();
            this.context = context;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what)
            {
                case LAUNCH_FINISH:
                    context.startGameActivity();
                    break;
                case LAUNCH_SUSPEND:
                    context.startFatalActivity((LauncherException)msg.obj);
                    break;
            }
        }
    }
}