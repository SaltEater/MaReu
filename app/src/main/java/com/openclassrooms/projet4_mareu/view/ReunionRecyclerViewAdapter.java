package com.openclassrooms.projet4_mareu.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.projet4_mareu.R;
import com.openclassrooms.projet4_mareu.event.DeleteReunionEvent;
import com.openclassrooms.projet4_mareu.model.Reunion;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReunionRecyclerViewAdapter extends RecyclerView.Adapter <ReunionRecyclerViewAdapter.ReunionViewHolder>{
    private ArrayList<Reunion> filteredReunionList;
    private Context context;

    ReunionRecyclerViewAdapter(ArrayList<Reunion> filteredReunionList, Context context) {
        this.filteredReunionList = filteredReunionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReunionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_reunion_item, parent, false);
        return new ReunionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReunionViewHolder holder, int position) {
        final Reunion reunion = filteredReunionList.get(position);
        holder.reunionTitle.setText(reunion.getName() + " - " + reunion.getDate() + " - " + reunion.getRoom());
        holder.participantsList.setText(reunion.getParticipants());

        int color = 0;
        switch (reunion.getRoom()){
            case "Mario":
                color = R.color.Mario;
                break;
            case "Luigi":
                color = R.color.Luigi;
                break;
            case "Peach":
                color = R.color.Peach;
                break;
            case "Toad":
                color = R.color.Toad;
                break;
        }
        Drawable background = holder.itemListAvatar.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(context.getResources().getColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(context.getResources().getColor(color));
        }

        holder.deleteImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new DeleteReunionEvent(reunion));
                Toast.makeText(context,"La réunion a été supprimée",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredReunionList.size();
    }

    class ReunionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reunion_title)
        TextView reunionTitle;
        @BindView(R.id.participants_list)
        TextView participantsList;
        @BindView(R.id.delete_image)
        ImageView deleteImage;
        @BindView(R.id.item_list_avatar)
        ImageView itemListAvatar;


        ReunionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
