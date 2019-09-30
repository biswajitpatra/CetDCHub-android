package com.example.raja.cetdchub;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import static android.content.Context.WIFI_SERVICE;

public class networking {
public static boolean inter_avail=false;
public static boolean conn_cetmmuss=false;
public static boolean init_mob_net=false;
    public static boolean init_wifi;
public Context ctx;
public networking(Context cnt){
    ctx=cnt;
}

public void chkStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
            init_wifi=true;
            init_mob_net=false;
            WifiManager wifiManager = (WifiManager) ctx.getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo;
            String ssid;
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                ssid = wifiInfo.getSSID();
                if(ssid.equals("CET MMUSS")){
                    conn_cetmmuss=true;
                }
            }
        } else if (mobile.isConnectedOrConnecting ()) {
           init_mob_net=true;
            init_wifi=false;
        } else {
           inter_avail=false;
        }
}
public int connect_to_cet(){
     chkStatus();
     if(conn_cetmmuss==false){
         try {
             WifiConfiguration wifiConfig = new WifiConfiguration();
             wifiConfig.SSID = String.format("\"%s\"", "CET MMUSS");
             wifiConfig.preSharedKey = String.format("\"%s\"", "0864297531");

             WifiManager wifiManager = (WifiManager) ctx.getSystemService(WIFI_SERVICE);
             //remember id
             int netId = wifiManager.addNetwork(wifiConfig);
             wifiManager.disconnect();
             wifiManager.enableNetwork(netId, true);
             wifiManager.reconnect();
         }catch (Exception e){
             return -1;
         }
     }
     return 1;
}
public void back_to_ori(){
       if(conn_cetmmuss==false){
           WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
           wifiManager.setWifiEnabled(false);
       }
    }




}
