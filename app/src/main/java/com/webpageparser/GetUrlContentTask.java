package com.webpageparser;

import android.os.AsyncTask;
import java.io.*;
import java.net.*;

// Класс AsyncTask предлагает простой и удобный механизм для перемещения трудоёмких операций в фоновый поток.
class GetUrlContentTask extends AsyncTask<String, Integer, String> {
    private OnTaskCompleted taskCompleted;

    public GetUrlContentTask(OnTaskCompleted activityContext) {
        this.taskCompleted = activityContext;
    }

    @Override
    protected void onPreExecute() {
        // Здесь находится то, что должно исполниться до того, как
        // начнётся основная работа. Например, вывод уведомления о
        // том, что пользователю следует подождать
    }

    @Override
    protected String doInBackground(String... urls) { //основной метод, который выполняется в новом потоке
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
        //выполняется после doInBackground() (может не вызываться, если AsyncTask был отменен).
        // Имеет доступ к UI(user interface). Используйте его для обновления пользовательского интерфейса,
        // как только ваша фоновая задача завершена. Данный обработчик при вызове синхронизируется с потоком GUI(graphical user interface),
        // поэтому внутри него вы можете безопасно изменять элементы пользовательского интерфейса.
        StringBuilder result = new StringBuilder(res);
        taskCompleted.onTaskCompleted(Parser.getAllLinksOnPage(result), Parser.getAllMails(result));
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        //Переопределите этот обработчик для публикации промежуточных обновлений в пользовательский интерфейс.
        // При вызове он синхронизируется с потоком GUI, поэтому в нём вы можете безопасно изменять элементы пользовательского интерфейса.
    }
}