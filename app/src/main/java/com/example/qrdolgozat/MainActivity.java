package com.example.qrdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button buttonScan, buttonKiir;
    private TextView textViewSzoveg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("QR Code szkennelés");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();

            }
        });

        buttonKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date datum = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datumFormazva = dateFormat.format(datum);
                String sor = String.format(" %s, %s",textViewSzoveg.getText().toString(), datumFormazva);
                String allapot = Environment.getExternalStorageState();
                if (allapot.equals(Environment.MEDIA_MOUNTED)) {
                    File fajl = new File(Environment.getExternalStorageState(), "scannedCodes.csv");
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("scannedCodes.csv", MODE_PRIVATE);
                        fileOutputStream.write((sor.getBytes());
                        Toast.makeText(MainActivity.this,"Sikeres fájlba írás" + getFilesDir(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this,"Sikertelen fájlba írás" + getFilesDir(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult eredmeny = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (eredmeny != null) {
            if (eredmeny.getContents() == null) {
                Toast.makeText(this, "Kilépés a szkennelésből", Toast.LENGTH_SHORT).show();
            }
            else {
                textViewSzoveg.setText(eredmeny.getContents());
                try {
                    Uri url = Uri.parse(eredmeny.getContents());
                    Intent intent = new Intent (Intent.ACTION_VIEW, url);
                    startActivity(intent);
                }
                 catch (Exception e) {
                    Log.d("URL ERROR", e.toString());
                 }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        buttonScan = findViewById(R.id.buttonScan);
        buttonKiir = findViewById(R.id.buttonKiir);
        textViewSzoveg = findViewById(R.id.textSzoveg);
    }
}