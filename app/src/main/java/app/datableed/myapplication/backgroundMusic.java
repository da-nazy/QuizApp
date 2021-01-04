package app.datableed.myapplication;

import android.content.Context;
import android.media.MediaPlayer;

public class backgroundMusic {
    static MediaPlayer mediaPlayer;
    static  boolean state=true;
    static  boolean pauseDialogState=false;

    static void runMusic(boolean play, Context context){
        if(play){
            mediaPlayer=MediaPlayer.create(context,R.raw.backgroundmusic);

            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    static void resume(){
        mediaPlayer.start();
    }
    static void pause(){
        mediaPlayer.pause();
    }
}
