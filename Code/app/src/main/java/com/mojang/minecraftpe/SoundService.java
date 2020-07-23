package com.mojang.minecraftpe;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class SoundService extends Service {
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return new SoundBinder();
    }

    public void onCreate() {
        super.onCreate();
        //TODO THIS
        //player = MediaPlayer.create(getApplicationContext(), R.raw.calm1);
        player.setLooping(true);
        player.start();
    }

    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }

    public void pause() {
        player.pause();
    }

    public void play() {
        player.start();
    }

    public class SoundBinder extends Binder {
        public SoundService getService() {
            return SoundService.this;
        }
    }
}