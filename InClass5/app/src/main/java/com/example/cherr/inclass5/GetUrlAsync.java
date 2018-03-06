/*
InClass_05
Bhanu Teja Sriram
Tejaswini Naredla


*/

package com.example.cherr.inclass5;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cherr.inclass5.MainActivity;
import com.example.cherr.inclass5.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetUrlAsync extends AsyncTask<String,Void,ArrayList<String>> {
    RequestParams mparams;
    private ProgressDialog dialog;
    Context act;
    ImageView ig;
    ImageView p,n;
    ArrayList<String> s = new ArrayList<>();
    ArrayList<String> url = new ArrayList<>();
    Toast tst;

    public GetUrlAsync(RequestParams params, ImageView iv, ImageView ip, ImageView in,Context con,Toast msg) {
        mparams = params;
        ig = iv;
        p=ip;
        n=in;
        dialog = new ProgressDialog(con);
        act = con;
        tst=msg;
    }

    private Context context;

    protected void onPreExecute() {
        this.dialog.setMessage("Loading Dictionary");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        url = mparams.getRequestURL();
        if(url.size()>0) {
            if(url.size()>1){
                p.setClickable(true);
                n.setClickable(true);
            }
            new GetImageAsync(ig, p, n, act).execute(url.get(mparams.getIndex()));
        }else
        {
            ig.setImageBitmap(null);
            tst.show();
        }


    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {

        StringBuilder sb = new StringBuilder();

        HttpURLConnection con = null;
        BufferedReader br = null;
        String result = null;

        try {
            Log.d("Demo", "In try ");
            Log.d("Demo", params[0]);
            URL url = new URL(mparams.getEncodeUrl(params[0]));

            Log.d("Demo", "In Back" + params[0]);
            con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("POST");
//                mparams.encodePostParameters(con);
            con.connect();
            InputStream is = con.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            Log.d("Demo", "In Image Async");
            while ((line = br.readLine()) != null) {
                s.add(line);
                Log.d("Demo", line);
            }
            mparams.setIndex(0);
            mparams.setRequestURL(s);
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //result= sb.toString();
            }
            // Log.d("Demo","In Back"+result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        return s;

    }


}

