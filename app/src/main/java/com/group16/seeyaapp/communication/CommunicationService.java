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
 * !! Not communicating yet with server
 */
public class CommunicationService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Socket socket;
    private String ip = "172.20.10.9"; //localhost from emulator
    private int port = 7500;

    private DataInputStream dis;
    private DataOutputStream dos;

    private static final String TAG = "CommunicationService";


    // Handler that receives messages from the thread
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

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }

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

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        Log.i(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service started");


        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;

        final ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
        String json = intent.getStringExtra("json");
        msg.obj = new ServerTask(resultReceiver, json);
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
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
