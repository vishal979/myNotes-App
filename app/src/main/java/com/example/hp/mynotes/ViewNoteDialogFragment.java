package com.example.hp.mynotes;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class ViewNoteDialogFragment extends Fragment implements FullScreenDialogContent {

    private FragmentActivity myContext;
    private FullScreenDialogController dialogController;
    private EditText titleET,contentET;
    private TextView dateTV;
    private Calendar calendar;
    private String oldTitle;
    private String Date;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_note_dialog,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleET=(EditText) getView().findViewById(R.id.titleEditText);
        contentET=(EditText) getView().findViewById(R.id.contentEditText);
        dateTV=(TextView) getView().findViewById(R.id.dateTextView);
        calendar=Calendar.getInstance();
        fragmentManager=myContext.getFragmentManager();
        titleET.setText(getArguments().getString("title"));
        contentET.setText(getArguments().getString("content"));
        dateTV.setText(getArguments().getString("date"));
        oldTitle=titleET.getText().toString();
        Date=dateTV.getText().toString();
        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        titleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialogController.setConfirmButtonEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialogController.setConfirmButtonEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController=dialogController;
    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        Bundle result=new Bundle();
        result.putString("oldTitle",oldTitle);
        result.putString("title",titleET.getText().toString());
        result.putString("content",contentET.getText().toString());
        result.putString("date",Date);
        dialogController.confirm(result);
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }

    private void showDatePickerDialog(){
        final int Day=calendar.get(Calendar.DAY_OF_MONTH);
        final int Month=calendar.get(Calendar.MONTH);
        final int Year=calendar.get(Calendar.YEAR);
//        Toast.makeText(this, "date picker ", Toast.LENGTH_SHORT).show();
        DatePickerDialog dg=DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Date=dayOfMonth + "/" +(monthOfYear+1)+"/"+year;
//                Toast.makeText(MainActivity.this, "Date: "+Date, Toast.LENGTH_SHORT).show();
//                showTimePickerDialog();
                dateTV.setText(Date);
            }
        },Year,Month,Day);
        dg.setScrollOrientation(DatePickerDialog.ScrollOrientation.HORIZONTAL);
        dg.show(fragmentManager,"date dialog");
    }
}
