package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.databinding.ActivityMain2Binding;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private final List<Tarea> data = new ArrayList<>();
    private TareaAdapter adapter;
    private static FirebaseFirestore db;
    static class Tarea {
        String titulo, fechaLimite, descripcion,docId;
        boolean estado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain2Binding binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main2);


        adapter = new TareaAdapter(data);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        cargarLista();
    }

    private void cargarLista() {
        data.clear();
        db.collection("tareas")
                .orderBy("fechaCreacion", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    for (QueryDocumentSnapshot d : snap) {
                        Tarea li = new Tarea();
                        li.titulo=d.getString("titulo");
                        li.fechaLimite=d.getString("fechaLimite");
                        li.descripcion=d.getString("descripcion");
                        li.estado= Boolean.TRUE.equals(d.getBoolean("estado"));
                        li.docId=d.getId();
                        data.add(li);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); // Usa el archivo de menú que creamos
        return true;
    }

    public void crearTarea(View view){
        startActivity(new Intent(this, CrearTareaActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_tasks) {
            Toast.makeText(this, "Opción Tareas seleccionada", Toast.LENGTH_SHORT).show();
            // Aquí puedes iniciar una nueva actividad o un fragmento para las tareas
            return true;
        } else if (itemId == R.id.menu_summary) {
            Toast.makeText(this, "Opción Resumen seleccionada", Toast.LENGTH_SHORT).show();
            // Aquí puedes iniciar una nueva actividad o un fragmento para el resumen
            return true;
        } else if (itemId == R.id.menu_logout) {
            //Lógica para cerrar sesión
            AuthUI.getInstance()
                    .signOut(this) // 'this' Activity
                    .addOnCompleteListener(task -> {
                        // Una vez cerrada la sesión, redirigir al MainActivity
                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                        // Limpia el stack de actividades para que el usuario no pueda volver atrás
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // finalizar MainActivity2
                    });
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private static class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.VH> {
        private final List<Tarea> items;
        TareaAdapter(List<Tarea> items){ this.items = items; }
        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
            View view = LayoutInflater.from(p.getContext()).inflate(R.layout.tarea_rv, p, false);
            return new VH(view);
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            Tarea l = items.get(pos);
            String estadoStr = l.estado ? "Completada" : "Pendiente";
            h.t1.setText(l.titulo == null ? "" : l.titulo);
            h.t2.setText(l.fechaLimite == null ? "" : l.fechaLimite);
            h.t3.setText(estadoStr);
            h.btnEditar.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), DetallesTareaActivity.class);
                i.putExtra("titulo",l.titulo);
                i.putExtra("fechaLimite", l.fechaLimite);
                i.putExtra("estado", l.estado);
                i.putExtra("descripcion", l.descripcion);
                i.putExtra("docId", l.docId);
                v.getContext().startActivity(i);
            });
            h.btnEliminar.setOnClickListener(v -> {
                borrar(l.docId);
                Intent i = new Intent(v.getContext(), MainActivity2.class);
                v.getContext().startActivity(i);
            });
        }
        @Override public int getItemCount(){ return items.size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView t1, t2,t3;
            Button btnEditar, btnEliminar;
            VH(@NonNull View v){
                super(v);
                t1=v.findViewById(R.id.textView1);
                t2=v.findViewById(R.id.textView2);
                t3=v.findViewById(R.id.textView3);
                btnEditar=v.findViewById(R.id.editar);
                btnEliminar=v.findViewById(R.id.borrar);
            }
        }
    }

    private static void borrar(String docId) {
        db.collection("tareas").document(docId).delete();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }
}