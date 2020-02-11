package com.openclassrooms.projet4_mareu.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.projet4_mareu.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomRecyclerViewAdapter extends RecyclerView.Adapter<RoomRecyclerViewAdapter.RoomViewHolder> {
    private ArrayList<String> roomList;
    private Context context;
    private SharedPreferences settings;

    RoomRecyclerViewAdapter(ArrayList<String> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }


    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_room_list, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomViewHolder holder, final int position) {
        settings = context.getSharedPreferences(context.getString(R.string.filterSettings), Context.MODE_PRIVATE);

        if (settings.getBoolean(roomList.get(position), true)) {
            holder.checkImage.setVisibility(View.VISIBLE);
        } else {
            holder.checkImage.setVisibility(View.INVISIBLE);
        }

        holder.textView.setText(roomList.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = settings.edit();

                if(holder.checkImage.getVisibility()==View.INVISIBLE){
                    holder.checkImage.setVisibility(View.VISIBLE);
                    editor.putBoolean(roomList.get(position), true);

                } else {
                    holder.checkImage.setVisibility(View.INVISIBLE);
                    editor.putBoolean(roomList.get(position), false);

                }
                editor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.holder_salle_list_text)
        TextView textView;
        @BindView(R.id.holder_salle_list_layout)
        ConstraintLayout layout;
        @BindView(R.id.holder_salle_list_check_image)
        ImageView checkImage;

        RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
