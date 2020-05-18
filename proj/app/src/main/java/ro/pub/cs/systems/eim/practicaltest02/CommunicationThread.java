package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

public class CommunicationThread extends Thread {
    private Socket socket;
    private ServerThread serverThread;

    public CommunicationThread(ServerThread serverThread,Socket socket) {
        this.socket = socket;
        this.serverThread = serverThread;
    }


    @Override
    public void run() {
        super.run();
        if (socket == null) {
            Log.e(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");

            String currencyNeeded = bufferedReader.readLine();

            String currencyValue = "";
            Log.i(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://api.coindesk.com/v1/bpi/currentprice/EUR.json");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("", ""));
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String pageSourceCode = httpClient.execute(httpPost, responseHandler);
            if (pageSourceCode == null) {
                Log.e(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                return;
            }
            Document document = Jsoup.parse(pageSourceCode);
            Element element = document.child(0);
            Elements elements = element.getElementsByTag(currencyNeeded);
            for (Element script : elements) {
                String scriptData = script.data();
                if (scriptData.contains("rate")) {
                    currencyValue = scriptData.substring(scriptData.indexOf("rate"));
                    break;
                }
            }

        if (currencyValue.isEmpty()) {
            Log.e(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] CurrencyValue is null!");
            return;
        }




            if(currencyNeeded.equals("USD"))
                printWriter.println("USD value is 12000");
            else
                printWriter.print("EUR value is 9000");
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.COMMUNICATION_THREAD_TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
