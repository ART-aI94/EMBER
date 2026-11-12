
package com.example.ember;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final List<Game> allGames;
    private final List<Game> visibleGames;
    private final int itemLayout;

    public GameAdapter(List<Game> games) {
        this(games, R.layout.item_game);
    }

    public GameAdapter(List<Game> games, int itemLayout) {
        allGames = new ArrayList<>(games);
        visibleGames = new ArrayList<>(games);
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(itemLayout, parent, false);

        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull GameViewHolder holder, int position) {

        Game game = visibleGames.get(position);

        holder.title.setText(game.getTitle());
        holder.cover.setImageResource(game.getImageResource());
        holder.cover.setContentDescription(game.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();

            Intent intent = new Intent(
                    context,
                    GameDetailsActivity.class
            );

            intent.putExtra(
                    GameDetailsActivity.EXTRA_TITLE,
                    game.getTitle()
            );

            intent.putExtra(
                    GameDetailsActivity.EXTRA_IMAGE,
                    game.getImageResource()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return visibleGames.size();
    }

    public void filter(String query) {
        visibleGames.clear();

        String text = query.trim().toLowerCase(Locale.ROOT);

        for (Game game : allGames) {
            if (game.getTitle()
                    .toLowerCase(Locale.ROOT)
                    .contains(text)) {

                visibleGames.add(game);
            }
        }

        notifyDataSetChanged();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title;

        GameViewHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.gameCover);
            title = itemView.findViewById(R.id.gameTitle);
        }
    }
}
