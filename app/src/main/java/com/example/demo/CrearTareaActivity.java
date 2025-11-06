package com.example.demo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CrearTareaActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion, etFechaLimite;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        db = FirebaseFirestore.getInstance();

        etTitulo = findViewById(R.id.editTextTitulo);
        etDescripcion = findViewById(R.id.editTextDescripcion);
        etFechaLimite = findViewById(R.id.editTextFechaLimite);

        Button btnGuardar = findViewById(R.id.btnGuardarTarea);

        btnGuardar.setOnClickListener(v -> guardarTarea());
    }

    private void guardarTarea() {
        String titulo = etTitulo.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String fechaLimite = etFechaLimite.getText().toString();

        if (titulo.isEmpty() || fechaLimite.isEmpty()) {
            Toast.makeText(this, "El título y la fecha son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> nuevaTarea = new HashMap<>();
        nuevaTarea.put("titulo", titulo);
        nuevaTarea.put("descripcion", descripcion);
        nuevaTarea.put("fechaLimite", fechaLimite);
        nuevaTarea.put("estado", false); // pendiente
        nuevaTarea.put("fechaCreacion", com.google.firebase.firestore.FieldValue.serverTimestamp()); // Para ordenar

        db.collection("tareas")
                .add(nuevaTarea)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Tarea creada exitosamente", Toast.LENGTH_SHORT).show();
                    finish(); // Onresume
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al crear la tarea", Toast.LENGTH_SHORT).show();
                });
    }
}