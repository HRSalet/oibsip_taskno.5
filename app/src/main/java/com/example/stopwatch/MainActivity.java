package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ImageButton start, stop, reset;
    ProgressBar progressBar;
    int seconds, minutes, milliSeconds;
    long millisecond, startTime, timeBuff, updateTime = 0L;
    Handler handler;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecond = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecond;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);

            textView.setText(MessageFormat.format("{0}:{1}:{2}", minutes, String.format(Locale.getDefault(), "%02d", seconds), String.format(Locale.getDefault(), "%02d", milliSeconds)));

            // Update the progress of the progress bar based on the elapsed time
            progressBar.setProgress((int) updateTime);

            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textStopwatch);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        reset = findViewById(R.id.reset);
        progressBar = findViewById(R.id.progressBar);

        handler = new Handler(Looper.getMainLooper());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);

                // Set the maximum progress of the progress bar based on the total duration of the stopwatch (60 seconds)
                progressBar.setMax(60000); // 60 seconds in milliseconds
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += millisecond;
                handler.removeCallbacks(runnable);
                reset.setVisibility(View.VISIBLE);
                stop.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                millisecond = 0L;
                startTime = 0L;
                timeBuff = 0L;
                updateTime = 0L;
                seconds = 0;
                minutes = 0;
                milliSeconds = 0;
                textView.setText("00:00:00");
                reset.setVisibility(View.INVISIBLE);

                // Reset the progress of the progress bar
                progressBar.setProgress(0);
            }
        });

        textView.setText("00:00:00");
    }
}