package com.example.dogapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Mascotas> list;

    public MyAdapter(Context context, ArrayList<Mascotas> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.carta_mascota,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Mascotas mascota = list.get(position);
        holder.NombreMascota.setText(mascota.getNombrePet());
        holder.Edad.setText(mascota.getEdadPet());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView NombreMascota, Edad;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            NombreMascota = itemView.findViewById(R.id.textviewMascotaLista);
            Edad = itemView.findViewById(R.id.textviewEdadLista);

        }
    }
}
