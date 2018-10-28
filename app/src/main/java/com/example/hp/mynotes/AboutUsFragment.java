package com.example.hp.mynotes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsFragment extends Fragment {
    TextView dreamTeam;
    ImageView backButtonIV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButtonIV=view.findViewById(R.id.backButtonIV);
        backButtonIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        dreamTeam=view.findViewById(R.id.dreamTeamTV);
        dreamTeam.setText("@"+"dream_team");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        HomeActivity.fab.setVisibility(View.VISIBLE);
        HomeActivity.relativeLayout.setVisibility(View.VISIBLE);
    }
}
