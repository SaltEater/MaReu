package com.openclassrooms.projet4_mareu.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.projet4_mareu.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReunionListFragment extends Fragment {

    private FilterCallbackInterface activity;
    @BindView(R.id.text_no_reunion)
    TextView noReunionText;
    @BindView(R.id.recycler_view_reunion)
    RecyclerView recyclerView;

    static ReunionListFragment newInstance(FilterCallbackInterface activity){
        ReunionListFragment reunionListFragment = new ReunionListFragment();
        reunionListFragment.activity = activity;
        return reunionListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reunion_list, container, false);
        ButterKnife.bind(this, view);
        Context context = getContext();
        configRecyclerView(context);
        updateText();
        return view;
    }

    void update() {
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        updateText();
    }

    void updateText() {
        if (recyclerView.getAdapter().getItemCount() == 0)
            noReunionText.setVisibility(View.VISIBLE);
        else
            noReunionText.setVisibility(View.INVISIBLE);
    }

    void configRecyclerView(Context context) {
        if (activity != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(context), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(new ReunionRecyclerViewAdapter(activity.getFilteredReunions(), context));
        }
    }
}
