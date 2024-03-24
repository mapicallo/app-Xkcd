package com.example.xkcd;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView imgComic;
    TextView txtvTitle;
    Button btnGetcomic;

    XkcdService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_main));

        imgComic = findViewById(R.id.imgComic);
        txtvTitle = findViewById(R.id.txtvTitle);
        btnGetcomic = findViewById(R.id.btnGetcomic);

        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create((XkcdService.class));

        btnGetcomic.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<Comic> call = service.getComic(new Random().nextInt(100));

                call.enqueue(new Callback<Comic>() {
                    @Override
                    public void onResponse(Call<Comic> call, Response<Comic> response) {
                        Comic comic = response.body();

                        try{
                        if(comic != null){
                            txtvTitle.setText(comic.getTitle());
                            Picasso.with(MainActivity.this)
                                    .load((comic.getImg()))
                                    .into(imgComic);

                        }
                    }catch (Exception e){
                            Log.e("MainActivity",e.toString());
                        }

                        }

                    @Override
                    public void onFailure(Call<Comic> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error en el servidor", Toast.LENGTH_LONG);
                    }
                });

            }
        }));



    }
}
