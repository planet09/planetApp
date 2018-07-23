package com.example.annew.planet;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendDataService extends Service {
    DataTask dataTask;
    public static String msg,money,name;


    public SendDataService() {

    }

    @Override
    public void onCreate() {
        Log.d("----------", "on create----------@@@");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("----------", "on startcommand----------@@@");

        if(intent==null){
            return Service.START_STICKY;
        }else{

            msg = intent.getStringExtra("msg");
            money= intent.getStringExtra("money");
            name= intent.getStringExtra("name");

            Log.d("-----------", msg);
            sendData();

        }
        return Service.START_NOT_STICKY;
    }


    public void sendData(){
        dataTask = new DataTask("http://70.12.245.108:3000/sendData/");
        dataTask.execute(msg);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class DataTask extends AsyncTask <String, String ,String >{

        String url;

        public DataTask() {}
        public DataTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            String query1="";
            String query2="";
            String msg= strings[0];
            try {
                query1 = URLEncoder.encode(money, "utf-8");
                query2 = URLEncoder.encode(name, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url += query1+"/"+query2;
            Log.d("-----------",url);

            StringBuilder sb = new StringBuilder();
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader reader=null;
            try {
                url = new URL(this.url);


                con = (HttpURLConnection) url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(10000);
                    //con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "*/*");
                    if (con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    reader =new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line="";
                    while(true){
                        line=reader.readLine();
                        if(line==null){
                            break;
                        }
                        sb.append(line);
                    }
                }
            } catch (Exception e) {
                return e.getMessage();
            } finally {
                con.disconnect();
                try {
                    if(reader!=null){
                        reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();

        }

        @Override
        protected void onPostExecute(String s) {

        }
    }



}
