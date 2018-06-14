package com.example.android.scorecounter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    int goalRM = 0;
    int goalFCB = 0;
    int foulRM = 0;
    int foulFCB = 0;
    int cornerRM = 0;
    int cornerFCB = 0;
    int count = 0;
    File imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void shareScore(View v) {
        Bitmap bitmap = takeScreenshot();
        final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            saveBitmap(bitmap);
            shareIt();
        }

    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + getString(R.string.screenshot));
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(getString(R.string.tags), e.getMessage(), e);
        } catch (IOException e) {
            Log.e(getString(R.string.tags), e.getMessage(), e);
        }
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType(getString(R.string.image));
        String shareBody = getString(R.string.sharing);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.uefa));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharevia)));
    }

    public void addScoreForRM(View v) {
        goalRM = goalRM + 1;
        displayForRM(goalRM);
    }

    public void addScoreForFCB(View v) {
        goalFCB = goalFCB + 1;
        displayForFCB(goalFCB);
    }

    public void addFoulForRM(View v) {
        foulRM = foulRM + 1;
        display1ForRM(foulRM);
    }

    public void addFoulForFCB(View v) {
        foulFCB = foulFCB + 1;
        display1ForFCB(foulFCB);
    }

    public void addCornerForRM(View v) {
        cornerRM = cornerRM + 1;
        display2ForRM(cornerRM);
    }

    public void addCornerForFCB(View v) {
        cornerFCB = cornerFCB + 1;
        display2ForFCB(cornerFCB);
    }

    public void resetScore(View v) {
        goalRM = 0;
        goalFCB = 0;
        cornerFCB = 0;
        cornerRM = 0;
        foulRM = 0;
        foulFCB = 0;
        display1ForFCB(foulFCB);
        display2ForFCB(cornerFCB);
        display1ForRM(foulRM);
        display2ForRM(cornerRM);
        displayForRM(goalRM);
        displayForFCB(goalFCB);
    }

    public void displayForRM(int score) {
        TextView scoreView = findViewById(R.id.rm_score);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForFCB(int score) {
        TextView scoreView = findViewById(R.id.fcb_score);
        scoreView.setText(String.valueOf(score));
    }

    public void display1ForRM(int score) {
        TextView scoreView = findViewById(R.id.rm_foul);
        scoreView.setText(String.valueOf(score));
    }

    public void display1ForFCB(int score) {
        TextView scoreView = findViewById(R.id.fcb_foul);
        scoreView.setText(String.valueOf(score));
    }

    public void display2ForRM(int score) {
        TextView scoreView = findViewById(R.id.rm_corner);
        scoreView.setText(String.valueOf(score));
    }

    public void display2ForFCB(int score) {
        TextView scoreView = findViewById(R.id.fcb_corner);
        scoreView.setText(String.valueOf(score));
    }

    public void onBackPressed(View v) {
        count = count + 1;
        if (count == 1) {
            Toast.makeText(getApplicationContext(), R.string.information, Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
