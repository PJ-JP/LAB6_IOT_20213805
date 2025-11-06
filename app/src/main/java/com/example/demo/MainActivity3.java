package com.example.demo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain3Binding binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int totalTareas=getIntent().getIntExtra("totalTareas",0);
        int tareasCompletadas=getIntent().getIntExtra("tareasCompletadas",0);
        int tareasPendientes=totalTareas-tareasCompletadas;
        binding.textView5.setText(String.valueOf(totalTareas));
        binding.textView6.setText(String.valueOf(tareasCompletadas));
        binding.textView7.setText(String.valueOf(tareasPendientes));
        
    }
}