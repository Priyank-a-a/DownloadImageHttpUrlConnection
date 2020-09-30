package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
//add 2 permissions in manifest

ImageView iv1;

    ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv1 = findViewById(R.id.iv1);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);



    }

    public void checkNetwork(View view) {
     //for android 10 version--if android version is greater or equal to 10,the controls comes below
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q)
        {
          NetworkCapabilities capabilities =  connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
          if(capabilities!=null)
          {
              if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
              {
                  Toast.makeText(this, "mobile network connected", Toast.LENGTH_SHORT).show();
              }
              else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
              {
                  Toast.makeText(this, "wifi connected", Toast.LENGTH_SHORT).show();
              }
          }
          else
          {
              Toast.makeText(this, "no active network is present", Toast.LENGTH_SHORT).show();
          }


        }
        //else- android version is lesser than 10
        else
        {

          String imageUrl="https://wallpapersite.com/images/pages/pic_w/6408.jpg";
          NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();//method is depricted for android 10 onwards
        if(networkInfo!=null)
        {
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                Toast.makeText(this, "wifi connected", Toast.LENGTH_SHORT).show();


            }
            else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)

            {
                new MyImageTask().execute(imageUrl);
                Toast.makeText(this, "mobile network connected", Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            Toast.makeText(this, "no active network is present", Toast.LENGTH_SHORT).show();
        }


    }}

    //class for image download--nested class
    //bitmap- as we want an image as result/output..void- for progress..we dont want any progress to be displayed
    //string will get converted to url
    class MyImageTask extends AsyncTask<String, Void, Bitmap>//param,progress,result
    {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);


        }

        //not predefined
        Bitmap downloadImage(String s)
        {
            Bitmap bitmap = null;

            try {
                URL url = new URL(s);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                int code = httpURLConnection.getResponseCode();


                if(code == HttpURLConnection.HTTP_OK)
                {
                    //for getting input
                InputStream inputStream =httpURLConnection.getInputStream();
                    if(inputStream != null)
                    {
                        bitmap  = BitmapFactory.decodeStream(inputStream);
                    }
                }

            }

            catch (MalformedURLException e) {
                Log.d("filter","prblm");
                e.printStackTrace();
            }
            catch(IOException e)
            {
                Log.d("filter","prblm");
                e.printStackTrace();
            }

            return bitmap;
        }



        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap!=null)
            {
                iv1.setImageBitmap(bitmap);
            }
        }
    }

}