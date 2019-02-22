package com.webpageparser;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity implements OnTaskCompleted {

    private Integer[] numbers = {1, 2, 3, 4, 5};
    private int progress = 0;
    private TextView mOutputTextView;
    private ProgressBar mProgressBar;
    private Button mStartButton;
    private EditText mEntryURL;
    private String firstURL;
    private TextView mTextProgressBar;
    private ArrayList<List<String>> mainLinksList, mainEmailsList;
    private int deep = 2;
    private int cur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createSpinner(); //Созд поле выбора

        mainEmailsList = new ArrayList<>();
        mainLinksList = new ArrayList<List<String>>();

        mOutputTextView = (TextView) findViewById(R.id.resultOutput);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextProgressBar = (TextView) findViewById(R.id.textProgressBar);
        mStartButton = (Button) findViewById(R.id.buttonStart);
        mEntryURL = (EditText) findViewById(R.id.entryURL);
        mStartButton.setOnClickListener(onButtonClick);

    }

    @Override
    public void onTaskCompleted(List<String> responseLinksList, List<String> responseEmailsList) {

        if (responseLinksList.size() != 0) mainLinksList.add(responseLinksList);
        if (responseEmailsList.size() != 0) mainEmailsList.add(responseEmailsList);
//            System.out.println("SIZE mainLinksList = " + mainLinksList.size());
        while (cur < deep) {

            for (int j = 0; j < mainLinksList.get(cur).size(); j++) {
                newUrlContent(mainLinksList.get(cur).get(j));
            }
            postProgress(progress + 10);
            cur++;
        }
        mOutputTextView.setText(mainLinksList.toString());
//        mOutputTextView.setText(mainEmailsList.toString());
    }

    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            firstURL = mEntryURL.getText().toString();
            newUrlContent(firstURL);
            progress = progress + 10;
            postProgress(progress);
        }
    };

    private void newUrlContent(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
                new GetUrlContentTask(MainActivity.this).execute(url);
            } else {
                Toast.makeText(getApplicationContext(), "The request url is not a valid http or https url.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "The request url can not be empty.", Toast.LENGTH_LONG).show();
        }
    }

    private void postProgress(int progress) {
        String strProgress = String.valueOf(progress) + " %";
        mProgressBar.setProgress(progress);

        if (progress == 0) {
            mProgressBar.setSecondaryProgress(0);
        } else {
            mProgressBar.setSecondaryProgress(progress + 10);
            mTextProgressBar.setText(strProgress);
        }
    }

    private void createSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("select search depth");
    }
}