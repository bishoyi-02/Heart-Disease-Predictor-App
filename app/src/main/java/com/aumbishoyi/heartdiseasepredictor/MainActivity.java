package com.aumbishoyi.heartdiseasepredictor;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    Thread thread;
    Boolean flag;
    public void run(View view){
        if(!flag) {
            Log.i("msg0", "Thread Started");
            thread.start();
            flag=true;
        }else{
            Log.i("msg0", "Thread Run");
            thread.run();
        }
//        thread.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag=false;
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        EditText sex = (EditText) findViewById(R.id.sex);
        EditText age = (EditText) findViewById(R.id.age);
        EditText cp = (EditText) findViewById(R.id.cp);
        EditText trestbps= (EditText) findViewById(R.id.trestbps);
        EditText cholestrol = (EditText) findViewById(R.id.cholestrol);
        EditText fbs = (EditText) findViewById(R.id.fbs);
        EditText restecg = (EditText) findViewById(R.id.restecg);
        EditText thalach = (EditText) findViewById(R.id.thalach);
        EditText exang = (EditText) findViewById(R.id.exang);
        EditText oldpeak = (EditText) findViewById(R.id.oldpeak);
        EditText slope = (EditText) findViewById(R.id.slope);
        EditText ca = (EditText) findViewById(R.id.ca);
        EditText thal = (EditText) findViewById(R.id.thal);
        thread = new Thread(new Runnable(){

            @Override
            public void run() {

                URL url = null;
                try {
                    url = new URL("http://192.168.184.38:5000");
//                    url = new URL("https://www.google.com/");
                } catch (Exception e) {
                    Log.i("msg0", String.valueOf(e));
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
//                    Log.i("msg0","code:"+conn.getResponseCode());
                } catch ( Exception e) {
                    Log.i("msg0", String.valueOf(e));
                }
                try {
                    conn.setRequestMethod("POST");
                } catch (Exception e) {
                    Log.i("msg0", String.valueOf(e));
                }
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                String sex_ ;
                if(sex.getText().toString().toLowerCase().equals("male")){
                    sex_="1";
                }else if(sex.getText().toString().toLowerCase().equals("female")){
                    sex_="0";
                }else{
                    sex_=sex.getText().toString();
                }
                Log.i("msg0",sex_);

//                String jsonInputString = "{'age':66,'sex':0,'cp':0,'trestbps':178,'chol':228,'fbs':1,'restecg':1,'thalach':165,'exang':1,'oldpeak':1,'slope':1,'ca':2,'thal':3}";
                String jsonInputString = String.format("{\"age\":%s,\"sex\":%s,\"cp\":%s,\"trestbps\":%s,\"chol\":%s,\"fbs\":%s,\"restecg\":%s,\"thalach\":%s,\"exang\":%s,\"oldpeak\":%s,\"slope\":%s,\"ca\":%s,\"thal\":%s}",age.getText().toString(),sex_,cp.getText().toString(),trestbps.getText().toString(),cholestrol.getText().toString(),fbs.getText().toString(),restecg.getText().toString(),thalach.getText().toString(),exang.getText().toString(),oldpeak.getText().toString(),slope.getText().toString(),ca.getText().toString(),thal.getText().toString());
                try(OutputStream os = conn.getOutputStream()){
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input,0,input.length);
                } catch (Exception e) {
                    Log.i("msg0", String.valueOf(e));
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(),"utf-8"))){
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while((responseLine= br.readLine())!=null){
                        response.append(responseLine.trim());
                    }
                    Log.i("msg0",response.toString());
                    if(response.toString().equals("healthy")){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this, "You are perfectly healthy.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                    }else{
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this, "You should see a doctor immediately.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                    }
                    thread.sleep(1000);
                    thread.interrupt();

                }catch(Exception e){
                    Log.i("msg0", String.valueOf(e));
                }
            }
        });





    }
}