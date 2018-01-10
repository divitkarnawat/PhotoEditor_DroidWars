package com.ahmedadeltito.photoeditor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by divit on 08-01-2018.
 */

public class splash extends AppCompatActivity {
    VideoView videoView;
    RelativeLayout rl1;
Button getStarted;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );
        getStarted = (Button) findViewById( R.id.getStarted );
        getStarted.setVisibility( View.INVISIBLE );
        getWindow().setFormat( PixelFormat.UNKNOWN );

        videoView = (VideoView) findViewById( R.id.videoView );
rl1 = (RelativeLayout)findViewById( R.id.rl1 );
rl1.setBackgroundColor( Color.BLACK );

        String path = "android.resource://" + getPackageName() + "/" + R.raw.lines;
        Uri uri = Uri.parse( path );

        videoView.setVideoURI( uri );
        videoView.requestFocus();
        videoView.start();
        videoView.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                String path = "android.resource://" + getPackageName() + "/" + R.raw.l2;
                Uri uri1 = Uri.parse( path );
                videoView.setVideoURI( uri1 );
                videoView.requestFocus();
                videoView.start();

                videoView.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Animation animation;
                        animation = AnimationUtils.loadAnimation(splash.this, R.anim.fade_out);
                        videoView.setAnimation( animation );
                        videoView.setVisibility( View.INVISIBLE );
                        animation = AnimationUtils.loadAnimation( splash.this,R.anim.fade_in );
                        getStarted.setAnimation( animation );
                        getStarted.setVisibility( View.VISIBLE );
                        getStarted.setY( rl1.getHeight()/2 );
                        rl1.setAnimation( animation );
                        rl1.setBackgroundColor( Color.BLACK );
                        rl1.setBackground( getResources().getDrawable( R.drawable.pe2 ) );
                    }
                } );
            }
        } );
        videoView.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping( false );
            }
        } );


      getStarted.setOnClickListener( new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent( splash.this,MainActivity2.class );
              startActivity( intent );
              finish();
          }
      } );
    }
}
