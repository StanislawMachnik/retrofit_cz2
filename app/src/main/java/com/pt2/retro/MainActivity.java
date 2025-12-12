package com.pt2.retro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    List<Pytanie>  pytania;
    RadioGroup radioGroup;

    TextView trescPytania;
    RadioButton radioButton_a;
    RadioButton radioButton_b;
    RadioButton radioButton_c;

    int radioButtonid[] = new int[]{
            R.id.radioButton1,
            R.id.radioButton2,
            R.id.radioButton3
    };
    Button buttonNext;
    int aktualnePytanie = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trescPytania = findViewById(R.id.trescPytania);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_a = findViewById(R.id.radioButton1);
        radioButton_b = findViewById(R.id.radioButton2);
        radioButton_c = findViewById(R.id.radioButton3);
        buttonNext = findViewById(R.id.button1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/StanislawMachnik/odczytywanie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Pytanie>> call = jsonPlaceHolderApi.getPytania();

        call.enqueue(
                new Callback<List<Pytanie>>() {
                    @Override
                    public void onResponse(Call<List<Pytanie>> call, Response<List<Pytanie>> response) {
                        // poprawne odczytanie z neta
                        if (!response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pytania = response.body();
                        Toast.makeText(MainActivity.this, pytania.get(0).getTrescPytania(), Toast.LENGTH_SHORT).show();
                        wyswietlPytanie(0);
                    }

                    @Override
                    public void onFailure(Call<List<Pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show(); //brak netu
                    }
                }
        );

        buttonNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(aktualnePytanie < pytania.size()-1){
                            if(sprawdzOdpowiedz(aktualnePytanie)){
                                Toast.makeText(MainActivity.this, "dobrze", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "źle", Toast.LENGTH_SHORT).show();
                            }
                            aktualnePytanie++;
                            wyswietlPytanie(aktualnePytanie);
                        }
                        else{
                         // TODO: sprawdzanie
                        }
                    }
                }
        );
    }
    public void wyswietlPytanie(int ktore){
        Pytanie pytanie = pytania.get(ktore);
        trescPytania.setText(pytanie.getTrescPytania());
        radioButton_a.setText(pytanie.getOdpa());
        radioButton_b.setText(pytanie.getOdpb());
        radioButton_c.setText(pytanie.getOdpc());
        radioButton_a.setChecked(false);
        radioButton_a.setChecked(false);
        radioButton_a.setChecked(false);
    }
    public boolean sprawdzOdpowiedz(int aktualnePytanie){
        Pytanie pytanie = pytania.get(aktualnePytanie);
        if(radioGroup.getCheckedRadioButtonId() == radioButtonid[pytanie.getPoprawna()]){
            return true;
        }
        else{
            return false;
        }
    }
}