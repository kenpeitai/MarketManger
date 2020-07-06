package com.example.marketmanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private Button logIn;
    private TextView info;
    private TextView register;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        account = findViewById(R.id.edit_account);
        password = findViewById(R.id.edit_password);
        logIn = findViewById(R.id.log_in);
        info = findViewById(R.id.info);
        register = findViewById(R.id.register);
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ac = account.getText().toString();
                String pa = password.getText().toString();
                if (sharedPreferences.getString(ac, "").equals("")) {
                    info.setText("此账户不存在");

                } else if (sharedPreferences.getString(ac, "").equals(pa)) {
                    info.setText("");
                    String name = sharedPreferences.getString(ac + "|", "");
                    if (sharedPreferences.getBoolean(ac + "_isManager", false)) {
                        Intent intent1 = new Intent(MainActivity.this, ManagerActivity.class);
                        intent1.putExtra("name", name);
                        intent1.putExtra("account", ac);
                        password.setText("");
                        startActivity(intent1);
                    } else {
                        Intent intent2 = new Intent(MainActivity.this, EmployeeActivity.class);
                        intent2.putExtra("name", name);
                        intent2.putExtra("account", ac);
                        password.setText("");
                        startActivity(intent2);
                    }



                } else {
                    info.setText("密码错误");
                    password.setText("");
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ThirdActivity.class));

            }
        });

    }


}
