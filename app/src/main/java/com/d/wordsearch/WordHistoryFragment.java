package com.d.wordsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class WordHistoryFragment extends Fragment {

    ArrayList<WordHistoryModel> listitems=new ArrayList<>();
    RecyclerView myRecyclerView;
    String Words[]={"Elusive","Dissident","Intrusive","Procrustean","Incantation","Epoch","de facto","de jure"};
    String definitions[]={"difficult to catch or retreive","person who opposes official policy","causing disruption","enforcing uniformity","reciting a magical spell","particular time period in history","in theory","by law"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
    }

    public void initializeList(){
        listitems.clear();

        for(int i=0;i<Words.length;i++){
            WordHistoryModel item=new WordHistoryModel();
            item.setCardName(Words[i]);
            item.setCardDefName(definitions[i]);
            item.setIsFav(0);
            item.setIsTurned(0);
            listitems.add(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_card, container, false);

        myRecyclerView=(RecyclerView) view.findViewById(R.id.wordHistoryCardView);
        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLinearLayoutManager=new LinearLayoutManager(getActivity());
        myLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(listitems.size()>0 & myRecyclerView!=null){
            myRecyclerView.setAdapter(new MyWordHistoryAdapter(listitems));
        }
        myRecyclerView.setLayoutManager(myLinearLayoutManager);

        return view;
    }

    public class MyWordHistoryHolderView extends RecyclerView.ViewHolder {

        public TextView wordHistoryTitleTextView;
        public TextView wordHistoryDefTextView;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public MyWordHistoryHolderView(View v) {
            super(v);
            wordHistoryTitleTextView=(TextView) v.findViewById(R.id.titleTextViewWordHistory);
            wordHistoryDefTextView=(TextView) v.findViewById(R.id.defTextViewWordHistory);
            likeImageView=(ImageView) v.findViewById(R.id.likeImageViewWordHistory);
            shareImageView=(ImageView) v.findViewById(R.id.shareImageViewWordHistory);

            likeImageView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    int id=(int) likeImageView.getTag();
                    if(id==R.drawable.ic_like){
                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);
                        Toast.makeText(getActivity(),wordHistoryTitleTextView.getText()+" add to favorites",Toast.LENGTH_SHORT).show();
//                        CardView mRoot=(CardView) v.findViewById(R.id.wordHistoryCardView);
//                        Snackbar snackbar=Snackbar.make(mRoot,wordHistoryTitleTextView.getText()+" add to favorites",Snackbar.LENGTH_LONG);
//                        snackbar.show();
                    }
                    else{
                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);
                        Toast.makeText(getActivity(),wordHistoryTitleTextView.getText()+" removed from favorites",Toast.LENGTH_SHORT).show();
//                        CardView mRoot=(CardView) v.findViewById(R.id.wordHistoryCardView);
//                        Snackbar snackbar=Snackbar.make(mRoot,wordHistoryTitleTextView.getText()+" add to favorites",Snackbar.LENGTH_LONG);
//                        snackbar.show();
                    }
                }
            });

            shareImageView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent shareIntent=new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"Hello");
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent,getResources().getText(R.string.send_to)));
                }
            });
        }
    }

    public class MyWordHistoryAdapter extends RecyclerView.Adapter<MyWordHistoryHolderView>{

        private ArrayList<WordHistoryModel> list;

        public MyWordHistoryAdapter(ArrayList<WordHistoryModel> Data){
            list=Data;
        }

        @Override
        public MyWordHistoryHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_items,parent,false);
            MyWordHistoryHolderView holder=new MyWordHistoryHolderView(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyWordHistoryHolderView holder, int position) {
            holder.wordHistoryTitleTextView.setText(list.get(position).getCardName());
            holder.wordHistoryDefTextView.setText(list.get(position).getCardDefName());
            holder.likeImageView.setTag(R.drawable.ic_like);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}