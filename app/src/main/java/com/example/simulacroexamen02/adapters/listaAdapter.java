package com.example.simulacroexamen02.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simulacroexamen02.R;
import com.example.simulacroexamen02.modelos.Producto;

import java.util.ArrayList;

public class listaAdapter extends RecyclerView.Adapter<listaAdapter.ListaVH>{

    private Context context;
    private ArrayList<Producto> objects;
    private int cardLayout;
    private TextView txtCantidadTotal;
    private TextView txtPrecioTotal;

    public listaAdapter(Context context, ArrayList<Producto> objects, int cardLayout, TextView txtCantidadTotal, TextView txtPrecioTotal){
        this.context = context;
        this.objects = objects;
        this.cardLayout = cardLayout;
        this.txtCantidadTotal = txtCantidadTotal;
        this.txtPrecioTotal = txtPrecioTotal;
    }

    @NonNull
    @Override
    public ListaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(context).inflate(cardLayout, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        todoView.setLayoutParams(layoutParams);
        return new ListaVH(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaVH holder, int position) {
        Producto producto = objects.get(position);
        holder.lblNombre.setText(producto.getNombre());
        holder.lblCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.lblPrecio.setText(String.valueOf(producto.getPrecio()));

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarProducto(producto, holder.getAdapterPosition()).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editToDo(producto, holder.getAdapterPosition()).show();
            }
        });
    }
    // Retornar la cantidad de elementos que hay que instanciar
    @Override
    public int getItemCount() {
        return objects.size();
    }

    private androidx.appcompat.app.AlertDialog editToDo(Producto producto, int position) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Editar Producto");
        builder.setCancelable(false);

        View alertView = LayoutInflater.from(context). inflate(R.layout.lista_model_alert, null);
        TextView txtNombre = alertView.findViewById(R.id.txtNombreAlert);
        TextView txtCantidad = alertView.findViewById(R.id.txtCantidadAlert);
        TextView txtPrecio = alertView.findViewById(R.id.txtPrecioAlert);
        builder.setView(alertView);

        txtNombre.setText(producto.getNombre());
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtPrecio.setText(String.valueOf(producto.getPrecio()));

        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()){
                    producto.setNombre(txtNombre.getText().toString());
                    producto.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                    producto.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                    notifyItemChanged(position);
                    actualizarContadores();
                }
                else {
                    Toast.makeText(context, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }

    private android.app.AlertDialog eliminarProducto(Producto producto, int position){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setCancelable(false);
        TextView mensaje = new TextView(context);
        mensaje.setText("¿ESTÁS SEGURO QUE QUIERES ELIMINARLO?");
        mensaje.setTextSize(20);
        mensaje.setTextColor(Color.RED);
        mensaje.setPadding(50,100,50,100);
        builder.setView(mensaje);

        builder.setNegativeButton("NO", null);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                objects.remove(producto);
                notifyItemRemoved(position);
                actualizarContadores();
            }
        });
        return builder.create();
    }

    private void actualizarContadores(){
        txtCantidadTotal.setText(String.valueOf(objects.size()));

        float precioTotal = 0;

        for (Producto p : objects){
            precioTotal += p.getPrecio();
        }

        txtPrecioTotal.setText(String.valueOf(precioTotal));
    }

    public class ListaVH extends RecyclerView.ViewHolder{

        TextView lblNombre, lblCantidad, lblPrecio;
        ImageButton btnEliminar;
        public ListaVH(@NonNull View itemView) {
            super(itemView);
            lblNombre = itemView.findViewById(R.id.lblNombreViewModel);
            lblCantidad = itemView.findViewById(R.id.lblCantidadViewModel);
            lblPrecio = itemView.findViewById(R.id.lblPrecioViewModel);
            btnEliminar = itemView.findViewById(R.id.btnEliminarViewModel);
        }
    }
}
