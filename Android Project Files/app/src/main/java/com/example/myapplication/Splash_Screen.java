package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and close splash screen and open home page
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            if (progressStatus == 100) {
                                startActivity(new Intent(getBaseContext(), Home_Page.class));
                                finish();
                            }
                        }
                    });
                    try {
                        // Sleep for 30 milliseconds.
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


//    Runnable close = new Runnable() {
//        @Override
//        public void run() {
//            startActivity(new Intent(getBaseContext(), Home_Page.class));
//            finish();
//        }
//    };
//    Handler canceller = new Handler();
//        canceller.postDelayed(close, 3000);
//    }
    }
