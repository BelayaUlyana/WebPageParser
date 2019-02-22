package com.webpageparser;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity implements OnTaskCompleted {

    private TextView mOutputTextView;
    private ProgressBar mProgressBar;
    private TextView mTextProgressBar;
    private Button mStartButton;
    private EditText mEntryURL;
    private String firstURL;
    private ArrayList<List<String>> mainLinksList, mainEmailsList;
    private int spinnerPosition;
    private int cur = 0;
    private int progress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createSpinner();

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
        if (responseLinksList.size() != 0) mainLinksList.add(cur, responseLinksList);
        if (responseEmailsList.size() != 0) mainEmailsList.add(cur, responseEmailsList);
        System.out.println("SIZE mainLinksList = " + mainLinksList.get(cur).size() + ":::" + mainLinksList.size());
        if (cur < spinnerPosition) {
            System.out.println("cur < deep = " + cur + ":" + spinnerPosition);
            if (mainLinksList.size() != 0) {
                for (int k = 0; k < mainLinksList.get(cur).size(); k++) {
                    System.out.println("link = " + mainLinksList.get(cur).get(k));
                    newUrlContent(mainLinksList.get(cur).get(k));
                    postProgress(progress + 20);
                }
                postProgress(progress + 20);
                cur++;
            } else {
                System.out.println("List links is empty");
            }
        } else {
            System.out.println("End of getting links. size = " + mainLinksList.size());
//            mOutputTextView.setText(mainLinksList.toString() + mainLinksList.size());
            mOutputTextView.setText(mainEmailsList.toString());
        }
    }

    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            firstURL = mEntryURL.getText().toString();
            newUrlContent(firstURL);
            progress = progress + 20;
            postProgress(progress);
        }
    };


    private void postProgress(int progress) {
        String strProgress = String.valueOf(progress) + " %";
        mProgressBar.setProgress(progress);

        if (progress == 0) {
            mProgressBar.setSecondaryProgress(0);
        } else {
            mProgressBar.setSecondaryProgress(progress + 20);
            mTextProgressBar.setText(strProgress);
        }
    }

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

    private void createSpinner() {
        final Spinner spinner = findViewById(R.id.spinner);
        final List<Integer> numbers = new ArrayList<>();
        for (int j = 1; j < 6; j++)
            numbers.add(j);
        final ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = numbers.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerPosition = numbers.get(1);
            }
        });
    }
}