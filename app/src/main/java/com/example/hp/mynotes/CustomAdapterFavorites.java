package com.example.hp.mynotes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

import java.util.ArrayList;

public class CustomAdapterFavorites extends RecyclerView.Adapter<CustomAdapterFavorites.ViewHolder> implements View.OnClickListener,FullScreenDialogFragment.OnConfirmListener,
        FullScreenDialogFragment.OnDiscardListener{

    private ArrayList<String> titlesList=new ArrayList<>();
    private ArrayList<String> datesList=new ArrayList<>();
    private ArrayList<String> contentsList=new ArrayList<>();
    private ArrayList<Integer> isImportant=new ArrayList<>();
    Context mContext;
    DatabaseClassNotes dbhelper;
    FavoritesNotesFragment fragment;
    public CustomAdapterFavorites(Context mContext,ArrayList<String> titlesList,ArrayList<String> contentsList,ArrayList<String> datesList,ArrayList<Integer> isImportant,FavoritesNotesFragment fragment){
        this.mContext = mContext;
        this.titlesList=titlesList;
        this.contentsList=contentsList;
        this.datesList=datesList;
        this.isImportant=isImportant;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_card_notes,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        dbhelper=new DatabaseClassNotes(mContext);
        holder.titlesTextView.setText(titlesList.get(position));
        holder.datesTextView.setText(datesList.get(position));
        String content;
        if(contentsList.get(position).length()>35){
            content=contentsList.get(position).substring(0,35);
            content=content+"...";
        }else{
            content=contentsList.get(position);
        }
        holder.contentsTextView.setText(content);
        holder.parentLayout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0,1,position,"Delete");
                menu.add(0,2,position,"share");
                menu.setHeaderTitle("Options");
            }
        });
        if(isImportant.get(position)==2){
            holder.starImageView.setImageResource(R.drawable.ic_star_checked);
        }else{
            holder.starImageView.setImageResource(R.drawable.ic_star_unchecked);
        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args=new Bundle();
                args.putString("title",titlesList.get(position));
                args.putString("content",contentsList.get(position));
                args.putString("date",datesList.get(position));
                new FullScreenDialogFragment.Builder(mContext).setTitle(""+titlesList.get(position))
                        .setConfirmButton("Save")
                        .setOnConfirmListener(CustomAdapterFavorites.this)
                        .setOnDiscardListener(CustomAdapterFavorites.this)
                        .setContent(ViewNoteDialogFragment.class,args)
                        .build().show(((FragmentActivity)mContext).getSupportFragmentManager(),"dialog");
            }
        });
        holder.starImageView.setOnClickListener(new View.OnClickListener() {
            //            int pos=position+1;
            @Override
            public void onClick(View v) {
                if(isImportant.get(position)==1){
                    dbhelper.update(Integer.toString(12+1),titlesList.get(position),contentsList.get(position),datesList.get(position),Integer.toString(2));
                    fragment.initList();
                    fragment.initAdapter();
                }else{
                    if(isImportant.get(position)==2){
                        dbhelper.update(Integer.toString(12+1),titlesList.get(position),contentsList.get(position),datesList.get(position),Integer.toString(1));
                        fragment.initList();
                        fragment.initAdapter();
                    }
                }
                fragment.initList();
                fragment.initAdapter();
            }
        });
    }

    @Override
    public int getItemCount() {
        return titlesList.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConfirm(@Nullable Bundle result) {
        boolean update=dbhelper.updateNote(result.getString("oldTitle"),result.getString("title"),result.getString("content"),result.getString("date"),Integer.toString(2));
        fragment.initList();
        fragment.initAdapter();
    }

    @Override
    public void onDiscard() {

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titlesTextView;
        TextView datesTextView;
        TextView contentsTextView;
        ImageView starImageView;
        CardView parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            titlesTextView=itemView.findViewById(R.id.titleTextView);
            datesTextView=itemView.findViewById(R.id.dateTextView);
            parentLayout=itemView.findViewById(R.id.cardViewNotesFragment);
            starImageView=itemView.findViewById(R.id.starImageView);
            contentsTextView=itemView.findViewById(R.id.contentTextView);
            titlesTextView.setVisibility(View.VISIBLE);
            datesTextView.setVisibility(View.VISIBLE);
            parentLayout.setVisibility(View.VISIBLE);
            contentsTextView.setVisibility(View.VISIBLE);
//            starImageView.setVisibility(View.INVISIBLE);
        }
    }
}
