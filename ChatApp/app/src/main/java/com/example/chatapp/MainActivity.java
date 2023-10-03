package com.example.chatapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Struct;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class MainActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private  Button buttonSend;
    private  TextView textViewChat;

    private WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main) ;

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        textViewChat = findViewById(R.id.textViewChat);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("ws://localhost:8080").build();

        WebSocketListener listener = new WebSocketListener() {

            @Override
            public  void onOpen(WebSocket webSocket, Response response){
                MainActivity.this.webSocket =webSocket;
            }

            @Override
            public  void onMessage(WebSocket webSocket, String text){
                updateChat(text);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                // WebSocket connection is closing
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                // WebSocket connection failed
            }
        };

        webSocket = client.newWebSocket(request, listener);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }
    private  void sendMessage(){
        String msg = editTextMessage.getText().toString();
        if (webSocket != null){
            webSocket.send(msg);
            editTextMessage.setText("");
            updateChat("Yout: "+msg);
        }
    }

    private  void updateChat(final  String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewChat.append(msg+"\n");
            }
        });
    }
}