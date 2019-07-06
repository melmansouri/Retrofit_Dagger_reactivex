package com.mel.retrofit_dagger_reactivex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.mel.retrofit_dagger_reactivex.R;
import com.mel.retrofit_dagger_reactivex.model.Personaje;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonajeAdapter extends Adapter<PersonajeAdapter.PersonajeViewHolder> {
    private List<Personaje> personajes;

    public PersonajeAdapter(List<Personaje> personajes) {
        this.personajes = personajes;
    }

    @NonNull
    @Override
    public PersonajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_personaje, parent, false);
        return new PersonajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonajeViewHolder holder, int position) {
        Personaje personaje=personajes.get(position);
        holder.tvName.setText(personaje.getName());
        holder.tvBirth.setText(personaje.getBirthYear());
        holder.tvEyeColor.setText(personaje.getEyeColor());
        holder.tvHeight.setText(personaje.getHeight());
    }

    @Override
    public int getItemCount() {
        return personajes.size();
    }

    public void setPersonajes(List<Personaje> personajesList){
        personajes=personajesList;
        notifyDataSetChanged();
    }

    static class PersonajeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvHeight)
        TextView tvHeight;
        @BindView(R.id.tvEyeColor)
        TextView tvEyeColor;
        @BindView(R.id.tvBirth)
        TextView tvBirth;
        public PersonajeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
