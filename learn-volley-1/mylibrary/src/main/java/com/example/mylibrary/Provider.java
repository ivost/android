package com.example.mylibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class Provider {

    private static final String URL = "https://api.github.com";
    private static final String URL_REP = URL + "/repositories";


    private final Context mContext;
    private final Logger mLog;
    private final RequestQueue mQueue;

    public Provider(Context context) {
        mContext = context;;
        mLog = new Logger("<<IVO>>", context);
        mQueue = Volley.newRequestQueue(mContext);
        mLog.toast(connected());
    }

    public void getRepos() {
        mLog.debug("getRepos " + URL_REP);
        final long start = System.nanoTime();
        StringRequest request = new StringRequest(Request.Method.GET, URL_REP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long elapsed = (int)((System.nanoTime() - start)/1000000);
                        mLog.debug("got result in " + elapsed + " ms" + ", size: " + response.length());
                        //mLog.toast(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLog.toast(error.getMessage());
                    }});
        mQueue.add(request);
    }

    private String connected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        boolean conn = networkInfo != null && networkInfo.isConnected();
        if (conn) {
            return "connected: " + conn + ", network type: " + networkInfo.getType();
        } else {
            return "Not connected";
        }
    }

}

//////////////////////////////
//    public static List<RssItem> parse(String rssFeed) {
//        List<RssItem> list = new ArrayList<>();
//        Random r = new Random();
//        // random number of item but at least 5
//        Integer number = r.nextInt(10) + 5;
//        for (int i = 0; i < number; i++) {
//            // create sample data
//            String s = String.valueOf(r.nextInt(1000));
//            RssItem item = new RssItem("Summary " + s, "Description " + s);
//            list.add(item);
//        }
//        return list;
//    }
