package com.example.raspbiansays;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Score extends AppCompatActivity {

    TextView scoreTxt,correctTxt, clickedTxt;
    Button menuBtn, againBtn;
    BluetoothSocket bs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreTxt = (TextView) findViewById(R.id.scoreTxt);
        menuBtn = (Button) findViewById(R.id.menuBtn);
        againBtn = (Button) findViewById(R.id.againBtn);
        correctTxt = (TextView) findViewById(R.id.correct);
        clickedTxt = (TextView) findViewById(R.id.clicked);

        againBtn.setVisibility(View.INVISIBLE);

        bs = Connect.btSocket;

        String score = getIntent().getStringExtra("score");
        String correct = getIntent().getStringExtra("correctBtn");
        String clicked = getIntent().getStringExtra("btnClicked");

        scoreTxt.setText(score);
        correctTxt.setText(colorName(correct));
        clickedTxt.setText(colorName(clicked));

        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("again");
                Intent i = new Intent(Score.this, MainActivity.class);
                startActivity(i);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("menu");
                Intent i = new Intent(Score.this, Connect.class);
                startActivity(i);
                finish();
            }
        });






    }

    public String colorName(String c){
        String color = "";
        switch (c){
            case "1":
                color =  "Red";
                break;
            case "2":
                color = "Blue";
                break;
            case "3":
                color = "Magenta";
                break;
            case "4":
                color = "Green";
                break;

        }
        return color;
    }

    private void Disconnect()
    {
        if (bs!=null) //If the btSocket is busy
        {
            try
            {
                bs.close(); //close connection
            }
            catch (IOException e)
            {  Toast.makeText(Score.this, "Error", Toast.LENGTH_SHORT).show();}
        }



    }

    private void send(String btn)
    {
        if (bs!=null)
        {
            try
            {
                bs.getOutputStream().write(btn.toString().getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(Score.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
