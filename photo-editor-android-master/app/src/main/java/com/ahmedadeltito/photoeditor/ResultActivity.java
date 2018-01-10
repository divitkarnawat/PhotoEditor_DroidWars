package com.ahmedadeltito.photoeditor;

/**
 * Created by divit on 08-01-2018.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yalantis.ucrop.view.UCropView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class ResultActivity extends CropBaseActivity {

    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    private static final String TAG = "ResultActivity";
    private static final int DOWNLOAD_NOTIFICATION_ID_DONE = 911;
    public String request_code="croppedimage";
    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent( context, ResultActivity.class );
        intent.setData( uri );
        context.startActivity( intent );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_result );
        Button savecrop = (Button) findViewById( R.id.savecrop );
        savecrop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCroppedImage();
            }
        } );
        try {
            UCropView uCropView = (UCropView) findViewById( R.id.ucrop );
            if (getIntent().getData() == null) {
                return;
            }
            uCropView.getCropImageView().setImageUri( getIntent().getData(), null );
            uCropView.getOverlayView().setShowCropFrame( false );
            uCropView.getOverlayView().setShowCropGrid( false );
            uCropView.getOverlayView().setDimmedColor( Color.TRANSPARENT );
        } catch (Exception e) {
            Log.e( TAG, "setImageUri", e );
            Toast.makeText( this, e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( new File( getIntent().getData().getPath() ).getAbsolutePath(), options );

//        setSupportActionBar( (Toolbar) findViewById( R.id.toolbar ) );
//        final ActionBar actionBar = getSupportActionBar();
        //       if (actionBar != null) {
        //          actionBar.setDisplayHomeAsUpEnabled( true );
        //         actionBar.setTitle( getString( R.string.format_crop_result_d_d, options.outWidth, options.outHeight ) );
        //    }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate( R.menu.menu_result, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            saveCroppedImage();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected( item );
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveCroppedImage();
                }
                break;
            default:
                super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }

    private void saveCroppedImage() {

        Uri imageUri = getIntent().getData();
        if (imageUri != null ){
            Intent returnimage = new Intent( ResultActivity.this,PhotoEditorActivity.class );
            String croppath = getPath(imageUri);
            returnimage.putExtra( "returncroppath",croppath );
            returnimage.putExtra( "caller","crop" );
            startActivity(returnimage);
        } else {
            Toast.makeText( ResultActivity.this, getString( R.string.toast_unexpected_error ), Toast.LENGTH_SHORT ).show();
        }


}
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected String getPath(final Uri uri) {
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (GalleryUtils.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (GalleryUtils.isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return GalleryUtils.getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (GalleryUtils.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return GalleryUtils.getDataColumn(this, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return GalleryUtils.getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {
        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ).getAbsolutePath();
        String filename = String.format( "%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment() );

        File saveFile = new File( downloadsDirectoryPath, filename );

        FileInputStream inStream = new FileInputStream( new File( croppedFileUri.getPath() ) );
        FileOutputStream outStream = new FileOutputStream( saveFile );
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo( 0, inChannel.size(), outChannel );
        inStream.close();
        outStream.close();

        showNotification( saveFile );
    }

    private void showNotification(@NonNull File file) {
        Intent intent = new Intent( Intent.ACTION_VIEW );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setDataAndType( Uri.fromFile( file ), "image/*" );

        NotificationCompat.Builder mNotification = new NotificationCompat.Builder( this );

        mNotification
                .setContentTitle( getString( R.string.app_name ) )
                .setContentText( "" )
                .setTicker( "" )
                .setOngoing( false )
                .setContentIntent( PendingIntent.getActivity( this, 0, intent, 0 ) )
                .setAutoCancel( true );


    }


}