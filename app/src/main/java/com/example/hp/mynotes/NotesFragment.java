package com.example.hp.mynotes;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesFragment extends Fragment{

    private static final String TAG = "NotesFragment";
    DatabaseClassNotes dbhelper;
    static ArrayList<String> titlesList=new ArrayList<>();
    static ArrayList<String> datesList=new ArrayList<>();
    static ArrayList<String> contentsList=new ArrayList<>();
    static ArrayList<Integer> isImportant=new ArrayList<>();
    private static final String TABLE_NAME="notes_table";
    static TextView noNotesTV;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragments_notes,container,false);
        dbhelper=new DatabaseClassNotes(getActivity());
        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int pos=item.getOrder();
        switch (item.getItemId()){
            case 1:
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete?");
                builder.setTitle("Delete Note");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbhelper.delete(titlesList.get(pos));
                        initList();
                        initAdapter();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(R.drawable.ic_priorityhigh_alert).show();
                break;
            case 2:
                ((HomeActivity)getActivity()).shareviamail(titlesList.get(pos),contentsList.get(pos));
                break;
        }
        return true;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noNotesTV=(TextView) view.findViewById(R.id.noNotesTV);
        recyclerView=(RecyclerView) view.findViewById(R.id.notesRecyclerView);
        registerForContextMenu(recyclerView);
        noNotesTV.setVisibility(View.VISIBLE);
        initList();
        initAdapter();
    }

    public void initList(){
        Cursor cc=dbhelper.getData("SELECT * FROM "+TABLE_NAME);
        noNotesTV.setVisibility(View.VISIBLE);
        if(cc.getCount()>0){
            noNotesTV.setVisibility(View.INVISIBLE);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                titlesList.clear();
                contentsList.clear();
                datesList.clear();
                isImportant.clear();
                Cursor c=dbhelper.getData("SELECT * FROM "+TABLE_NAME);
                if(c.getCount()>0){
                    while(c.moveToNext()){
                        titlesList.add(c.getString(1));
                        contentsList.add(c.getString(2));
                        datesList.add(c.getString(3));
                        isImportant.add(c.getInt(4));
                    }
                }
            }
        }).start();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void initAdapter(){
        RecyclerView recyclerView=(RecyclerView) getView().findViewById(R.id.notesRecyclerView);
        CustomAdapter recyclerViewAdapter=new CustomAdapter(getContext(),titlesList,contentsList,datesList,isImportant,NotesFragment.this);
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
