/*
InClass_05
Bhanu Teja Sriram
Tejaswini Naredla


*/

package com.example.cherr.inclass5;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cherr on 12-02-2018.
 */
public class RequestParams {
    private ArrayList<String> RequestURL;
    private int index;
    private HashMap<String,String > params;
    private StringBuilder stringBuilder;

    public ArrayList<String> getRequestURL() {
        return RequestURL;
    }

    public void setRequestURL(ArrayList<String> requestURL) {
        RequestURL = requestURL;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }



    public RequestParams() {
        params=new HashMap<>();
        stringBuilder=new StringBuilder();

    }
    public RequestParams addParameter(String key,String value){
        try {
            params.put(key, URLEncoder.encode(value,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return this;
    }


    public String getEncodedParameters(){
        for(String key:params.keySet()){
            if(stringBuilder.length()>0){
                stringBuilder.append("&");
            }
            stringBuilder.append(key+"="+params.get(key));
        }
        return stringBuilder.toString();
    }

    public String getEncodeUrl(String url){
        return url+"?"+getEncodedParameters();
    }

    public void encodePostParameters(HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);
        OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
        writer.write(getEncodedParameters());
        writer.flush();
    }
}
