package com.example.raja.cetdchub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.raja.cetdchub.SplashActivity.bgvar;

public class MainActivity extends AppCompatActivity {
    public static final String USER_LINK = "MESSAGE";
    public static final String USER_SEARCH="mm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(":::bgvar",Integer.toString(bgvar));
        if(bgvar==-100){
            Log.e(":::","-100");
            AlertDialog.Builder goLogin = new AlertDialog.Builder(getApplicationContext());
            goLogin.setMessage("UPDATE REQUIRED");
            goLogin.setCancelable(false);
            goLogin.setPositiveButton("I WILL DO IT", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    //System.exit(0);
                }
            });
            AlertDialog alertLogin = goLogin.create();
            alertLogin.show();
        }
        if(bgvar==0){
            Toast.makeText(getApplicationContext(),"Error connecting  with Server \n May be server has not started yet..", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(bgvar==-2)
        {

            Toast.makeText(getApplicationContext(),"No or slow internet connection found..", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(bgvar==-900){
            Toast.makeText(getApplicationContext(), "Authentication Failed...\n May be due to slow or no internet connection",
                    Toast.LENGTH_LONG).show();
            finish();
        }

    }
    public void sendlink(View view) {
        EditText editText = (EditText) findViewById(R.id.editText2);
        String message = editText.getText().toString();
        Log.e(":::messae",message);
        if(message.length()==0){
            Toast.makeText(getApplicationContext(),"Link cant be blank",Toast.LENGTH_SHORT).show();
        }
        else{
        Intent intent = new Intent(this, RequestActivity.class);


        intent.putExtra(USER_LINK, message);
        //intent.putExtra(USER_IP, ip2);
        startActivity(intent);}
    }
    public void gotomenu(View view){
        Intent intent = new Intent(this, Main3Activity.class);
        EditText editText = (EditText) findViewById(R.id.editText);

        String message = editText.getText().toString();

        intent.putExtra(USER_SEARCH, message);
        //intent.putExtra(USER_IP, ip2);
        startActivity(intent);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {

            SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                    "    Shibani (");
            spanTxt.append("8018388283");
            spanTxt.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getApplicationContext(), "Calling Shibani....",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:8018388283"));
                    startActivity(intent);
                }
            }, spanTxt.length() - "8018388283".length(), spanTxt.length(), 0);
            spanTxt.append(")\n    Sourajeet (");
            //spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 32, spanTxt.length(), 0);
            spanTxt.append("7077304663");
            spanTxt.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getApplicationContext(), "Calling Sourajeet ...",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:7077304663"));
                    startActivity(intent);
                }
            }, spanTxt.length() - "7077304663".length(), spanTxt.length(), 0);
            spanTxt.append(")\n    Biswajit (");
            //spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 32, spanTxt.length(), 0);
            spanTxt.append("9337144022");
            spanTxt.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getApplicationContext(), "Calling Biswajit ...",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:9337144022"));
                    startActivity(intent);
                }
            }, spanTxt.length() - "9337144022".length(), spanTxt.length(), 0);
            spanTxt.append(")");
            //spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 32, spanTxt.length(), 0);
            final TextView message = new TextView(this);
            message.setText(spanTxt);
            message.setMovementMethod(LinkMovementMethod.getInstance());





            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("CONTACTS");
            //alert.setMessage("Shibani(8018388283)\nSourajeet(7077304663)\nBiswajit(9337144022)");
            alert.setView(message);
            //alert.setView(tx1);
            alert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            alert.show();
            //((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            //view.setMovementMethod(LinkMovementMethod.getInstance());
            //view.setText(spanTxt, TextView.BufferType.SPANNABLE);

        }
        return super.onOptionsItemSelected(item);
    }
}
