package com.ahmedadeltito.photoeditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity2 extends MediaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main);
        LinearLayout linearLayout = (LinearLayout) findViewById( R.id.ll1 );
        linearLayout.setBackground( getResources().getDrawable( R.drawable.pe10 ) );
    }

    public void openUserGallery(View view)
    {
        openGallery();
    }

    public void openUserCamera(View view)
    {
        startCameraActivity();
    }

    @Override
    protected void onPhotoTaken() {
        Intent intent = new Intent(MainActivity2.this, PhotoEditorActivity.class);
        intent.putExtra("selectedImagePath", selectedImagePath);

        startActivity(intent);
    }
}
