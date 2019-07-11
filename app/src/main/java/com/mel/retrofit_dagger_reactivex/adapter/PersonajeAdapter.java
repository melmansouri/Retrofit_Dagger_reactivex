package com.mel.retrofit_dagger_reactivex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.mel.retrofit_dagger_reactivex.R;
import com.mel.retrofit_dagger_reactivex.model.Personaje;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonajeAdapter extends Adapter<PersonajeAdapter.BasicViewHolder> {
    private List<Personaje> personajes;
    private int totalElementInServer;
    private int NORMAL_VIEW=0;
    private int LOADING_VIEW=1;
    private boolean isEndScroll;


    public PersonajeAdapter(List<Personaje> personajes) {
        this.personajes = personajes;
    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_personaje, parent, false);
        BasicViewHolder basicViewHolder=new PersonajeViewHolder(view);
        if (viewType==LOADING_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
            basicViewHolder=new LoadingViewHolder(view);
        }
        return basicViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        if (holder instanceof PersonajeViewHolder){
            Personaje personaje=personajes.get(position);
            ((PersonajeViewHolder)holder).tvName.setText(personaje.getName());
            ((PersonajeViewHolder)holder).tvBirth.setText(personaje.getBirthYear());
            ((PersonajeViewHolder)holder).tvEyeColor.setText(personaje.getEyeColor());
            ((PersonajeViewHolder)holder).tvHeight.setText(personaje.getHeight());
            isEndScroll=false;
        }else {
            isEndScroll=true;
        }
    }

    /**
     * Aqui diferenciaremos cuando el elemento o vista de la lista
     * tiene que ser, el que contenga los datos que obtenemos del ws o
     * el del progressBar
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position==personajes.size()-1){
            return LOADING_VIEW;
        }
        return NORMAL_VIEW;
    }

    @Override
    public int getItemCount() {
        return personajes.size();
    }

    public void setPersonajes(List<Personaje> personajesList){
        personajes=personajesList;
        notifyDataSetChanged();
    }

    public boolean isEndScroll() {
        return isEndScroll;
    }

    static class PersonajeViewHolder extends BasicViewHolder {
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
    static class LoadingViewHolder extends BasicViewHolder{
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    static class BasicViewHolder extends RecyclerView.ViewHolder{

        public BasicViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
