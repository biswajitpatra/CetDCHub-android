package com.example.raja.cetdchub;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

class MessageSender
{   public static String versioncode;
    public static String ip;
    public static Integer port;
    public static String andid;
    public static String uniqueID = null;
    public static String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public static boolean done=false;



public Void getHTML()
{

    HttpURLConnection urlConnection = null;
    String result ="";
    int spaceCount,i;

    try {
        while(true) {
            URL url = new URL("https://cet-dc.herokuapp.com/client");
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();

            if (code == 200) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                }
                in.close();
            }

            String arr="from ";
            spaceCount = 0;
            i=0;
            result=result.trim();
            while( i < result.length() ){
                    if( result.charAt(i) == ' ' ) {
                        arr=arr+"|"+Integer.toString(i);
                        spaceCount++;
                    }
                    i++;
                }


            if(spaceCount==2) {
                Log.e("ALERT:::", result+" "+Integer.toString(spaceCount)+"|"+arr);
                break;
            }
            result="";
            //Log.e("RESULT::: ",result);
        }

            Log.e("RESULT::: ",result);
            String[] words = result.split(" ");
            ip = words[0];
            port = Integer.parseInt(words[1]);
            versioncode=words[2];
            done=true;



    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    finally {
        urlConnection.disconnect();
    }
    return null;
    }
}