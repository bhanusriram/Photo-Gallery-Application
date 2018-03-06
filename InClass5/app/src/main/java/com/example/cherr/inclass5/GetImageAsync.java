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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GetImageAsync extends AsyncTask<String, Void, Bitmap> {
    private ProgressDialog dialog;
        ImageView ig,p,n;
        ArrayList<String> s = new ArrayList<>();


        public GetImageAsync(ImageView iv,ImageView ip,ImageView in,Context con) {
            ig = iv;
            p=ip;
            n=in;
            dialog = new ProgressDialog(con);
        }

    private Context context;

    protected void onPreExecute() {
        this.dialog.setMessage("Loading Photo");
        this.dialog.show();
    }
        @Override
        protected Bitmap doInBackground(String... params) {
            HttpURLConnection con = null;
            Bitmap image = null;
            try {

                URL url = new URL(params[0]);

                Log.d("Demo", "In Back" + params[0]);
                con = (HttpURLConnection) url.openConnection();
                con.connect();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    image = BitmapFactory.decodeStream(con.getInputStream());
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

            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
        dialog.dismiss();
            if (ig != null && bitmap != null) {

                ig.setImageBitmap(bitmap);

            }

            super.onPostExecute(bitmap);
        }
    }
