package com.example.mylibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Provider {
    private static Provider mInstance;

    private static final String URL = "https://api.github.com";
    private static final String URL_REP = URL + "/repositories";

    private static Context mContext = null;

    private final Logger mLog;
    private RequestQueue mRequestQueue = null;

    private Provider(Context context) {
        mContext = context;
        mLog = new Logger("<<IVO>>", context);
        mLog.toast(connected());

//        // Instantiate the cache
//        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//
//        // Set up the network to use HttpURLConnection as the HTTP client.
//        BasicNetwork network = new BasicNetwork(new HurlStack());
//
//        // Instantiate the RequestQueue with the cache and network.
//        mQueue = new RequestQueue(cache, network);
//
//        // Start the queue
//        mQueue.start();

    }

    public static synchronized Provider getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Provider(context);
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void queueRequest(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * on emulator - 533 ms 1st, 8-14 ms next
     * on S4       - 980 ms 1st, 100-200 ms next?
     */
    public void getRepos() {
        mLog.debug("getRepos " + URL_REP);
        final long start = System.nanoTime();

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, URL_REP, null,
                        new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        long elapsed = (int)((System.nanoTime() - start)/1000000);
                        mLog.toast("got result in " + elapsed + " ms, len: " + response.length());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String msg = error.getLocalizedMessage();
                        mLog.toast(msg.substring(msg.length()-80));
                    }});
        queueRequest(request);
    }


    /**
     * on emulator -  850 ms 1st,  90-100 ms next
     * on S4       - 1600 ms 1st, 500-600 ms next?
     */
    public void getRepos2() {
        mLog.debug("getRepos2 " + URL_REP);
        final long start = System.nanoTime();

        Map<String, String> headers = new HashMap<>();

        GsonRequest request = new GsonRequest
                (URL_REP, JsonArray.class, headers,
                        new Response.Listener<JsonArray>() {
                            @Override
                            public void onResponse(JsonArray response) {
                                long elapsed = (int)((System.nanoTime() - start)/1000000);
                                mLog.toast("got result in " + elapsed + " ms"); // , len: " + response.length());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String msg = error.getLocalizedMessage();
                                mLog.toast(msg.substring(msg.length()-80));
                            }});
        queueRequest(request);
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
