package com.samba.nanohttp;

import android.util.Log;

import org.json.JSONArray;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.util.ServerRunner;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
//import fi.iki.elonen.NanoHTTPD;

import jcifs.smb.SmbFile;

/**
 * Created by chris on 27/11/2015.
 */
public class MyHTTPD extends NanoHTTPD {

    /**
     * logger to log to.
     */
    private static final Logger LOG = Logger.getLogger(MyHTTPD.class.getName());

    public static void main(String[] args) {
        ServerRunner.run(MyHTTPD.class);
    }
    public MyHTTPD() throws IOException {
        super(30000);
        Log.e("MyHTTPD", "Created MyHTTPD");
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.e("MyHTTPD", "Serve called...");
        Method method = session.getMethod();
        String uri = session.getUri();
        MyHTTPD.LOG.info(method + " '" + uri + "' ");
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("username") == null) {
            msg += "<form action='?' method='get'>\n" + "  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
        } else {
            msg += "<p>Hello, " + parms.get("username") + "!</p>";
        }

        msg += "</body></html>\n";

        return Response.newFixedLengthResponse(msg);
        }

    }


