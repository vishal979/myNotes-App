package com.example.hp.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.franmontiel.fullscreendialog.FullScreenDialogFragment;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener,FullScreenDialogFragment.OnConfirmListener,
        FullScreenDialogFragment.OnDiscardListener{

    DatabaseClassNotes dbhelper;
    static FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    ImageView starIV;
    SectionsPagerAdapter sectionsPagerAdapter;
    public static RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        starIV=(ImageView) findViewById(R.id.starImageView);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.parentLayout);
        dbhelper=new DatabaseClassNotes(this);
        sectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager=(ViewPager) findViewById(R.id.containerViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos=viewPager.getCurrentItem();
                Fragment fragment=sectionsPagerAdapter.getItem(pos);
                if(pos==0){
                    ((NotesFragment)fragment).initList();
                    ((NotesFragment)fragment).initAdapter();
                }else{
                    if(pos==1){
                    ((FavoritesNotesFragment)fragment).initList();
                    ((FavoritesNotesFragment)fragment).initAdapter();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        toolbar=(Toolbar) findViewById(R.id.profilesToolBar);
        setSupportActionBar(toolbar);
        relativeLayout=(RelativeLayout) findViewById(R.id.relLayoutParent);
        setupFab();
        setupViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu,menu);
        return true;
    }

    private void setupViewPager(){
        sectionsPagerAdapter.addFragment(new NotesFragment());
        sectionsPagerAdapter.addFragment(new FavoritesNotesFragment());
        ViewPager viewPager=(ViewPager)findViewById(R.id.containerViewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("NOTES");
        tabLayout.getTabAt(1).setText("FAVORITES");
    }

    public void shareviamail(String title,String content){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, ""+title);
        emailIntent.putExtra(Intent.EXTRA_TEXT,""+content);
        startActivity(Intent.createChooser(emailIntent, null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clearAllNotes:
                AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Are you sure you want to delete all the notes?");
                builder.setTitle("Clear Notes");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbhelper.deleteAll("SELECT * FROM notes_table");
                        int pos=viewPager.getCurrentItem();
                        Fragment fragment=sectionsPagerAdapter.getItem(pos);
                        if(pos==0){
                            ((NotesFragment)fragment).initList();
                            ((NotesFragment)fragment).initAdapter();
                        }else{
                            if(pos==1){
                                ((FavoritesNotesFragment)fragment).initList();
                                ((FavoritesNotesFragment)fragment).initAdapter();
                            }
                        }
                        Snackbar.make(coordinatorLayout,"Notes Cleared",Snackbar.LENGTH_SHORT).show();
                        NotesFragment.noNotesTV.setVisibility(View.VISIBLE);
                        FavoritesNotesFragment.favNotesTV.setVisibility(View.VISIBLE);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(), "No clicked", Toast.LENGTH_SHORT).show();
                    }
                }).setIcon(R.drawable.ic_priorityhigh_alert).show();
                break;
            case R.id.id_share_app:
                shareviamail("MyNotesApp","market://details?id="+getPackageName());
                break;

            case R.id.id_about_us:
                relativeLayout.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.add(R.id.frameLayoutForAboutUs,new AboutUsFragment());
                ft.addToBackStack("abc");
                ft.commit();
                break;

            case R.id.id_rate_us:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupFab(){
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle args=new Bundle();

        if(v.getId()==R.id.fab){
            new FullScreenDialogFragment.Builder(HomeActivity.this).setTitle("Add a Note")
                    .setConfirmButton("Save")
                    .setOnConfirmListener(HomeActivity.this)
                    .setOnDiscardListener(HomeActivity.this)
                    .setContent(NewNoteDialogFragment.class,args)
                    .build().show(getSupportFragmentManager(),"dialog");
        }
    }


    @Override
    public void onConfirm(@Nullable Bundle result) {
        boolean InsertData=dbhelper.addData(result.getString("title"),result.getString("content"),result.getString("date"),1);
        if(InsertData){
            Snackbar.make(coordinatorLayout,"Note added successfully",Snackbar.LENGTH_SHORT).show();
            int pos=viewPager.getCurrentItem();
            Fragment fragment=sectionsPagerAdapter.getItem(pos);
            if(pos==0){
                ((NotesFragment)fragment).initList();
                ((NotesFragment)fragment).initAdapter();
            }else{
                if(pos==1){
                    ((FavoritesNotesFragment)fragment).initList();
                    ((FavoritesNotesFragment)fragment).initAdapter();
                }
            }
        }else{
            Snackbar.make(coordinatorLayout,"Failed to create Note",Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onDiscard() {
    }
}
