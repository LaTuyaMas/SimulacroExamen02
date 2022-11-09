package com.example.simulacroexamen02;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.simulacroexamen02.adapters.listaAdapter;
import com.example.simulacroexamen02.constantes.Constantes;
import com.example.simulacroexamen02.modelos.Producto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.example.simulacroexamen02.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Producto> listaCompra;
    private listaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ActivityResultLauncher<Intent> launcherCreateProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // INICIALIZANDO ELEMENTOS
        listaCompra = new ArrayList<>();
        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;

        adapter = new listaAdapter(MainActivity.this, listaCompra, R.layout.lista_model_view,
                binding.contentMain.txtCantidadMain, binding.contentMain.txtPrecioMain);
        layoutManager = new GridLayoutManager(MainActivity.this, columnas);
        binding.contentMain.contenedor.setAdapter(adapter);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);

        launcherCreateProducto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                if (result.getData().getExtras() != null) {
                                    if (result.getData().getExtras().getSerializable(Constantes.PRODUCTO) != null) {
                                        Producto producto = (Producto) result.getData().getExtras().getSerializable(Constantes.PRODUCTO);
                                        listaCompra.add(0, producto);
                                        adapter.notifyItemInserted(0);
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "El bundle no lleva el tag "+Constantes.PRODUCTO, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "NO HAY BUNDLE EN EL INTENT", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "NO HAY INTENT EN EL RESULT", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Ventana Cancelada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        actualizarContadores();

        // YA NO SE INICIAN MÁS ELEMENTOS
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcherCreateProducto.launch(new Intent(MainActivity.this, CreateActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarContadores();
    }

    private void actualizarContadores(){
        binding.contentMain.txtCantidadMain.setText(String.valueOf(listaCompra.size()));

        float precioTotal = 0;

        for (Producto p : listaCompra){
            precioTotal += p.getPrecio();
        }

        binding.contentMain.txtPrecioMain.setText(String.valueOf(precioTotal));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constantes.PRODUCTO, listaCompra);
    }

    //Restablece la lista
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> temporal = (ArrayList<Producto>) savedInstanceState.getSerializable(Constantes.PRODUCTO);
        listaCompra.addAll(temporal);
        adapter.notifyItemRangeInserted(0, listaCompra.size());
    }
}