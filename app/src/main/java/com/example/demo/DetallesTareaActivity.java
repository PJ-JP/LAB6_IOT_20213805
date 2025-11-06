package com.example.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo.databinding.ActivityDetallesTareaBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetallesTareaActivity extends AppCompatActivity {

    private ActivityDetallesTareaBinding binding;
    private String docId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetallesTareaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();

        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String fechaLimite = getIntent().getStringExtra("fechaLimite");
        docId = getIntent().getStringExtra("docId");

        Button btnEditar = findViewById(R.id.btnEditarTarea);
        EditText etTitulo = findViewById(R.id.editTextTitulo);
        EditText etDescripcion = findViewById(R.id.editTextDescripcion);
        EditText etFechaLimite = findViewById(R.id.editTextFechaLimite);
        Spinner spEstado = findViewById(R.id.spinner);


        etTitulo.setText(titulo);
        etDescripcion.setText(descripcion);
        etFechaLimite.setText(fechaLimite);

        btnEditar.setOnClickListener(this::guardar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void guardar(View view){
        String titulo = binding.editTextTitulo.getText().toString().trim();
        String descripcion = binding.editTextDescripcion.getText().toString().trim();
        String fechaLimite = binding.editTextFechaLimite.getText().toString().trim();

        if (titulo.isEmpty() || fechaLimite.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> up = new HashMap<>();
        up.put("titulo", titulo);
        up.put("descripcion", descripcion);
        up.put("fechaLimite", fechaLimite);

        db.collection("tareas").document(docId).update(up)
                    .addOnSuccessListener(v -> { Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show(); finish(); })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
        finish();
    }
}