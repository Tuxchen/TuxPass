package com.example.mypasswordsactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button login;
    private EditText passwordField;
    private FileInputStream fis;
    private FileOutputStream fos;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(fileExists(this, "password.dat")) {
            try {
                fis = openFileInput("password.dat");

                int b;
                pass = "";

                while((b = fis.read()) != -1) {
                    pass += (char) b;
                }

                fis.close();
            }
            catch(FileNotFoundException e) {

            }
            catch(IOException e) {

            }
        }
        else {
            pass = "123456";

            try {
                fos = openFileOutput("password.dat", Context.MODE_PRIVATE);

                fos.write(pass.getBytes());

                fos.close();
            }
            catch(FileNotFoundException e) {

            }
            catch(IOException e) {

            }
        }

        login = (Button) findViewById(R.id.ok);
        login.setOnClickListener(this);

        passwordField = (EditText) findViewById(R.id.passwordfield);
    }

    public void onClick(View v) {

        if(pass.equals(passwordField.getText().toString())) {
            passwordField.setText("");
            Intent intent = new Intent(v.getContext(), PasswordsActivity.class);
            intent.putExtra("password", "correct");
            startActivity(intent);
        }
        else {
            passwordField.setText("");
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.wrongTitle);
            builder.setMessage(R.string.wrongMsg);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog diag = builder.create();
            diag.show();
        }
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);

        if(file == null || !file.exists()) {
            return false;
        }

        return true;
    }
}
