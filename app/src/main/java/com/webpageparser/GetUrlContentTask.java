package com.webpageparser;

import android.os.AsyncTask;

import java.io.*;
import java.net.*;

class GetUrlContentTask extends AsyncTask<String, Integer, String> {
    private OnTaskCompleted taskCompleted;

    public GetUrlContentTask(OnTaskCompleted activityContext) {
        this.taskCompleted = activityContext;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... urls) {
        String inputLine;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urls[0]);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine);
            in.close();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    protected void onPostExecute(String res) {
        taskCompleted.onTaskCompleted(Parser.getAllLinksOnPage(res), Parser.getAllMails(res));
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }
}