package com.example.android.content.adapter;

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
import com.example.android.content.R;
import com.example.android.content.item.TVShow;
import com.example.android.content.loading.LoadingTVShowDetail;

import java.util.ArrayList;

public class ListTVShowAdapter extends RecyclerView.Adapter<ListTVShowAdapter.ListViewHolder> {
    private ArrayList<TVShow> listTVShow;
    private Context context;

    public ListTVShowAdapter(ArrayList<TVShow> tvShows) {
        this.listTVShow = tvShows;
    }

    public ListTVShowAdapter(Context context) {
        this.context = context;
    }

    public void setListFavorite(ArrayList<TVShow> items) {
        this.listTVShow = new ArrayList<>();
        this.listTVShow.addAll(items);
        notifyDataSetChanged();
    }

    public ArrayList<TVShow> getListTVShow() {
        return listTVShow;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_tv_show, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final TVShow tvShow = listTVShow.get(position);

        Glide.with(holder.itemView.getContext()).load(holder.itemView.getContext().getResources().getString(R.string.image_url) + tvShow.getPhoto1()).into(holder.imgPhoto);

        if (tvShow.getName() != null && !TextUtils.isEmpty(tvShow.getName()))
            holder.tvName.setText(tvShow.getName());
        if (tvShow.getDate() != null && !TextUtils.isEmpty(tvShow.getDate()))
            holder.tvYear.setText(tvShow.getDate().substring(0, 4));
        if (!TextUtils.isEmpty(String.valueOf(tvShow.getRating())))
            holder.tvRating.setText(String.valueOf(tvShow.getRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), listTVShow.get(position).getName().toUpperCase(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(holder.itemView.getContext(), LoadingTVShowDetail.class);
                TVShow tvShow1 = new TVShow();
                tvShow1.setName(tvShow.getName());
                tvShow1.setId(tvShow.getId());
                tvShow1.setDescription(tvShow.getDescription());
                tvShow1.setDate(tvShow.getDate());
                tvShow1.setRating(tvShow.getRating());
                tvShow1.setPhoto1(tvShow.getPhoto1());
                tvShow1.setPhoto2(tvShow.getPhoto2());
                intent.putExtra(LoadingTVShowDetail.EXTRA_TV_SHOW, tvShow1);

                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTVShow.size();
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
