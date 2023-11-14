package com.example.dogapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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

        MyViewHolder vh = (MyViewHolder) holder;
        Mascotas mascota = list.get(position);
        vh.NombreMascota.setText(mascota.getNombrePet());
        vh.Edad.setText(mascota.getEdadPet());
        String imageUri=null;
        imageUri = mascota.getImage();
        if (imageUri == null){
            vh.ImgPet.setImageResource(R.drawable.animal_icon_144556);
        }else{
            Picasso.with(context).load(imageUri).into(vh.ImgPet);
        }


        vh.txt_option.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,vh.txt_option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item ->
            {
                if (item.getItemId() == R.id.menu_ver_perfil) {
                    Intent intent = new Intent(context, PetProfileActivity.class);
                    intent.putExtra("Nombre", list.get(vh.getAdapterPosition()).getNombrePet());
                    intent.putExtra("Edad", list.get(vh.getAdapterPosition()).getEdadPet());
                    intent.putExtra("Direccion", list.get(vh.getAdapterPosition()).getDireccionPet());
                    intent.putExtra("Genero", list.get(vh.getAdapterPosition()).getGeneroPet());
                    intent.putExtra("Imagen", list.get(vh.getAdapterPosition()).getImage());
                    context.startActivity(intent);
                }else if (item.getItemId() == R.id.ver_qr){
                    Intent intent = new Intent(context, VerCodigoQR.class);
                    intent.putExtra("Nombre", list.get(vh.getAdapterPosition()).getNombrePet());
                    intent.putExtra("Edad", list.get(vh.getAdapterPosition()).getEdadPet());
                    intent.putExtra("Direccion", list.get(vh.getAdapterPosition()).getDireccionPet());
                    intent.putExtra("Genero", list.get(vh.getAdapterPosition()).getGeneroPet());
                    intent.putExtra("Imagen", list.get(vh.getAdapterPosition()).getImage());
                    intent.putExtra("QR",list.get(vh.getAdapterPosition()).getQR());
                    context.startActivity(intent);

                } else if (item.getItemId() == R.id.menu_borrar) {

                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView NombreMascota, Edad,txt_option;
        ImageView ImgPet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            NombreMascota = itemView.findViewById(R.id.textviewMascotaLista);
            Edad = itemView.findViewById(R.id.textviewEdadLista);
            txt_option = itemView.findViewById(R.id.txt_option);
            ImgPet = itemView.findViewById(R.id.FotoMascota_card);

        }
    }
}
