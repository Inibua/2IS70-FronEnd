package com.example.group16.journaloo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group16.journaloo.activity.CreateJourneyActivity;
import com.example.group16.journaloo.activity.NewEntryActivity;
import com.example.group16.journaloo.activity.ViewJourneyActivity;
import com.example.group16.journaloo.activity.ViewProfileActivity;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.model.Journey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    public boolean journeyActive; // becomes true if journey is saved, is used to change layout of MainActivity
    public String nameOfJourney; // to be passed too the viewJourney activity when stopping an active journey
    static final int requestCode =20;
    public GestureDetectorCompat detector;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private Journey activeJourneyObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(1900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (getIntent().getExtras() != null) {
            journeyActive = getIntent().getExtras().getBoolean("isActive");
        }

        if (!journeyActive) {
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main_active);
            //activeJourneyObj = wrapper.getCurrentJourney();
            TextView nameJourney = (TextView) findViewById(R.id.nameJourney);
            nameJourney.setText(getIntent().getExtras().getString("nameJourney"));
            nameOfJourney = getIntent().getExtras().getString("nameJourney");
            detector = new GestureDetectorCompat(this,this);
        }


        // create custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

    }


    public void stopJourney(View view){
        Intent intent = new Intent(this, ViewJourneyActivity.class);
        Toast.makeText(getApplicationContext(), "Saved Journey", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(intent);
    }


    public void openCreateJourney(View view){
        Intent intent = new Intent(this, CreateJourneyActivity.class);
        finish();
        startActivity(intent);
    }

/*
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

    */

    /*@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(getApplicationContext(),"SD card found",Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(getApplicationContext(),"SD card not found",Toast.LENGTH_LONG).show();
        }

        if(e2.getX() - e1.getX() > 50)  {
            startActivityForResult(photoCaptureIntent, requestCode);
            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
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
            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);
        */
        //}

    //}
/*
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

*/



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.landing:
                if (!journeyActive) {
                    Intent intentL = new Intent(this, MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Main", Toast.LENGTH_SHORT).show();
                    startActivity(intentL);

                }else{
                    Intent intentL2 = new Intent(this,MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Main", Toast.LENGTH_SHORT).show();
                    intentL2.putExtra("isActive", journeyActive);
                    startActivity(intentL2);
                }
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


    // unused required methods

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false;}

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}


*/