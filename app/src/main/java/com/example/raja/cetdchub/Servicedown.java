package com.example.raja.cetdchub;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Servicedown extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {

                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
        zipFile.delete();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //getting systems default ringtone
        Socket s;
        DataInputStream din;
        PrintWriter pw;
        FileOutputStream fos = null;
        BufferedReader in;
        String reply;
        String voids=intent.getStringExtra("MyService.data");
        String address=intent.getStringExtra("MyService.ip");
        int port= intent.getIntExtra("MyService.port",-1);
        try {
            //int permissionCheck = ContextCompat.checkSelfPermission(Getfolder.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            s = new Socket(address, port);
            pw = new PrintWriter(s.getOutputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            din =new DataInputStream(s.getInputStream());

            Log.e(":::","SOCKET CREATED");
            pw.format("SEFO");
            Log.e(":::","SEFO");
            pw.flush();
            pw.format(voids);
            Log.e(":::","Getting zip file..");
            pw.flush();
            try {
                String namFile = Environment.getExternalStorageDirectory().getAbsolutePath();
                File thatfile=new File(namFile+"/"+voids+".zip");
                //if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                thatfile.createNewFile();
                Log.e("PERMISSION:::","GRANTED");
                FileOutputStream fOut = new FileOutputStream(thatfile);
               // this.publishProgress("file started");
                byte[] buffer = new byte[8192];
                for (int counter = 0; (counter = din.read(buffer, 0, buffer.length)) >= 0; ) {
                    fOut.write(buffer, 0, counter);
                }
                fOut.flush();
                fOut.close();
                Log.e(":::","file ended");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(":::","ERROR1"+e.toString());
            }

            in.close();
            din.close();
            pw.close();
            s.close();
            s = null;
            pw = null;
            din = null;
        }catch (IOException e){
            e.printStackTrace();
            Log.e(":::","ERROR"+e.toString());
        }
        Log.e(":::","Starting opening zip...");
        try {
            unzip(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+voids+".zip"),new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(":::UNZIP","NOT ZIPPING");
        }
        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed

    }
}
