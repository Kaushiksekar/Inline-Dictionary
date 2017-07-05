package com.d.wordsearch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;

public class HomeFragment extends Fragment {

    private EditText searchEditText;
    private Button wordSearchButton,otherSearchButton;
    private TextInputLayout inputLayoutSearch;
    private static final String SAVED_EDIT_TEXT="Welcome";
    private String getTextFromMemory;
    private ProgressBar progressBar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            getTextFromMemory=savedInstanceState.getString(SAVED_EDIT_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);

        searchEditText=(EditText) v.findViewById(R.id.search_edit_text);
        wordSearchButton=(Button) v.findViewById(R.id.word_search_button);
        otherSearchButton=(Button) v.findViewById(R.id.other_search_button);
        inputLayoutSearch=(TextInputLayout) v.findViewById(R.id.input_layout_search);
        progressBar=(ProgressBar) v.findViewById(R.id.progressBar);

        if(savedInstanceState!=null){
            searchEditText.setText(getTextFromMemory);
        }

        searchEditText.addTextChangedListener(new MyTextWatcher(searchEditText));

        wordSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                submitForm(v);
            }
        });

        otherSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                submitForm(v);
            }
        });

        return v;
    }

    private void submitForm(View v){
        if(!validateSearchField()){
            return;
        }
//        callBottomSheet();
        if(v.getId()==R.id.word_search_button){
            new CallbackTask().execute(dictionaryEntries());
        }
        else{
            new WikiClass().execute(wikiEntries());
        }
    }

    private boolean validateSearchField(){
        if(searchEditText.getText().toString().trim().isEmpty()){
            inputLayoutSearch.setError(getString(R.string.error_search_field));
            requestFocus(searchEditText);
            return false;
        }
        else{
            inputLayoutSearch.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view){
        if(view.requestFocus()){
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_EDIT_TEXT,searchEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private class MyTextWatcher implements TextWatcher{
        private View view;

        private MyTextWatcher(View view){
            this.view=view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateSearchField();
        }
    }

    private String dictionaryEntries() {
        final String language = "en";
        final String word = searchEditText.getText().toString();
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }

    private String wikiEntries() {
        final String word = searchEditText.getText().toString();
        String word1 = "";
        if (word.contains(" ")) {
            word1 = word.replace(" ", "_");
        } else {
            word1 = word;
        }
        return "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=" + word1;
    }

    public void callBottomSheet(Bundle args){
        BottomSheetFragment bottomSheetFragment=new BottomSheetFragment();
        bottomSheetFragment.setArguments(args);
        bottomSheetFragment.show(getFragmentManager(),bottomSheetFragment.getTag());
    }
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        ArrayList<String> definitions=new ArrayList<String>();
        ArrayList<String> examples=new ArrayList<String>();
        int maxLength=0;

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "0344eda7";
            final String app_key = "b53971a0e0ec830ed0c1b7038d2283c9";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

//                JSONObject data = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();
//                JSONArray locations = data.getJSONArray("results").getJSONObject(0).getJSONArray("lexicalEntries").getJSONObject(0).getJSONArray("entries").getJSONObject(0).getJSONArray("senses");
//                for (int i = 0; i < locations.length(); i++) {
//                    definitions.add(locations.getJSONObject(i).optString("definitions", ""));
//                }

                JSONObject data = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();
                JSONArray locations1=data.getJSONArray("results").getJSONObject(0).getJSONArray("lexicalEntries");
                JSONArray locations=data.getJSONArray("results").getJSONObject(0).getJSONArray("lexicalEntries");

                for(int i=0;i<locations.length();i++){
                    String a=locations.getJSONObject(i).getJSONArray("entries").getJSONObject(0).getJSONArray("senses").getJSONObject(0).getJSONArray("definitions").optString(0);
                    definitions.add(a.substring(0,a.length()-1));
                    examples.add(locations.getJSONObject(i).getJSONArray("entries").getJSONObject(0).getJSONArray("senses").getJSONObject(0).getJSONArray("examples").getJSONObject(0).optString("text"));
                }

                if(definitions.size()==examples.size()){
                    maxLength=definitions.size();
                }
                else if(definitions.size()>examples.size()){
                    maxLength=definitions.size();
                }
                else{
                    maxLength=examples.size();
                }

                return "List added";

            } catch (UnknownHostException e) {
                return "Sorry you don't have an internet connection.";
//                return "Sorry, the word you're looking for could not be found";
            } catch (Exception e) {
                e.printStackTrace();
//                return e.toString();
                return "The requested word could not be found";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
//            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            String word=searchEditText.getText().toString();
            String definition="";
            String example="";
            Log.d("result", result);
            if (result.equals("List added")) {
//                outputTV.setText("");
//                for (String a : definitions) {
//                    outputTV.append(a.substring(2, a.length() - 3) + "\n");
//                }

                for(int i=0;i<maxLength;i++){
//                    System.out.println("Word: "+definitions.get(i));
//                    System.out.println("Example: "+examples.get(i));
                    Log.d("Word",definitions.get(i));
                    Log.d("Example",examples.get(i));

                    definition=definition+";"+definitions.get(i);
                    example=example+";"+examples.get(i);
                }
            } else {
//                outputTV.setText(result);
                Log.d("Word",result);

                definition+=result;
            }
            Bundle args=new Bundle();
            args.putString("word",word);
            args.putString("definition",definition);
            args.putString("example",example);
            callBottomSheet(args);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

        }
    }

    private class WikiClass extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                JSONObject data = (JSONObject) new JSONTokener(IOUtils.toString(new URL(strings[0]))).nextValue();
                JSONObject pages = data.getJSONObject("query").getJSONObject("pages");
                Iterator<?> keys = pages.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (pages.get(key) instanceof JSONObject) {
                        result = pages.getJSONObject(key).getString("extract");
                    }
                }
                return result;
            } catch (UnknownHostException e) {
                return "Sorry you don't have an internet connection.";
//                return "Sorry, the word you're looking for could not be found";
            } catch (Exception e) {
//                return e.toString();
                return "Sorry, the word you're looking for could not be found";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            String word=searchEditText.getText().toString();
            BottomSheetFragment bottomSheetFragment=new BottomSheetFragment();
            Bundle bundle=new Bundle();
            if(s.length()==0) {
                s="Sorry, the word you're looking for could not be found";
            }
            else if(s.length()>2000) {
                s = s.substring(0, 2000);
                s = s + "...";
            }
            Log.d("result",s);
            Log.d("result",String.valueOf(s.length()));
            String definition=s;
            Bundle args=new Bundle();
            args.putString("word",word);
            args.putString("definition",s);
            callBottomSheet(args);
//            bundle.putString("bottomResult",s);
//            bottomSheetFragment.setArguments(bundle);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }
}