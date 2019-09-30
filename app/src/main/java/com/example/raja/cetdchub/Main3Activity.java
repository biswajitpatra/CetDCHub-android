package com.example.raja.cetdchub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main3Activity extends AppCompatActivity {
public static String GETNOFILE="";
    Filetransac ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.USER_SEARCH);
        final TextView textView = findViewById(R.id.editText3);
        textView.setText(message);
        ft=new Filetransac(this);
        ft.execute(message);/*
        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                TableLayout table = (TableLayout) findViewById(R.id.mylayout); table.removeAllViews();
                ft.selfRestart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                TableLayout table = (TableLayout) findViewById(R.id.mylayout); table.removeAllViews();
                ft.selfRestart();
            }
        });*/
    }

    /**
    void function_button(View view){
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        //Fileno fno=new Fileno(this);
        //fno.execute(buttonText);
        Main3Activity.this.GETNOFILE=buttonText;



    }

    public class Fileno extends AsyncTask<String, String,String>{
        Context context;
        Fileno(Context ctx) {
            this.context = ctx;
        }
        @Override
        protected String doInBackground(String... voids) {
            try {
                FileInputStream in = openFileInput("cetdcdetails.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                sb.setLength(0);
                String line;
                int n=0;
                while ((line = bufferedReader.readLine()) != null) {
                    n+=1;
                    sb.append(line+"\n");
                    if(n%2==0){
                        if(!voids[0].equalsIgnoreCase(sb.toString())){
                            sb.setLength(0);
                            continue;
                        }
                        else{
                            sb.setLength(0);
                            String namer=sb.toString();
                            Log.e("ALOAT:::", namer.substring(0, namer.indexOf("\n")));
                            return namer.substring(0, namer.indexOf("\n"));
                        }
                    }
                }

                bufferedReader.close();
                inputStreamReader.close();
                in.close();

            }catch(Exception e) {
                e.printStackTrace();
                Log.e("RESULT:::","Error opening file"+e.toString());
            }
          return null;
        }
        @Override
        protected void onPostExecute(String voids){
            super.onPostExecute(voids);
            Main3Activity.this.GETNOFILE=voids;
            Log.e(":::",voids);
            Toast.makeText(context, voids, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, Getfolder.class);
            context.startActivity(intent);
            ((Activity)context).finish();


        }
    }
        */
    public class Filetransac extends AsyncTask<String, String, Void> {
        Context context;

        Filetransac(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected Void doInBackground(String... voids) {
            try {
                String sbs;
                FileInputStream in = openFileInput("cetdcdetails.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                sb.setLength(0);
                String line;
                int n=0;
                Log.e("voidds:::",voids[0]);
                while ((line = bufferedReader.readLine()) != null) {
                    n+=1;
                    sb.append(line+"\n");
                    if(n%2==0){
                        sbs=sb.toString();
                        sbs=sbs.toLowerCase();
                        if(sbs.indexOf(voids[0].toLowerCase())<0){
                            sb.setLength(0);
                            continue;
                        }
                        TableLayout tl = (TableLayout) findViewById(R.id.mylayout);
                        TableRow tr = new TableRow(Main3Activity.this);
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
                        Button b = new Button(Main3Activity.this);
                        b.setTransformationMethod(null);
                        //b.setText(sb);
                        b.setText(Html.fromHtml("<big><b><font color='#333333'>"+sb.substring(0,sb.indexOf("\n"))+"</font></b></big><small><b><font color='#CC5490'>"+"("+sb.substring(sb.indexOf("\n"))+")"+"</font></b></small>"));
                        b.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                Button b = (Button)view;
                                String buttonText = b.getText().toString().trim();
                                Log.e("ALOAT:::",buttonText);
                                buttonText=buttonText.substring(0,buttonText.indexOf('('));
                                Main3Activity.this.GETNOFILE=buttonText;
                                Log.e("ALOAT:::",buttonText);
                                Intent intent = new Intent(context, Getfolder.class);
                                intent.putExtra("FILENAME",buttonText);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                                /*
                                MessageSender ms=new MessageSender();
                                String address=ms.ip;
                                int port=ms.port;
                                Intent intent = new Intent(context, Servicedown.class);
                                intent.putExtra("MyService.text", buttonText);
                                intent.putExtra("MyService.ip", ms.ip);
                                intent.putExtra("MyService.port", ms.port);
                                startService(intent);*/
                                //Fileno fno=new Fileno(context);
                                //fno.execute(buttonText);
                            }
                        });
                        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tr.addView(b);
                        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        sb.setLength(0);
                    }
                }

                bufferedReader.close();
                inputStreamReader.close();
                in.close();

            }catch(Exception e) {
                 e.printStackTrace();
                 Log.e("RESULT:::","Error opening file  "+e.toString());
            }
            return null;
        }
        public void selfRestart() {
            ft = new Filetransac(Main3Activity.this);
        }

    }

}
