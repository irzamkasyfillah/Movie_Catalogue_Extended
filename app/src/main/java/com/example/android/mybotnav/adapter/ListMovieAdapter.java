package com.example.android.mybotnav.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.mybotnav.Item.Movie;
import com.example.android.mybotnav.Loading.LoadingMovieDetail;
import com.example.android.mybotnav.R;

import java.util.ArrayList;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ListViewHolder> {
    private ArrayList<Movie> listMovie;
    private Context context;

    public ListMovieAdapter(ArrayList<Movie> listMovie) {
        this.listMovie = listMovie;
    }

    public ListMovieAdapter(Context context) {
        this.context = context;
    }

    public void refill(ArrayList<Movie> items) {
        this.listMovie = new ArrayList<>();
        this.listMovie.addAll(items);

        notifyDataSetChanged();
    }

    public void setListFavorite(ArrayList<Movie> listMovie) {
        this.listMovie = new ArrayList<>();
        this.listMovie.addAll(listMovie);
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_movie, viewGroup, false);
        return new ListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Movie movie = listMovie.get(position);

        Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500" + movie.getPhoto1()).into(holder.imgPhoto);

        if (movie.getName() != null && !TextUtils.isEmpty(movie.getName()))
            holder.tvName.setText(movie.getName());
        if (!TextUtils.isEmpty(String.valueOf(movie.getRating())))
            holder.tvRating.setText(String.valueOf(movie.getRating()));
        if (movie.getDate() != null && !TextUtils.isEmpty(movie.getDate()))
            holder.tvYear.setText(movie.getDate().substring(0, 4));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), movie.getName().toUpperCase(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), LoadingMovieDetail.class);

                Movie movie1 = new Movie();
                movie1.setId(movie.getId());
                movie1.setName(movie.getName());
                movie1.setPhoto1(movie.getPhoto1());
                movie1.setPhoto2(movie.getPhoto2());
                movie1.setYear(movie.getDate().substring(0, 4));
                movie1.setRating(movie.getRating());
                movie1.setGenre(movie.getGenre());
                movie1.setDate(movie.getDate());
                movie1.setDescription(movie.getDescription());

                intent.putExtra(LoadingMovieDetail.EXTRA_MOVIE, movie1);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvRating, tvYear;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.img_photo);
            tvName = itemView.findViewById(R.id.name);
            tvRating = itemView.findViewById(R.id.rating);
            tvYear = itemView.findViewById(R.id.year);
        }
    }
}
