package com.example.ember;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter
        extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    public interface OnResultClickListener {
        void onResultClick(SearchResult result);
    }

    private final List<SearchResult> results = new ArrayList<>();
    private final OnResultClickListener listener;

    public SearchResultAdapter(OnResultClickListener listener) {
        this.listener = listener;
    }

    public void setResults(List<SearchResult> newResults) {
        results.clear();
        results.addAll(newResults);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        SearchResult result = results.get(position);

        holder.title.setText(result.getTitle());
        holder.subtitle.setText(result.getSubtitle());
        holder.category.setText(result.getCategory());

        if (result.getImageResource() != 0) {
            holder.image.setImageResource(result.getImageResource());
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onResultClick(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView subtitle;
        TextView category;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.searchResultImage);
            title = itemView.findViewById(R.id.searchResultTitle);
            subtitle = itemView.findViewById(R.id.searchResultSubtitle);
            category = itemView.findViewById(R.id.searchResultCategory);
        }
    }
}