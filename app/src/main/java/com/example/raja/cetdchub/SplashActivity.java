package com.example.raja.cetdchub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {
String ip;
public static final String versionapp="1";
public static String uniqueID = null;
public static String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
public static int bgvar=-5662;


    private static final String TAG = "GoogleActivity";
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private Starter st;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth=FirebaseAuth.getInstance();
        this.setFinishOnTouchOutside(false);
        signin();

        st=new Starter(this);
        //Object ob=st.execute().get();
        st.execute();

        /*
        MessageSender ms2=new MessageSender();
        ms2.andid=id(this);
        Log.e("::::::::",ms2.andid);*/


    }
    private void signin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    void updateUI(String msg){
        Log.e(":::FIREBASE",msg);
    }
    @Override
    protected void onStart(){
        super.onStart();
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null) {
            updateUI(account.getEmail().toString());
            //firebaseAuthWithGoogle(account);
        }else
            updateUI("not signed in");

    }
    @Override
    public void onActivityResult(int requestcode,int resultcode,Intent data){
        super.onActivityResult(requestcode, resultcode, data);
        if(requestcode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task =GoogleSignIn.getSignedInAccountFromIntent(data);
            //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Intent mainClass = new Intent(SplashActivity.this, MainActivity.class);
                mainClass.putExtra("BGVAR",bgvar);
                //new Starter(this).execute();
                startActivity(mainClass);
                finish();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                updateUI("Google sign in failed"+ e.toString());
                // [START_EXCLUDE]
                signin();
                // [END_EXCLUDE]
            }

        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        updateUI("firebaseAuthWithGoogle:" + acct.getId());
        Toast.makeText(SplashActivity.this, "Signed in with \n"+acct.getEmail().toString(), Toast.LENGTH_SHORT).show();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "getInstanceId failed:::", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("TOKEN:::",token);
                        MessageSender ms2=new MessageSender();
                        ms2.andid=token;
                        updateUI(token);
                    }
                });




        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            updateUI("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user.getEmail().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI("signInWithCredential:failure"+task.getException());
                            updateUI("Authentication Failed.");
                            bgvar=-900;

                        }

                        // ...
                    }
                });
    }



    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
    public class Starter extends AsyncTask<String,String,Integer> {
        public int flag=0;
        public int flag2=0;
        public String reply=null;
        Socket s;
        DataInputStream din;
        PrintWriter pw;
        private FileOutputStream fos = null;
        int port;
        Context context;

        public Starter(Context mContext) {
            this.context = mContext;
        }


        @Override
        protected Integer doInBackground(String... voids){

// ...
// Initialize Firebase Auth
           // mAuth = FirebaseAuth.getInstance();
            int n=0;

            try {
                MessageSender ms=new MessageSender();
                ms.getHTML();
                Log.e(":::",versionapp);
                if(ms.versioncode==null){
                    //new networking().inter_avail=false;
                    return -2;
                }
                Log.e(":::",ms.versioncode);
                if(!(Integer.valueOf(versionapp)==Integer.valueOf(ms.versioncode)))
                {
                    return -100;
                }
                String address=ms.ip;
                port=ms.port;

                s = new Socket(address, port);
                pw = new PrintWriter(s.getOutputStream());
                this.publishProgress("SOCKET CREATED");
                 din =new DataInputStream(s.getInputStream());
                //in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                pw.format("SEND");
                //n=0;
               // while(n<500){n+=1;}
             //   this.publishProgress("SEND");
                pw.flush();

                pw.format("cetdcdetails.txt");
               // this.publishProgress("cetdcdetails.txt");
                pw.flush();


                try {

                    FileOutputStream fos = openFileOutput("cetdcdetails.txt",Context.MODE_PRIVATE);

                 //   this.publishProgress("file started");
                    // Get content in bytes and write to a file
                    byte[] buffer = new byte[8192];
                    for(int counter=0; (counter = din.read(buffer, 0, buffer.length)) >= 0;)
                    {
                        fos.write(buffer, 0, counter);
                    }
                    flag=1;
                    fos.flush();
                    fos.close();


                    // this.publishProgress("file ended");
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
                din.close();
                pw.close();
                s.close();
                s=null;
                pw=null;
                din=null;
                s = new Socket(address, port);
                pw = new PrintWriter(s.getOutputStream());
                //     this.publishProgress("SOCKET CREATED");
                din =new DataInputStream(s.getInputStream());
                pw.flush();
                pw.format("SEND");
                //   this.publishProgress("SEND");
                pw.flush();
                pw.format("cetdcuses.txt");
                // this.publishProgress("cetdcdetails.txt");
                pw.flush();
                try {

                    FileOutputStream fos = openFileOutput("cetdcuses.txt",Context.MODE_PRIVATE);

                    byte[] buffer = new byte[8192];
                    for(int counter=0; (counter = din.read(buffer, 0, buffer.length)) >= 0;)
                    {
                        fos.write(buffer, 0, counter);
                    }
                    flag=1;
                    fos.flush();
                    fos.close();


                    // this.publishProgress("file ended");
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
                din.close();
                pw.close();
                s.close();
            }catch (IOException e){
                e.printStackTrace();
                Log.e(":::","FILES NOT LOADED");
                return 0;
            }
            flag=2;
            return 1;
        }
        @Override
        protected void onPostExecute(Integer fl) {
            bgvar=fl;

            if(fl==-100){
                Log.e(":::","-100");
                AlertDialog.Builder goLogin = new AlertDialog.Builder(context);
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
            }/*
            if(fl==0){
                Toast.makeText(getApplicationContext(),"Error connecting  with Server \n May be server has not started yet..", Toast.LENGTH_LONG).show();
                finish();
            }
            else if(fl==-2)
            {

                Toast.makeText(getApplicationContext(),"No or slow internet connection found..", Toast.LENGTH_LONG).show();
                finish();
            }*/
            /*
            else {
                Intent mainClass = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainClass);
                finish();

            }*/
        }



    }

}
