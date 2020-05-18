package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.client.ClientProtocolException;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private CommunicationThread communicationThread;

    ServerThread(int serverPort){
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            Log.e(Constants.SERVER_THREAD_TAG, "[SERVER THREAD] An exception has occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        try{
            while(!Thread.currentThread().isInterrupted()){
                Log.i(Constants.SERVER_THREAD_TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i(Constants.SERVER_THREAD_TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                communicationThread = new CommunicationThread(this,socket);
                communicationThread.start();
            }
        } catch (ClientProtocolException clientProtocolException) {
            Log.e(Constants.SERVER_THREAD_TAG, "[SERVER THREAD] An exception has occurred: " + clientProtocolException.getMessage());
            if (Constants.DEBUG) {
                clientProtocolException.printStackTrace();
            }
        } catch (IOException ioException) {
            Log.e(Constants.SERVER_THREAD_TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(Constants.SERVER_THREAD_TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
