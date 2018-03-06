/*
InClass_05
Bhanu Teja Sriram
Tejaswini Naredla


*/
package com.example.cherr.inclass5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button go;
    ImageView prev, next, imageView;
    TextView search;
    String selectedKeyword;
    RequestParams params;
    ArrayList<String> keywords_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go = (Button) findViewById(R.id.buttonGo);
        prev = (ImageView) findViewById(R.id.imageViewPrev);
        next = (ImageView) findViewById(R.id.imageViewNext);
        imageView = (ImageView) findViewById(R.id.imageViewSelectedImage);
        search = (TextView) findViewById(R.id.textViewSearch);
        prev.setClickable(false);
        next.setClickable(false);
       final Toast no_image= (Toast) Toast.makeText( MainActivity.this, "No Images Found", Toast.LENGTH_SHORT);
        if(isConnected()){
            Log.d("demo", "Connected to Internet: ");
            new getKeywordsAsync().execute("http://dev.theappsdr.com/apis/photos/keywords.php");

        }
        else{
            Log.d("demo", "Not Connected to Internet");
        }

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder keywords = new AlertDialog.Builder(MainActivity.this);
                keywords.setCancelable(false);
                keywords.setTitle("Choose a Keyword");
                final ArrayAdapter<String> passwordsAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, keywords_list);
                Log.d("Demo", String.valueOf(keywords_list.size()));
                keywords.setAdapter(passwordsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedKeyword = keywords_list.get(which);
                        search.setText(selectedKeyword);
                        params=new RequestParams();
                        params.addParameter("keyword",search.getText().toString());
                        if(isConnected()) {
                            new GetUrlAsync(params, imageView, prev, next, MainActivity.this, no_image).execute("http://dev.theappsdr.com/apis/photos/index.php");
                        }

                        else{
                            Toast.makeText(MainActivity.this,"There is no Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                        }
                });
                keywords.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()) {


                    if (params.getIndex() == params.getRequestURL().size() - 1) {
                        params.setIndex(0);
                        // Log.d("Demorrr", "Loop1.." + .getParams1().size() + requestURL.getIndex());

                    } else {
                        params.setIndex(params.getIndex() + 1);
                        // Log.d("Demorrr", "Loop3s.." + requestURL.getParams1().size() + requestURL.getIndex());

                    }
//
//                int i = (params.getIndex())+1;
                    new GetImageAsync(imageView, prev, next, MainActivity.this).execute(params.getRequestURL().get(params.getIndex()));
                }
                else{
                    Toast.makeText(MainActivity.this,"There is no Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()) {
                    if (params.getIndex() == 0) {
                        params.setIndex(params.getRequestURL().size() - 1);
                        // Log.d("Demorrr", "Loop1.." + .getParams1().size() + requestURL.getIndex());

                    } else {
                        params.setIndex(params.getIndex() - 1);
                        // Log.d("Demorrr", "Loop3s.." + requestURL.getParams1().size() + requestURL.getIndex());

                    }


//                int i = (params.getIndex())+1;
                    new GetImageAsync(imageView, prev, next, MainActivity.this).execute(params.getRequestURL().get(params.getIndex()));
                }

                else{
                    Toast.makeText(MainActivity.this,"There is no Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cm.getActiveNetworkInfo();
        if (nf == null || !nf.isConnected()) {
            return false;
        }
        return true;
    }

    private class getKeywordsAsync extends AsyncTask<String, Void, ArrayList> {
        @Override
        protected ArrayList doInBackground(String... strings) {
            Log.d("demo", "Inside Async");
            StringBuilder sb = new StringBuilder();
            HttpURLConnection con = null;
            BufferedReader br = null;
            String[] keywords=null;
            ArrayList<String> keywords_api = new ArrayList<>();
            String result = null;

            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection) url.openConnection();
                con.connect();
                InputStream is = con.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = sb.toString();
                    Log.d("demo", result);
                    if (result != null) {
                        keywords=  (result.split(";"));
                        for(int i=0;i<keywords.length;i++){
                            keywords_api.add(keywords[i]);
                            }
                    }
                    Log.d("demo", keywords[0]);
                   Log.d("demo", String.valueOf(keywords_api.size()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //Log.d("demo", String.valueOf(keywords_api.size()));
            return keywords_api;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {

            if(arrayList.size()>0){
               keywords_list=arrayList;
                Log.d("demo", "Post execute"+String.valueOf(keywords_list.size()));
            }
            Log.d("demo", "Click GO");
        }
    }




}

