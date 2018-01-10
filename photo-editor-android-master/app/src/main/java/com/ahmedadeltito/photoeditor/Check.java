package com.ahmedadeltito.photoeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by divit on 08-01-2018.
 */

public class Check extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.check );
        ImageView ivv = (ImageView) findViewById( R.id.ivv );
        if(getIntent().getExtras()==null){
            return;
        }

     /*   String imagepath = getIntent().getExtras().getString( "imageUri" );
        if(imagepath==null){
            return;
        }
        Uri imageUri = Uri.fromFile( new File( imagepath ) );
        ivv.setImageURI( imageUri );
      //  String stringUri =  getIntent().getExtras().getString( "resultUri" );
      //  BitmapFactory.Options options = new BitmapFactory.Options();
      //  options.inSampleSize = 1;
      //  Bitmap bitmap = BitmapFactory.decodeFile(stringUri, options);
      //  ivv.setImageBitmap( bitmap );
    */
    }
}