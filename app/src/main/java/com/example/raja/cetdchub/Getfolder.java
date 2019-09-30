package com.example.raja.cetdchub;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Getfolder extends AppCompatActivity {
    //getfolderparts[] gfp;
    public static int multi;
    public static String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfolder);
        Intent intent = getIntent();
        //message = Main3Activity.GETNOFILE;
        message=intent.getStringExtra("FILENAME");

        ActivityCompat.requestPermissions(Getfolder.this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                100);
        /* Request user permissions in runtime */
/*
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e("EXTERNAL",storagePath);
        String rootPath = storagePath + "/test";
        String fileName = "/test.zip";

        File root = new File(rootPath);
        if(!root.mkdirs()) {
            Log.i("Test", "This path is already exist: " + root.getAbsolutePath());
        }

        File file = new File(rootPath + fileName);
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(Getfolder.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (!file.createNewFile()) {
                    Log.i("Test", "This file is already exist: " + file.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        Log.e(":::",message);
        getfolder gft=new getfolder();
        gft.execute(message);
        //gfp[0] = new getfolderparts();
        //startMyTask(gfp[0], "A" + ".zip" + "." + Integer.toString(1));


    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(AsyncTask asyncTask,String... params) {
        Log.e("parrallel started::::","done");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //int corePoolSize = 600;
            //int maximumPoolSize = 800;
            //int keepAliveTime = 1000;
            //BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
            //Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        }
            else
            asyncTask.execute(params);
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0
                        || grantResults[0] == PackageManager.PERMISSION_GRANTED
                        || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    /* User checks permission. */

                } else {
                    Toast.makeText(Getfolder.this, "Permission is denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //return; // delete.
        }
    }
    /*
    static void readZip(ZipOutputStream outStream, File targetFile)
            throws Exception {
        ZipInputStream inStream = new ZipInputStream(new FileInputStream(
                targetFile));
        byte[] buffer = new byte[1024];
        int len = 0;

    for (ZipEntry e; (e = inStream.getNextEntry()) != null; ) {
        outStream.putNextEntry(e);
        while ((len = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, len);
        }
    }

        inStream.close();

        //
    }*/
