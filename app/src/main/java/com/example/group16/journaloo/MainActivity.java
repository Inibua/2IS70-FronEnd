package com.example.group16.journaloo;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public boolean journeyActive = false; // becomes true if journey is saved, is used to change layout of MainActivity
    static final int requestCode =20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            journeyActive = getIntent().getExtras().getBoolean("isActive");
        }

        if (!journeyActive) {
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main_2);
            TextView nameJourney = (TextView) findViewById(R.id.nameJourney);
            nameJourney.setText(getIntent().getExtras().getString("nameJourney"));

        }


        // create custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

    }




    public void stopJourney(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(getApplicationContext(), "Saved Journey", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


    public void openCreateJourney(View view){
        Intent intent = new Intent(this, CreateJourneyActivity.class);
        finish();
        startActivity(intent);
    }


    public void openCamera(View view) {
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(getApplicationContext(),"SD card found",Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(getApplicationContext(),"SD card not found",Toast.LENGTH_LONG).show();
        }




        startActivityForResult(photoCaptureIntent, requestCode);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(MainActivity.requestCode == requestCode && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            try {
                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                bmp.recycle();

                //Pop intent
                Intent in1 = new Intent(this, NewEntryActivity.class);
                in1.putExtra("image", filename);
                startActivity(in1);
            } catch (Exception e) {
                e.printStackTrace();
            }



            /*
            ImageView entryView = (ImageView) findViewById(R.id.entryView);
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");


            entryView.setImageBitmap(bitmap);


            Intent intent = new Intent(this, NewEntryActivity.class);
            startActivity(intent);
          //  String partFilename = currentDateFormat();
          //  storeCameraPhotoInSDCard(bitmap, partFilename);

            // display the image from SD Card to ImageView Control
            //String storeFilename = "photo_" + partFilename + ".jpg";
           // Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
           // entryView.setImageBitmap(mBitmap);

        */
        }

    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.landing:
                Toast.makeText(getApplicationContext(), "Landing", Toast.LENGTH_SHORT).show();
                break;
            case R.id.explore:
                // fill in what should happen when clicked help
                Toast.makeText(getApplicationContext(), "Explore", Toast.LENGTH_SHORT).show();
                break; // break to end action only do the filled in actions
            case R.id.history:
                Intent intent2 = new Intent(this, ViewJourneyActivity.class);
                Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_SHORT).show();
                startActivity(intent2);
                break;
            case R.id.profile:
                Intent intent = new Intent(this, ViewProfileActivity.class);
                Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
