package com.samba;

import android.os.AsyncTask;
import android.util.Log;
import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;   // <- note: *Authenticator*, not Authentication
import jcifs.smb.SmbFile;
import jcifs.Configuration;
import java.lang.reflect.Field;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

/**
 * Created by shane on 9/23/16.
 */

public class sambaClient {
    public void run(){
        new LongOperation().execute("");
    }

    public static void dumpObject(String tag, Object obj) {
        if (obj == null) {
            Log.e(tag, "null");
            return;
        }

        Class<?> clazz = obj.getClass();
        Log.e(tag, "Class = " + clazz.getName());

        // Print inheritance chain
        Class<?> parent = clazz.getSuperclass();
        while (parent != null) {
            Log.e(tag, "Superclass = " + parent.getName());
            parent = parent.getSuperclass();
        }

        // Print implemented interfaces
        for (Class<?> i : clazz.getInterfaces()) {
            Log.e(tag, "Interface = " + i.getName());
        }

        // Print fields + values
        for (Field f : clazz.getDeclaredFields()) {
            try {
                f.setAccessible(true);
                Object value = f.get(obj);
                Log.e(tag, "Field: " + f.getName() + " = " + value);
            }
            catch (Exception e) {
                Log.e(tag, "Field: " + f.getName() + " = <inaccessible>");
            }
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            SmbFile[] listFiles = {};
            try {
                String yourPeerPassword = "normal";
                String yourPeerName = "normal";
                String yourPeerIP = "10.0.2.15";
                String path = "smb://" + yourPeerIP  + ":1139/";
                CIFSContext base = SingletonContext.getInstance();
                CIFSContext authCtx = base.withCredentials(
                        new NtlmPasswordAuthenticator("", yourPeerName, yourPeerPassword)
                );
                Configuration cfg = base.getConfig();

               /* NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                        null, yourPeerName, yourPeerPassword);*/
                Log.e("Connected", "Yes");
               // SmbFile smbFile = new SmbFile(path, auth);
                SmbFile smbFile = new SmbFile(path, authCtx);
                /** Printing Information about SMB file which belong to your Peer **/
                String nameoffile = smbFile.getName();
                String pathoffile = smbFile.getPath();
                Log.e(nameoffile, pathoffile);
                listFiles = smbFile.listFiles();
                Log.e(nameoffile, String.valueOf(listFiles.length));
                for(int i = 0;i < listFiles.length;i++){
                    Log.e("PATH", listFiles[i].toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Connected", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