/*
    public static void readZip(File zipFile,File parts){

        try {
            PrintWriter pw = new PrintWriter(zipFile);
            BufferedReader br = new BufferedReader(new FileReader(parts));
            String line = br.readLine();

            while (line != null)
            {
                pw.println(line);
                line = br.readLine();
            }
            pw.flush();
            // closing resources
            br.close();
            pw.close();
            Log.e("JOINED :::",parts.getAbsolutePath().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("PROBLEM JOIN:::",e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("PROBLEM JOIN2:::",e.toString());
        }


    }*/


    public class getfolderparts extends AsyncTask<String,String,Void>{
        public int flag=0;
        protected Void doInBackground(String... voids){
            MessageSender ms=new MessageSender();
            String address=ms.ip;
            int port=ms.port;
            Log.e("gfp:::","strated");
          try{

            Socket s = new Socket(address, port);

            PrintWriter pw = new PrintWriter(s.getOutputStream());
            DataInputStream din =new DataInputStream(s.getInputStream());
            pw.format("SEND");
            pw.flush();
            pw.format(voids[0]);
            pw.flush();
            try {

                String namFile = Environment.getExternalStorageDirectory().getAbsolutePath();
                File thatfile=new File(namFile+"/"+voids[0]);
                //if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                thatfile.createNewFile();
                Log.e("PERMISSION:::","GRANTED");
                FileOutputStream fos = new FileOutputStream(thatfile);
                byte[] buffer = new byte[1024 * 20];
                for(int counter=0; (counter = din.read(buffer, 0, buffer.length)) >= 0;)
                {
                    fos.write(buffer, 0, counter);
                }
                fos.flush();
                fos.close();
                flag=1;

                // this.publishProgress("file ended");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR THREAD:::",e.toString());

            }
            din.close();
            pw.close();
            s.close();


        }catch(Exception e){
              Log.e("ERROR THREAD:::",e.toString());
              return null;
            }

            return null;
    }}
    public class getfolder extends AsyncTask<String,String,Integer> {
        Socket s;
        DataInputStream din;
        PrintWriter pw;
        private FileOutputStream fos = null;
        BufferedReader in;
        String reply;
        getfolderparts[] gfp;

        public void MergeFileExample(File target,String added) {
            FileOutputStream fos;
            FileInputStream fis;
            byte[] fileBytes;
            int bytesRead = 0;
            List<File> list = new ArrayList<File>();
            for(int i=1;i<multi+1;i++){
                list.add(new File(added+Integer.toString(i)));
                Log.e("File",added+Integer.toString(i));
            }
            try {
                int nig=-1;
                fos = new FileOutputStream(target,true);
                for (File file : list) {
                    nig++;
                    if(nig!=0)
                        while (gfp[nig].flag != 1) ;
                    fis = new FileInputStream(file);
                    fileBytes = new byte[(int) file.length()];
                    bytesRead = fis.read(fileBytes, 0,(int)  file.length());
                    assert(bytesRead == fileBytes.length);
                    assert(bytesRead == (int) file.length());
                    fos.write(fileBytes);
                    fos.flush();
                    fileBytes = null;
                    fis.close();
                    //fis = null;
                    file.delete();
                    Log.e("JOINED :::",fis.toString());
                }
                //fos.delete();
                fos.close();
                fos = null;
            }catch (Exception exception){
                exception.printStackTrace();
                Log.e("MERGE ERROR:::",exception.toString());
            }
        }





        public void unzip(File zipFile, File targetDirectory) throws IOException {

            zipFile.createNewFile();
            //ZipOutputStream zin = new ZipOutputStream(new FileOutputStream(zipFile));
/*
        for(int i=1;i< multi+1;i++)
        {
            File fl=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + message + ".zip"+"."+Integer.toString(i));
            try {
                readZip(zipFile,fl);
                //fl.delete();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ZIPPING ERORR :::"+Integer.toString(i),e.toString());
            }

        }*/
            MergeFileExample(zipFile,Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + message + ".zip"+".");

            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));
            try {
                ZipEntry ze;
                int count;
                byte[] buffer = new byte[1024*10];
                while ((ze = zis.getNextEntry()) != null) {
                    File file = new File(targetDirectory, ze.getName());
                    File dir = ze.isDirectory() ? file : file.getParentFile();
                    if (!dir.isDirectory() && !dir.mkdirs())
                        throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                    if (ze.isDirectory())
                        continue;
                    FileOutputStream fout = new FileOutputStream(file);
                    try {
                        while ((count = zis.read(buffer)) != -1)
                            //Log.e("Counter:::",Integer.toString(count));
                            fout.write(buffer, 0, count);
                    } finally {

                        fout.close();
                    }

                    Log.e(":::","UNZIPPED");

                }
            } finally {
                zis.close();
            }
            zipFile.delete();

        }

        @Override
        protected Integer doInBackground(String... voids) {
            try {
                //int permissionCheck = ContextCompat.checkSelfPermission(Getfolder.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                MessageSender ms = new MessageSender();
                if(ms.done==false)
                    ms.getHTML();
                String address = ms.ip;
                int port = ms.port;

                s = new Socket(address, port);
                pw = new PrintWriter(s.getOutputStream());
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                din = new DataInputStream(s.getInputStream());

                this.publishProgress("SOCKET CREATED");
                pw.format("SEFO");
                this.publishProgress("SEFO");
                pw.flush();
                pw.write(voids[0]);
                this.publishProgress("Getting zip file..");
                pw.flush();
                while (!in.ready()) ;
                this.publishProgress("Checking the reply .... ");
                reply = "";
                while (in.ready()) {
                    reply = reply + (char) in.read();
                }
                pw.flush();
                this.publishProgress("Reply from server:" + reply);
                pw.flush();


                multi = Integer.valueOf(reply.substring(3));
                gfp = new getfolderparts[multi];
                for(int ni=1;ni<multi;ni++)
                {
                    gfp[ni] = new getfolderparts();
                    startMyTask(gfp[ni], voids[0] + ".zip" + "." + Integer.toString(ni+1));
                }

                try {
                    String namFile = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File thatfile = new File(namFile + "/" + voids[0] + ".zip" + "." + Integer.toString(1));
                    //if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    thatfile.createNewFile();
                    Log.e("PERMISSION:::", "GRANTED");
                    FileOutputStream fOut = new FileOutputStream(thatfile);
                    this.publishProgress("file started");
                    byte[] buffer = new byte[1024 * 20];
                    // int noloops=0;//////////
                    for (int counter = 0; (counter = din.read(buffer, 0, buffer.length)) >= 0; ) {
                        // noloops+=1;////////////
                        // avg=((noloops-1)*(avg/noloops)+counter/noloops);////////////
                        Log.e("BUFFER :::", Integer.toString(counter));/////////
                        fOut.write(buffer, 0, counter);
                    }
                    //Log.e("AVG:::",Double.toString(avg));///////////////
                    // Log.e("NOOFLOOPS",Integer.toString(noloops));////////////
                    fOut.flush();
                    fOut.close();
                    this.publishProgress("file ended");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(":::", "ERROR1" + e.toString());
                    return 0;
                }

                in.close();
                din.close();
                pw.close();
                s.close();
                s = null;
                pw = null;
                din = null;
                this.publishProgress("Waiting for files");
                //for(int ni=1;ni<multi;ni++)
                // while (gfp[ni].flag != 1) ;
                //this.publishProgress("ALL ZIPS READY");

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(":::", "ERROR" + e.toString());
                return 0;
            }
            this.publishProgress("Starting opening zip...");


            try {
                unzip(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + voids[0] + ".zip"), new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(":::UNZIP", "NOT ZIPPING"+e.toString());
            }
            this.publishProgress("^^^  ENJOY WITH YOUR FILES  ^^^");
            return 1;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TextView display = (TextView) findViewById(R.id.textView);
            String yourtext = display.getText().toString();
            int spacebar, i;
            spacebar = 0;
            i = 0;
            while (i < yourtext.length()) {
                if (yourtext.charAt(i) == '\n') {
                    //arr=arr+"|"+Integer.toString(i);
                    spacebar++;
                }
                i++;
            }
            if (spacebar > 12)
                yourtext = yourtext.substring(yourtext.indexOf('\n'));
            display.setText(yourtext + "\n" + values[0]);

        }

        @Override
        protected void onPostExecute(Integer res) {

            AlertDialog.Builder alert = new AlertDialog.Builder(Getfolder.this);
            alert.setTitle("DOWNLOADED");
            if(res==1) {
                alert.setMessage("Done");
            }
            else
                alert.setMessage("Not done");
            alert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                            dialog.dismiss();
                        }
                    });
            alert.show();

        }
        }
    }
    /*
    public class getfolder extends AsyncTask<String,String,Integer>{
        Socket s;
        DataInputStream din;
        PrintWriter pw;
        private FileOutputStream fos = null;
        BufferedReader in;
        String reply;
        @Override
        protected Integer doInBackground(String... voids){
            try {
                MessageSender ms=new MessageSender();
                String address=ms.ip;
                int port=ms.port;

                s = new Socket(address, port);
                pw = new PrintWriter(s.getOutputStream());
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                din =new DataInputStream(s.getInputStream());

                this.publishProgress("SOCKET CREATED");
                pw.format("SEFO");
                this.publishProgress("SEFO");
                pw.flush();
                pw.format(voids[0]);
                this.publishProgress("Getting files");
                pw.flush();
                try {
                    while(true) {
                        while (!in.ready()) ;
                        this.publishProgress("Checking the reply .... ");
                        reply = "";
                        while (in.ready()) {
                            reply = reply + (char) in.read();
                        }
                        pw.flush();
                        this.publishProgress("Reply from server:" + reply);
                        Log.e("RESUlT::::::",reply);
                        if(reply.equals("COMD")){
                            break;
                        }
                        reply=reply.substring(8);
                        reply = reply.replace("\\", "/");
                        String[] replya=reply.split(" ");
                        String namFile = Environment.getExternalStorageDirectory() + "/" + replya[1];
                       // String thatfilest=Environment.getExternalStorageDirectory() + "/" + replya[0];
                        this.publishProgress("Folder: " + namFile);
                        File datfile = new File(namFile);
                        Log.e("NO ERROR:::","OK");
                        datfile.mkdirs();
                        File thatfile=new File(Environment.getExternalStorageDirectory() + "/" + replya[0]);
                        thatfile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(thatfile);
                        pw.write("NEXT");
                        pw.flush();
                        this.publishProgress("NEXT");
                        this.publishProgress("file started");
                        // Get content in bytes and write to a file
                        byte[] buffer = new byte[8192];
                        for (int counter = 0; (counter = din.read(buffer, 0, buffer.length)) >= 0; ) {
                            //fos.write(buffer, 0, counter);
                            fOut.write(buffer, 0, counter);
                        }
                        fOut.flush();
                        fOut.close();
                        this.publishProgress("file ended");
                        in.close();
                        din.close();
                        pw.close();
                        s.close();
                        s = null;
                        pw = null;
                        din = null;

                        s = new Socket(address, port);
                        pw = new PrintWriter(s.getOutputStream());
                        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        din = new DataInputStream(s.getInputStream());

                        pw.format("THEN");
                        pw.flush();
                        this.publishProgress("THEN");



                        // this.publishProgress("file ended");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(":::","ERROR"+e.toString());
                    return 0;
                }
                din.close();
                pw.close();
                s.close();
            }catch (IOException e){
                e.printStackTrace();
                Log.e(":::","ERROR"+e.toString());
                return 0;
            }

            return 1;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TextView display = (TextView) findViewById(R.id.textView);
            String yourtext= display.getText().toString();
            int spacebar,i;
            spacebar=0;
            i=0;
            while( i < yourtext.length() ){
                if( yourtext.charAt(i) == '\n' ) {
                    //arr=arr+"|"+Integer.toString(i);
                    spacebar++;
                }
                i++;
            }
            if(spacebar>12)
                yourtext=yourtext.substring(yourtext.indexOf('\n'));
            display.setText(yourtext +"\n"+values[0]);
        }
        @Override
        protected void onPostExecute(Integer res) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Getfolder.this);
            alert.setTitle("DOWNLOADED");
            if(res==1) {
                alert.setMessage("Done");
            }
            else
                alert.setMessage("Not done");
            alert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                            dialog.dismiss();
                        }
                    });
            alert.show();

        }
    }*/

