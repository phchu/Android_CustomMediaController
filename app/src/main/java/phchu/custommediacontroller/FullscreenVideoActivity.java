package phchu.custommediacontroller;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

public class FullscreenVideoActivity extends Activity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    private String TAG = "FullscreenVideoActivity";
    SurfaceView videoSurface;
    private MediaPlayer mediaPlayer;
    private VideoControllerView controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        String videoPath =  "android.resource://" + getPackageName() + "/" + R.raw
                .sample_video;
        mediaPlayer = new MediaPlayer();
        controller = new VideoControllerView(this);
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(this, Uri.parse(videoPath));
            mediaPlayer.setOnPreparedListener(this);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException |
                IOException e) {
            e.printStackTrace();
        }
    }

    //Show the media controller
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        mediaPlayer.start();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public boolean isFullScreen() {
        boolean isFullScreen = (getResources().getConfiguration().orientation==2);
        Log.d(TAG, "isFullScreen: "+isFullScreen);
        return isFullScreen;
    }

    @Override
    public void toggleFullScreen() {
        Log.d(TAG, "toggleFullScreen");
        mediaPlayer.pause();
        if(getResources().getConfiguration().orientation==1){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
