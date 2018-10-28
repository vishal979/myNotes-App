package com.example.hp.mynotes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoritesNotesFragment extends Fragment{
    private static final String TAG = "FavoritesNotesFragment";
    DatabaseClassNotes dbhelper;
    static ArrayList<String> favtitlesList=new ArrayList<>();
    static ArrayList<String> favdatesList=new ArrayList<>();
    static ArrayList<String> favcontentsList=new ArrayList<>();
    static ArrayList<Integer> favisImportant=new ArrayList<>();
    private static final String TABLE_NAME="notes_table";
    RecyclerView recyclerView;
    static TextView favNotesTV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_favoritenotes,container,false);
        dbhelper=new DatabaseClassNotes(getActivity());
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favNotesTV=(TextView) view.findViewById(R.id.nofavoritenotesTV);
        recyclerView=(RecyclerView) view.findViewById(R.id.favoriteNotesRecyclerView);
        registerForContextMenu(recyclerView);
        favNotesTV.setVisibility(View.VISIBLE);
        initList();
        initAdapter();
    }

    public void initList(){
        Cursor cc=dbhelper.getData("SELECT * FROM "+TABLE_NAME+" WHERE isImportant=2");
        favNotesTV.setVisibility(View.VISIBLE);
        if(cc.getCount()>0){
            favNotesTV.setVisibility(View.INVISIBLE);}
        new Thread(new Runnable() {
            @Override
            public void run() {
                favtitlesList.clear();
                favcontentsList.clear();
                favdatesList.clear();
                favisImportant.clear();
                Cursor c=dbhelper.getData("SELECT * FROM "+TABLE_NAME+" WHERE isImportant=2");
                if(c.getCount()>0){
                    while(c.moveToNext()){
                        favtitlesList.add(c.getString(1));
                        favcontentsList.add(c.getString(2));
                        favdatesList.add(c.getString(3));
                        favisImportant.add(c.getInt(4));
                    }
                }
            }
        }).start();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.notes_clicked_menu,menu);
        menu.setHeaderTitle("Options");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_delete:

        }
        return true;
    }

    public void initAdapter(){
        RecyclerView recyclerView=(RecyclerView) getView().findViewById(R.id.favoriteNotesRecyclerView);
        CustomAdapterFavorites recyclerViewAdapter=new CustomAdapterFavorites(getContext(),favtitlesList,favcontentsList,favdatesList,favisImportant,FavoritesNotesFragment.this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
        initAdapter();
    }
}
