package com.example.raspbiansays;


import android.bluetooth.BluetoothSocket;

import android.content.Intent;

import android.graphics.Color;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;



public class MainActivity extends AppCompatActivity {


    String address = null;
    boolean allow = false;

    BluetoothSocket btSocket = null;



    Button btn1, btn2, btn3, btn4;
    Intent i;
    TextView tv;
    Recieve r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        address = getIntent().getStringExtra("addr");

        tv = findViewById(R.id.tv);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn1.setBackgroundColor(Color.RED);
        btn2.setBackgroundColor(Color.BLUE);
        btn3.setBackgroundColor(Color.MAGENTA);
        btn4.setBackgroundColor(Color.GREEN);


        btSocket = Connect.btSocket;

        r = new Recieve(btSocket);
        r.start();



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("1");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("2");

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("3");
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("4");
            }
        });

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String text = (String) msg.obj;
            String[] info = text.split(" ");
            if(info[0].equals("score")){
                Intent i = new Intent(MainActivity.this, Score.class);
                i.putExtra("score", info[1]);
                i.putExtra("btnClicked", info[2]);
                i.putExtra("correctBtn", info[3]);
                startActivity(i);
                finish();
            } else if (info[0].equals("allow")){
                if(info[1].equals("true")){
                    allow = true;
                }else if(info[1].equals("false")){
                    allow = false;
                }
            }
            return true;
        }
    });

    private void send(String btn)
    {
        if(allow == true) {
            if (btSocket != null) {
                try {

                    btSocket.getOutputStream().write(btn.toString().getBytes());

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Wait", Toast.LENGTH_SHORT).show();
        }
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {  Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();}
        }

        Intent i = new Intent(MainActivity.this, Connect.class);
        startActivity(i);

    }

    public class Recieve extends Thread {

        private BluetoothSocket bs;

        public Recieve(BluetoothSocket bs) {
            this.bs = bs;
        }
        @Override
        public void run() {
            byte[] buf = new byte[1024];
            int bytes;

            InputStream in;
            while (true) {
                try {
                    in = bs.getInputStream();
                    bytes = in.read(buf);
                    String text = new String(buf, 0, bytes);
                    Log.i("TEXT", text);
                    Message msg = new Message();
                    msg.obj = text;
                    handler.sendMessage(msg);

                } catch (IOException e) {

                }
            }

        }
    }


}
