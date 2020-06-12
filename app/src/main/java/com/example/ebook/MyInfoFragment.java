package com.example.ebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyInfoFragment extends Fragment {



    public MyInfoFragment() {
    }
    View rootView;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.pageview2, container, false);
        uploadTextview();
        downloadTextview();
        return rootView;
    }

    public void downloadTextview(){

        TextView downloadTextview=(TextView)rootView.findViewById(R.id.download);
        downloadTextview.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "同步", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),DownloadBookList.class);
                Bundle bundle = new Bundle();
                bundle.putString("BOOKNAME", "lsy");
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
    }

    public void uploadTextview(){

        TextView uploadTextview=(TextView)rootView.findViewById(R.id.upload);
        uploadTextview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                Toast.makeText(getContext(), "同步", Toast.LENGTH_SHORT).show();
                 Intent intent=new Intent(getContext(),UploadBookList.class);
                 startActivity(intent);

            }

        });
    }




    public  void uploadLogFile(Context context, String uploadUrl, String oldFilePath) throws IOException {
        File file = new File(oldFilePath);
        if(file==null){
            Log.e("文件","文件为空" );
        }else{
            Log.e("文件","文件不为空" );
        }
        MediaType MEDIA_TYPE_TXT = MediaType.parse("text/plain");
        RequestBody filebody = MultipartBody.create(MEDIA_TYPE_TXT, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
        Log.e("getname","" );
        multiBuilder.addFormDataPart("file", file.getName(), filebody);
        multiBuilder.setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(uploadUrl).post(multiBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的处理
                Log.e("文件","上传失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    Log.e("文件","上传成功" );
                    InputStream is = null;
                    String resultData = "";
                    is = response.body().byteStream();   //获取输入流
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bufferReader = new BufferedReader(isr);
                    String inputLine = "";
                    while ((inputLine = bufferReader.readLine()) != null) {
                        resultData += inputLine + "\n";
                    }
                    Log.e("返回内容","post方法取回内容：" + resultData);
                    parseJSONWithJSONObject(resultData,"bookname0");
                }
            }
        });
    }

    //方法一：使用JSONObject
    public static String parseJSONWithJSONObject(String JsonData,String key) {
        String str="";
        try
        {
            JSONObject jsonObject = new JSONObject(JsonData);//每一层都是一个Object对象
            str=jsonObject.get(key).toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }
//        final String IMGUR_CLIENT_ID = "...";
//        //final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//
//        final OkHttpClient client = new OkHttpClient();
//
//
//        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("title", "Square Logo")
//                .addFormDataPart("image", "logo-square.png",
//                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
//                .build();
//
//        Request request = new Request.Builder()
//                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
//                .url("https://api.imgur.com/3/image")
//                .post(requestBody)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        //System.out.println(response.body().string());
//        Log.e("response",response.body().string());
//
//
//
//
//
//
//
//        try {
//            URL url = new URL(uploadUrl);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//            // 允许Input、Output，不使用Cache
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//
//            con.setConnectTimeout(50000);
//            con.setReadTimeout(50000);
//            // 设置传送的method=POST
//            con.setRequestMethod("POST");
//            //在一次TCP连接中可以持续发送多份数据而不会断开连接
//            con.setRequestProperty("Connection", "Keep-Alive");
//            //设置编码
//            con.setRequestProperty("Charset", "UTF-8");
//            //text/plain能上传纯文本文件的编码格式
//            con.setRequestProperty("Content-Type", "text/plain");
//
//            // 设置DataOutputStream
//            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//
//            // 取得文件的FileInputStream
//            FileInputStream fStream = new FileInputStream(oldFilePath);
//            if(fStream==null){
//                Log.e("文件","文件为空");
//            }else{
//                Log.e("文件","文件不为空");
//            }
//            // 设置每次写入1024bytes
//            int bufferSize = 1024;
//            byte[] buffer = new byte[bufferSize];
//
//            int length = -1;
//            // 从文件读取数据至缓冲区
//            while ((length = fStream.read(buffer)) != -1) {
//                // 将资料写入DataOutputStream中
//                ds.write(buffer, 0, length);
//            }
//            ds.flush();
//            fStream.close();
//            ds.close();
//            if (con.getResponseCode() == 200) {
//                //logger.info("文件上传成功！上传文件为：" + oldFilePath);
//                Log.e("上传", "上传成功");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
////                    logger.info("文件上传失败！上传文件为：" + oldFilePath);
////                    logger.error("报错信息toString：" + e.toString());
//            Log.e("上传", "上传失败");
//            Log.e("上传", "上传失败"+e.toString());
//        }

}