package com.example.marketmanger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class ManagerActivity extends AppCompatActivity {
    private ArrayList<Goods> list = new ArrayList<>();
    private static int goodsPosition = -1;
    private Spinner goods;
    private ArrayAdapter<String> adapter;
    private Switch goodsSwitch;
    private EditText goodsPrice,goodsCount,goodsPriceChange,goodsCountChange;
    private TextView goodsInfo1,goodsInfo2,goodsInfo3, goodsAdd,delete;
    private Button Change, histoty_check;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        goods = findViewById(R.id.goods);
        goodsSwitch = findViewById(R.id.switch_gooods);
        goodsInfo1 = findViewById(R.id.goods_info1);
        goodsInfo2 = findViewById(R.id.goods_info2);
        goodsInfo3 = findViewById(R.id.goods_info3);
        Change = findViewById(R.id.btn_price_change);
        goodsAdd = findViewById(R.id.goods_add);
        histoty_check = findViewById(R.id.btn_check_history);
        goodsPrice = findViewById(R.id.edit_price);
        goodsPriceChange = findViewById(R.id.price_edit);
        goodsCountChange = findViewById(R.id.count_edit);
        delete = findViewById(R.id.delete_goods);


        //商品加载
       initGoods();
        final String[] m = new String[list.size()];

        //添加商品设置
        goodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View view = inflater.inflate(R.layout.dialog, null);
                AlertDialog.Builder dia = new AlertDialog.Builder(v.getContext());
                dia.setTitle("Add goods");
                dia.setView(view);
                dia.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final EditText edit1 = view.findViewById(R.id.edit_add_name);
                        final EditText edit2 = view.findViewById(R.id.edit_add_count);
                        final EditText edit3 = view.findViewById(R.id.edit_price);



                        //if()已存在逻辑判断
                        if (edit1.length()!=0&&edit2.length()!=0&&edit3.length()!=0){
                            String name = edit1.getText().toString();
                            int count = Integer.parseInt(edit2.getText().toString());
                            double price = Double.parseDouble(edit3.getText().toString());

                            for (int i = 0; i <m.length ; i++) {
                                if (name.equals(m[i])){
                                    Toast.makeText(ManagerActivity.this, "添加失败[商品已存在]", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }


                        Gson gson = new Gson();
                        Goods newGoods = new Goods(name, price, count);

                        String json = gson.toJson(newGoods, Goods.class);

                        //IO处理
                        save(json);
                        Toast.makeText(ManagerActivity.this, "商品添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(view.getContext(),ManagerActivity.class));
                     } else {
                            Toast.makeText(ManagerActivity.this, "添加失败[没有输入完整信息]", Toast.LENGTH_SHORT).show();;}
                    }
                });
                dia.setCancelable(true);
                //.setCanceledOnTouchOutside(true);
                dia.show();

            }
        });


        //下拉框设置

        Log.d("----------->", String.valueOf(list.size()));
        for (int i = 0; i < list.size(); i++) {

            m[i] = list.get(i).getName();
        }
        Log.d("----------->", String.valueOf(m));
        adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, m);//
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goods.setAdapter(adapter);
        goods.setVisibility(View.VISIBLE);

        goods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text= goods.getItemAtPosition(position).toString();
                for (int i = 0; i < m.length; i++) {
                    if (text.equals(m[i])){
                     String name = list.get(i).getName();
                     String price = String.valueOf(list.get(i).getPrice());
                     String count = String.valueOf(list.get(i).getCount());
                     goodsInfo1.setText("商品名称:"+name);
                     goodsInfo2.setText("单价:"+price);
                     goodsInfo3.setText("库存"+count);
                     goodsPriceChange.setText(price);
                     goodsCountChange.setText(count);
                     goodsSwitch.setChecked(list.get(i).isShow());
                     goodsPosition = i;


                    }
                }
             //   Toast.makeText(view.getContext(),text,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        //开关设置
        goodsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!buttonView.isPressed()) {
//                    return;
//                }
                if (isChecked) {
                    buttonView.setText("上架中");
                } else {
                    buttonView.setText("下架中");
                }
            }
        });


        //修改按钮设置
        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(goodsPosition).setPrice(Double.parseDouble(goodsPriceChange.getText().toString()));
                list.get(goodsPosition).setCount(Integer.parseInt(goodsCountChange.getText().toString()));
                list.get(goodsPosition).setShow(goodsSwitch.isChecked());

                InfoCommit();

                //刷新
                Toast.makeText(ManagerActivity.this, "商品信息修改成功", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(v.getContext(),ManagerActivity.class));

            }
        });

               //删除按钮
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("删除商品");
                dialog.setMessage("你确定要删除这个商品吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        list.remove(goodsPosition);
                        InfoCommit();

                        Toast.makeText(ManagerActivity.this, "商品已删除", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(v.getContext(),ManagerActivity.class));

                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });









        //销售业绩按钮设置

        histoty_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





    }

    void save(String s) {
        FileOutputStream fileOutputStream = null;
        BufferedWriter writer = null;
        try {
            fileOutputStream = openFileOutput("data", MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(s + '|');
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void initGoods() {
        String info = null;
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            info = content.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(info==null){return;}
        String s[] = info.split("\\|",0);
        for (int i = 0; i < s.length; i++) {
            if (s[i] != null) {
                JSONObject jsonObject = JSONObject.parseObject(s[i]);
                Goods newGoods = JSON.toJavaObject(jsonObject,Goods.class);
                list.add(newGoods);
            }

        }
    }
  private void InfoCommit(){
        String jsonstr = "";
      for (int i = 0; i <list.size() ; i++) {
          if (list.get(i)!=null)
          jsonstr += (new Gson().toJson(list.get(i), Goods.class))+'|';
      }

      FileOutputStream fileOutputStream = null;
      BufferedWriter writer = null;
      try {
          fileOutputStream = openFileOutput("data", MODE_PRIVATE);
          writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
          writer.write(jsonstr);
          writer.close();
      } catch (Exception e) {
          e.printStackTrace();
      }



  }


}
