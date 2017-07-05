package com.d.wordsearch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BottomSheetFragment extends BottomSheetDialogFragment {

//    public void setupDialog(final Dialog dialog,int style){
//        super.setupDialog(dialog,style);
////        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
////            @Override
////            public void onShow(DialogInterface dialog) {
////                BottomSheetFragment bottomSheetFragment=(BottomSheetFragment) dialog;
////
////            }
////        });
//        View bottomSheetInternal=dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
//        BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
//        View contentView=View.inflate(getContext(),R.layout.fragment_bottom_sheet,null);
//        dialog.setContentView(contentView);
//    }

    TextView wordTV, defTV;
    String word,def;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_bottom_sheet,container,false);
        wordTV=(TextView) v.findViewById(R.id.bottomSheetWordTV);
        defTV=(TextView) v.findViewById(R.id.bottomSheetDefTV);

        Bundle args=getArguments();
        word=args.getString("word");
        def=args.getString("definition");

        def=def.replaceAll(";","\n");

        wordTV.setText(word);
        defTV.setText(def);

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d=(BottomSheetDialog) dialog;
                View bottomSheetInternal=d.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return v;
    }
}