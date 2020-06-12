package com.example.ebook;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.ebook.FindBooksFragment.db;
import static com.example.ebook.MyInfoFragment.parseJSONWithJSONObject;

public class UploadBookList extends AppCompatActivity {

    List<UploadBook> booklist=new LinkedList<UploadBook>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book_list);
        booklist();

        Log.e("onCreate: ","setAdapter" );
        UploadBookAdapter bookAdapter = new UploadBookAdapter((LinkedList<UploadBook>) booklist, UploadBookList.this);
        ListView lv = (ListView) findViewById(R.id.uploadLv);
        lv.setAdapter(bookAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long l) {

                Toast.makeText(UploadBookList.this, "开始上传", Toast.LENGTH_SHORT).show();

                Log.e("booklist","成功" );
                final TextView textView = (TextView)arg1.findViewById(R.id.ItemPath);
                try{
                    uploadLogFile(UploadBookList.this,"http://192.168.199.121:8080/uploadbook",textView.getText()+"");//192.168.43.98
                }catch(Exception e){
                    Log.e("upload_onItemClick: ",""+e.getMessage() );
                }


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
    Log.e("getname",""+file.getName() );
    multiBuilder.addFormDataPart("file", file.getName(), filebody);
    multiBuilder.setType(MultipartBody.FORM);
    RequestBody multiBody = multiBuilder.build();
    OkHttpClient okHttpClient = new OkHttpClient();
    Request request = new Request.Builder()
            .url("http://192.168.199.121:8080/uploadbook")
            .post(multiBody)
            .build();
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
                Looper.prepare();
                Toast.makeText(UploadBookList.this, "上传完成", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    });
}


    public void booklist(){
        Cursor cursor = db.query("books", null, null, null, null, null, null);
        int g = -1;
        while (cursor.moveToNext()) {
            Log.e("游标", cursor.getString(2));
            Log.e("游标", cursor.getString(1));
            UploadBook book=new UploadBook(cursor.getString(1),cursor.getString(2),R.drawable.booklogo);
            booklist.add(book);
        }
        cursor.close();
    }
}
