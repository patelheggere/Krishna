package com.patelheggere.krishna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button red, green, blue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        red = findViewById(R.id.red);
        blue = findViewById(R.id.blue);
        green = findViewById(R.id.green);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KmlDemoActivity.class);
                intent.putExtra("MAP", 1);
                startActivity(intent);
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KmlDemoActivity.class);
                intent.putExtra("MAP", 3);
                startActivity(intent);
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KmlDemoActivity.class);
                intent.putExtra("MAP", 2);
                startActivity(intent);
            }
        });
    }
}
