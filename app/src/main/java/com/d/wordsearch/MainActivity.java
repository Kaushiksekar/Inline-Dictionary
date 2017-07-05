package com.d.wordsearch;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM = "arg_selected_item";
//    private String checkFragmentHistory="No History";
    private BottomNavigationView bottomNavigationView;
    private int mSelectedItem=1;
    String titleBarText;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar) findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);

        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                checkFragmentHistory="History Changed";
                selectFragment(item);
                return true;
            }
        });


        MenuItem menuItem;
        if(savedInstanceState!=null){
            mSelectedItem=savedInstanceState.getInt(SELECTED_ITEM,0);
            menuItem=bottomNavigationView.getMenu().findItem(mSelectedItem);
        }
        else{
            menuItem=bottomNavigationView.getMenu().getItem(mSelectedItem);
        }
        selectFragment(menuItem);
    }

    private void selectFragment(MenuItem item){
        Fragment fragment=null;

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();

        int position=0;

        switch (item.getItemId()){
            case R.id.action_history:
                fragment=new HistoryFragment();
                titleBarText="History";
                position=0;
                break;
            case R.id.action_home:
                fragment=new HomeFragment();
                titleBarText="Home";
                position=1;
                break;
            case R.id.action_preferences:
                fragment=new PreferencesFragment();
                titleBarText="Preferences";
                position=2;
                break;
        }
        mSelectedItem=item.getItemId();
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
//        fragmentTransaction.addToBackStack(null);
//        if(checkFragmentHistory.equals("History Changed")){
//            getSupportFragmentManager().popBackStackImmediate();
//        }
        fragmentTransaction.commit();
        updateToolBarTitle(titleBarText);

//        for(int i=0;i<bottomNavigationView.getMenu().size();i++){
            MenuItem menuItem=bottomNavigationView.getMenu().getItem(position);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
//        }
    }

    private void updateToolBarTitle(String text){
//        ActionBar actionBar=getSupportActionBar();
//        if(actionBar!=null){
//            actionBar.setTitle(text);
//        }
//        getSupportActionBar().setTitle(text);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM,mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem=bottomNavigationView.getMenu().getItem(1);
        if(mSelectedItem!=homeItem.getItemId()){
            selectFragment(homeItem);
        }
        else{
            super.onBackPressed();
        }
    }
}