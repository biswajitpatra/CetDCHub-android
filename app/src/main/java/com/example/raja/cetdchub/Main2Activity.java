package com.example.raja.cetdchub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.net.*;
import java.io.*;

import java.util.Scanner;


public class Main2Activity extends AppCompatActivity {
   /*
    private Socket socket = null;
    private FileOutputStream fos = null;
    private DataInputStream din = null;
    private PrintWriter pout = null;
    private Scanner scan = null;
    private BufferedReader in =null;


    public InetAddress iaddress;
*/

    public String messagetext ="Starting  ...";
    public String address;
    public int port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra("USERLINK");
        // Capture the layout's TextView and set the string as its text
        print("Requesting  "+ message);
        print("Initializing Client");
        String response="\n ";
        //String messag = intent.getStringExtra(MainActivity.USER_IP);
        //address = messag;
        MessageSender send =new MessageSender();
        send.execute(message,address);
        print("Background started....");



/**
        try {
            iaddress = InetAddress.getByName(address);
            print("address recorded...");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
            print(response);
        }

        try {
        socket = new Socket(iaddress, port);
        print("socket created...");
       /* din = new DataInputStream(socket.getInputStream());
        pout = new DataOutputStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        }
        print(response);
        print("Connection completed....");


        try {
            send("CHAT");
            send("REQUEST "+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }
    public void print(String msg)
    {
        TextView textView = findViewById(R.id.textView);
        this.messagetext =  this.messagetext + '\n'+msg;
        textView.setText(messagetext);
    }
/**
    public void send(String msg) throws IOException
    {
        pout.print(msg);
        print(msg);
        pout.flush();
    }

    public String recv() throws IOException
    {
        byte[] bytes = new byte[1024];
        din.read(bytes);
        String reply = new String(bytes, "UTF-8");
        print("Inside recv(): ");
        return reply;
    }

    public void closeConnections() throws IOException
    {
        // Clean up when a connection is ended
        socket.close();
        din.close();
        pout.close();
        scan.close();
    }

    public void chat() throws IOException
    {
        String response = "s";

        print("Initiating Chat Sequence.... ");
        while(!response.equals("QUIT")){
            print("Client: ");
            String message = scan.nextLine();
            send(message);
            if(message.equals("QUIT"))
                break;
            response = recv();
            print("Server: " + response);
        }
        closeConnections();
    }

    // Request a specific file from the server
    public void getFile(String filename)
    {
        print("Requested File: "+filename);
        try {
            File file = new File(filename);
            // Create new file if it does not exist
            // Then request the file from server
            if(!file.exists()){
                file.createNewFile();
                print("Created New File: "+filename);
            }
            fos = new FileOutputStream(file);
            send(filename);

            // Get content in bytes and write to a file
            byte[] buffer = new byte[8192];
            for(int counter=0; (counter = din.read(buffer, 0, buffer.length)) >= 0;)
            {
                fos.write(buffer, 0, counter);
            }
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
public class MessageSender extends AsyncTask<String,String,Integer> {
    public int flag=0;
    public int flag2=0;
    public String reply=null;
    Socket s;
   // DataInputStream dis;
    PrintWriter pw;
    BufferedReader in;


    @Override
    protected Integer doInBackground(String... voids){

        String message =voids[0];
        String address;


        try {
            com.example.raja.cetdchub.MessageSender ms=new com.example.raja.cetdchub.MessageSender();
           // ms.getHTML();
            address=ms.ip;
            port=ms.port;
            s = new Socket(address, port);
            pw = new PrintWriter(s.getOutputStream());
            // dis =new DataInputStream(s.getInputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.publishProgress("Server connected....");
            pw.write("CHAT");
            this.publishProgress("Chatting started....");
            pw.flush();
            pw.write("REQUESTING " + ms.andid+" "+message);
            this.publishProgress("REQUESTING " + ms.andid+" "+message);
            pw.flush();
            /*
            int _sizeOfMessage=dis.readInt();//sends int value from server, no string
            byte[] _data=new byte[_sizeOfMessage];
            dis.read(_data);
            reply = new String(_data, "UTF-8");
            */


            while(! in.ready());
            this.publishProgress("Checking the reply .... ");
            reply= "";
            while (in.ready()) {
                reply = reply + (char) in .read();
            }

            if(reply=="DONE" ||reply=="NOTDONE")
                pw.flush();
            this.publishProgress("Reply from server:" + reply);

            /*reply = dis.readUTF();

             */
            if(reply.equals("DONE")) {
                flag = 1;
                this.publishProgress("!---ACCEPTED--!");
            }
            else
                this.publishProgress("### REJECTED ###");
            flag2=1;
            pw.write("QUIT");
            pw.flush();
            this.publishProgress("QUIT");

            //dis.close();
            in.close();
            pw.close();
            s.close();


        }catch (IOException e){
            e.printStackTrace();
            this.publishProgress("IOException: " + e.toString());
            this.publishProgress("### FAILED CONNECTION ###");
        }
        return flag;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        TextView display = (TextView) findViewById(R.id.textView);
        String yourtext= display.getText().toString();
        display.setText(yourtext +"\n"+values[0]);
    }
    @Override
    protected void onPostExecute(Integer res) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Main2Activity.this);
        alert.setTitle("REQUEST");
        if(res==1) {
            alert.setMessage("Request ACCEPTED");
        }
        else
            alert.setMessage("Request REJECTED");
        alert.setPositiveButton("Yes",
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
