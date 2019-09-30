package com.example.raja.cetdchub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RequestActivity extends AppCompatActivity {
    public static String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Intent intent= getIntent();
        msg=intent.getStringExtra(MainActivity.USER_LINK);
        TextView txt= findViewById(R.id.textView3);
        txt.setText(Html.fromHtml("<big><b><font color='#333333'>"+txt.getText()+"</font></b></big> <small><b><font color='#CC5490'>"+msg+"</font></b></small>"));

    }
    public void request(View view){
        TextView txt1=findViewById(R.id.editText4);
        String txt11= txt1.getText().toString();
        TextView txt2=findViewById(R.id.editText5);
        String txt21=txt2.getText().toString();
        if(txt11.length()==0){
            Toast.makeText(getApplicationContext(),"Name cant be blank....",Toast.LENGTH_SHORT).show();
        }
        else {
            txt11=txt11.replace(" ","_");
            txt21=txt21.replace("\n",".");
            msg = msg + " " + txt11 + " **" + txt21;
            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra("USERLINK", msg);
            startActivity(intent);
            finish();
        }
    }


}
