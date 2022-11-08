package com.example.simulacroexamen02.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simulacroexamen02.R;
import com.example.simulacroexamen02.modelos.Producto;

import java.util.ArrayList;

public class listaAdapter extends RecyclerView.Adapter<listaAdapter.ListaVH>{

    private Context context;
    private ArrayList<Producto> objects;
    private int cardLayout;

    public listaAdapter(Context context, ArrayList<Producto> objects, int cardLayout){
        this.context = context;
        this.objects = objects;
        this.cardLayout = cardLayout;
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
        holder.lblCantidad.setText(Integer.toString(producto.getCantidad()));
        holder.lblPrecio.setText(Float.toString(producto.getPrecio()));

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarProducto(producto, holder.getAdapterPosition()).show();
            }
        });

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AlertDialog con todos los campos a editar
                // Necesita el todo
                // Necesito la posición
                editToDo(todo, holder.getAdapterPosition()).show();
            }
        });

         */
    }
    // Retornar la cantidad de elementos que hay que instanciar
    @Override
    public int getItemCount() {
        return objects.size();
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
            }
        });
        return builder.create();
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
