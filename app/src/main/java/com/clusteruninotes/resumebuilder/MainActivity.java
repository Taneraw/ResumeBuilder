package com.clusteruninotes.resumebuilder;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView template1 = findViewById(R.id.template1);
        ImageView template2 = findViewById(R.id.template2);

        template1.setOnClickListener(this);
        template2.setOnClickListener(this);
        checkPermission();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.template1:
                Intent intent1 = new Intent(MainActivity.this,TemplateView.class);
                intent1.putExtra("ID",1);
                startActivity(intent1);
                break;
            case R.id.template2:
                Intent intent2 = new Intent(MainActivity.this,TemplateView.class);
                intent2.putExtra("ID",2);
                startActivity(intent2);
                break;
        }
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   //permission granted
                } else {
                    notifyUser("Permission required to continue");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    private void notifyUser(String msg) {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkPermission();
                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        if(!((Activity)MainActivity.this).isFinishing())
            alert.show();
    }
}
