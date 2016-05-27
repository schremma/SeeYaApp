package com.group16.seeyaapp.communication;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Andrea on 10/04/16.
 * Service for sending json requests to a server and receiving json responses from a server.
 * Responses are forwarded to the object that called the service for sending a request.
 *
 */
public class CommunicationService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Socket socket;
    //private String ip = "10.0.2.2"; //localhost from emulator
    //private String ip = "89.133.200.141";
    private String ip = "213.65.110.13"; // our server :)
    //private String ip = "10.2.22.72";
    private int port = 7500;


    private DataInputStream dis;
    private DataOutputStream dos;

    private static final String TAG = "CommunicationService";


    /**
     *  Handler that receives messages from the thread.
     *  Each message contains a json string to be sent to a server.
     *  Response from the server is forwarded to the ComResultReceiver
     *  contained in the message, so that it eventually gets to the object listening to
     *  the ResultReceiver.
     */
    private final class ServiceHandler extends Handler {


        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            Log.i(TAG, "Message handling started");

            ServerTask sTask = (ServerTask) msg.obj;
            String jsonStr = sTask.json;


            String response = sendStringToServer(jsonStr);

            Bundle bundle = new Bundle();
            bundle.putString("result", response);
            sTask.resultReceiver.send(100, bundle);

            Log.i(TAG, "Result sent back from service");

        }

        /**
         * Sends a string to the server amd reads the response, also a string.
         * @param jsonString The string to send to the server
         * @return The server's response to the string sent
         */
        private String sendStringToServer(String jsonString) {
            String response = null;

            try {
                connectToServer();
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                return "Could not connect to server";
            }


            try {
                dos.writeUTF(jsonString);
                response = dis.readUTF();

            } catch (IOException e) {
                Log.d(TAG, "Error sending json to server: " + e.getMessage());
            }
            finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException ex) {
                    }
                }
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException ex) {
                    }
                }
            }

            return response;
        }
    }

    /**
     *  Start up the thread running the service.
     * A separate thread is created because the service normally runs in the process's
     *  main thread, which we don't want to block.  We also make it
     *  background priority so CPU-intensive work will not disrupt the GUI.
     */
    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        Log.i(TAG, "Service created");
    }

    /**
     * For each start request, send a message to the handler thread to start a job.
     * THe message contains a ServerTask with the string to send to the server and
     * the ResultReceiver instance into which the response from the server is to be placed.
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service started");

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;

        if (intent.getExtras() != null) {
            if (intent.hasExtra("receiver")) {
                final ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
                if (intent.hasExtra("json")) {
                    String json = intent.getStringExtra("json");
                    msg.obj = new ServerTask(resultReceiver, json);
                    mServiceHandler.sendMessage(msg);
                }
                else {
                    Log.i(TAG, "intent has no json extra");
                }
            }
            else {
                Log.i(TAG, "intent has no receiver extra");
            }
        }
        else {
            Log.i(TAG, "intent has no extras");
        }

        // If we get killed, after returning from here, do not restart
        return START_NOT_STICKY;
    }

    private void connectToServer() throws IOException {

        socket = new Socket(ip, port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());


        Log.i(TAG, "Connected to server at ip " + ip);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
    }

    /**
     * Represents a job to be sent to the HandlerThread:
     * it stores the string to send to the server and the ResultReceiver
     * instance into which the response from the server is to be placed.
     */
    private class ServerTask
    {
        private ResultReceiver resultReceiver;
        private String json;

        public ServerTask(ResultReceiver rReceiver, String json)
        {
            resultReceiver = rReceiver;
            this.json = json;
        }

        public ResultReceiver getResultReceiver() {return  resultReceiver;}
        public Object getJson() {return json;}
    }
}
