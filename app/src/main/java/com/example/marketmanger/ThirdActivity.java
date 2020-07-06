package com.example.marketmanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {


        EditText name, account, password, password_confirm;
        Button button;
        //    String[] a, n, p, pc;
        TextView textView;
        CheckBox checkBox;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_third);
            getSupportActionBar().hide();
            button = findViewById(R.id.btn_register);
            textView = findViewById(R.id.info);
            name = findViewById(R.id.edit_name);
            account = findViewById(R.id.edit_account);
            password = findViewById(R.id.edit_password);
            password_confirm = findViewById(R.id.edit_password_confirm);
            checkBox = findViewById(R.id.manager_check);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name.getText().toString().isEmpty() || account.getText().toString().isEmpty() || password.getText().toString().isEmpty() || password_confirm.getText().toString().isEmpty()) {
                        textView.setText("您还没有输入完整信息");
                    } else if (password.getText().toString().length() < 6) {
                        textView.setText("密码长度不得小于6位");
                    } else if (!password.getText().toString().equals(password_confirm.getText().toString())) {
                        textView.setText("两次密码输入不一致，请重新输入");
                    } else if (isExist(account.getText().toString())) {
                        textView.setText("此账户已存在");
                    } else {
                        textView.setText("");
                        register(account.getText().toString(), password.getText().toString(), name.getText().toString());


                    }
                }
            });

        }

        private boolean isExist(String s) {
            SharedPreferences pre = getSharedPreferences("data", Context.MODE_PRIVATE);
            if (pre.getString(s, "").equals("")) {
                return false;
            } else
                return true;

        }
    private void register(String account, String password, String name) {
        SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_APPEND).edit();
        editor.putString(account, password);
        editor.putString(account+"|", name);
        if (checkBox.isChecked()){
            editor.putBoolean(account+"_isManager",true);
        }else {editor.putBoolean(account+"_isManager",false);}
        editor.apply();
        Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("account",account);
        startActivity(intent);
        finish();
    }
    }

