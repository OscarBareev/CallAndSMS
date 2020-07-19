package com.example.callandsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 12;

    private EditText phoneNmb;
    private EditText smsTxt;
    private Button callBtn;
    private Button sendSmsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {
        phoneNmb = findViewById(R.id.callForm);
        smsTxt = findViewById(R.id.textForm);
        callBtn = findViewById(R.id.callBtn);
        sendSmsBtn = findViewById(R.id.sendBtn);

        callBtn.setOnClickListener(callByNumber);
        sendSmsBtn.setOnClickListener(sendSmsText);
    }

    View.OnClickListener callByNumber = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String number = phoneNmb.getText().toString().trim();

            if (number.isEmpty()) {
                Toast.makeText(MainActivity.this, "Введите номер телефона!",
                        Toast.LENGTH_SHORT).show();
            } else {
                checkAndCall(number);
            }
        }
    };

    View.OnClickListener sendSmsText = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String text = smsTxt.getText().toString().trim();
            String number = phoneNmb.getText().toString().trim();

            if (text.isEmpty() || number.isEmpty()) {
                Toast.makeText(MainActivity.this, "Введите данные!",
                        Toast.LENGTH_SHORT).show();
            } else {
                checkAndSend(number, text);
            }
        }
    };

    private void checkAndCall(String phoneNmb) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNmb));
            startActivity(dialIntent);
        }
    }

    private void checkAndSend(String phoneNmb, String smsText) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phoneNmb, null, smsText, null, null);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                // Проверяем результат запроса на право позвонить
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    callBtn.setOnClickListener(callByNumber);

                } else {

                    Toast.makeText(MainActivity.this, "Разрешение на звонки не дано!",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSmsBtn.setOnClickListener(sendSmsText);

                } else {

                    Toast.makeText(MainActivity.this, "Разрешение на sms не дано!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}