package com.example.mypasswordsactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PasswordsActivity extends Activity implements View.OnClickListener {

    private Spinner passwords;
    private Button changepass, add;
    private FileOutputStream fos;
    private FileInputStream fis;
    private List<String> passwordsList;
    private String content;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passlayout);

        Intent intent = getIntent();
        if(intent.getStringExtra("password").equals("correct")) {

            passwords = (Spinner) findViewById(R.id.passwords);
            passwordsList = new ArrayList<>();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, passwordsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            passwords.setAdapter(adapter);

            if(fileExists(this, "passwords.dat")) {
                try {
                    fis = openFileInput("passwords.dat");

                    int b;
                    content = "";

                    while((b = fis.read()) != -1) {
                        content += (char) b;
                    }

                    fis.close();

                    StringTokenizer st = new StringTokenizer(content, "\n");

                    while(st.hasMoreTokens()) {
                        String name = st.nextToken();
                        name = name.substring(0, name.indexOf(";"));
                        passwordsList.add(name);
                    }
                }
                catch(FileNotFoundException e) {

                }
                catch(IOException e) {

                }
            }

            add = (Button) findViewById(R.id.add);
            add.setOnClickListener(this);

            passwords = (Spinner) findViewById(R.id.passwords);

            changepass = (Button) findViewById(R.id.changepass);
            changepass.setOnClickListener(this);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Unauthorized!");
            builder.setMessage("You are not allowed to access this resource!");
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog diag = builder.create();
            diag.show();
            finish();
        }
    }


    public void onPause() {
        super.onPause();

        finish();
    }

    public void onDestroy() {
        super.onDestroy();

        finish();
    }

    public void onClick(View v) {
        if(v == changepass) {
            final Dialog diag = new Dialog(v.getContext());
            diag.setContentView(R.layout.passworddialog);
            final EditText neupass = (EditText) diag.findViewById(R.id.neupass);
            final EditText neupassneu = (EditText) diag.findViewById(R.id.neupassneu);
            final TextView info = (TextView) diag.findViewById(R.id.info);

            Button ok = (Button) diag.findViewById(R.id.neupassok);
            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (neupass.getText().toString().equals(neupassneu.getText().toString())) {
                        String pass = neupass.getText().toString();

                        try {
                            fos = openFileOutput("password.dat", Context.MODE_PRIVATE);

                            fos.write(pass.getBytes());

                            fos.close();
                        }
                        catch(FileNotFoundException e) {

                        }
                        catch(IOException e) {

                        }

                        diag.dismiss();
                    } else {
                        info.setText("Check your password input!");
                    }
                }
            });

            Button cancel = (Button) diag.findViewById(R.id.neupasscancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    diag.dismiss();
                }
            });

            diag.show();
        }
        else if(v == add) {
            final Dialog diag = new Dialog(v.getContext());
            diag.setContentView(R.layout.addpassworddialog);

            final EditText passname = (EditText) diag.findViewById(R.id.passname);
            final EditText username = (EditText) diag.findViewById(R.id.username);
            final EditText savepass = (EditText) diag.findViewById(R.id.savepassword);

            Button save = (Button) diag.findViewById(R.id.savebutton);
            save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String neupass = passname.getText().toString() + ";" + username.getText().toString() + ";" + savepass.getText().toString() + "\n";
                    passwordsList.add(passname.getText().toString());

                    content += neupass;

                    if(fileExists(v.getContext(), "passwords.dat")) {
                        try {
                            fos = openFileOutput("passwords.dat", Context.MODE_PRIVATE);

                            fos.write(content.getBytes());

                            fos.close();
                        }
                        catch(FileNotFoundException e) {

                        }
                        catch(IOException e) {

                        }
                    }
                    else {
                        try {
                            content = neupass;

                            fos = openFileOutput("passwords.dat", Context.MODE_PRIVATE);

                            fos.write(content.getBytes());

                            fos.close();
                        }
                        catch(FileNotFoundException e) {

                        }
                        catch(IOException e) {

                        }
                    }

                    diag.dismiss();
                }
            });

            Button cancel = (Button) diag.findViewById(R.id.cancelbutton);
            cancel.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {
                   diag.dismiss();
               }
            });

            diag.show();
        }
        else if(v == passwords) {

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
