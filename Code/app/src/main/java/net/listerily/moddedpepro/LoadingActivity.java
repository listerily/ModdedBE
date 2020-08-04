package net.listerily.moddedpepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import net.listerily.moddedpepro.ui.BitmapRepeater;
import net.listerily.nmodder_android.launcher.Launcher;
import net.listerily.nmodder_android.launcher.LauncherException;
import net.listerily.nmodder_android.nmod.NMod;

import java.io.IOException;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        /*
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_background);
        bitmap = BitmapRepeater.repeat(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight(), bitmap);
        getWindow().getDecorView().setBackground(new BitmapDrawable(bitmap));
        */

        new Thread()
        {
            @Override
            public void run() {
                super.run();
                Launcher.mInstance.setLauncherListener(new Launcher.LauncherListener() {
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
                    public void onLoadAppAssest(String name) {

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
                        handler.sendEmptyMessageDelayed(LAUNCH_FINISH,500);
                    }

                    @Override
                    public void onError(Error error) {
                        Message errorMessage = new Message();
                        errorMessage.what = LAUNCH_ERROR;
                        errorMessage.obj = error;
                        handler.sendMessage(errorMessage);
                    }

                    @Override
                    public void onException(Exception exception) {
                        Message errorMessage = new Message();
                        errorMessage.what = LAUNCH_EXCEPTION;
                        errorMessage.obj = exception;
                        handler.sendMessage(errorMessage);
                    }
                });
                Launcher.mInstance.launchGame();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.app_loading_summary,Toast.LENGTH_LONG).show();
    }

    private LoadingActivity.MHandler handler = new LoadingActivity.MHandler();
    private final int LAUNCH_FINISH = 0;
    private final int LAUNCH_ERROR = 1;
    private final int LAUNCH_EXCEPTION = 2;
    private class MHandler extends Handler
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what)
            {
                case LAUNCH_ERROR:
                    ((Error)msg.obj).printStackTrace();
                    break;
                case LAUNCH_FINISH:

                    break;
                case LAUNCH_EXCEPTION:
                    ((Exception)msg.obj).printStackTrace();
                    break;
            }
        }
    }


}