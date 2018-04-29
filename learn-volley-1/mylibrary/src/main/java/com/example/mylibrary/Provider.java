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
import com.android.volley.toolbox.StringRequest;

public class Provider {

    private static final String URL = "https://api.github.com";
    private static final String URL_REP = URL + "/repositories";

    private final Context mContext;
    private final Logger mLog;
    private final RequestQueue mQueue;

    public Provider(Context context) {
        mContext = context;;
        mLog = new Logger("<<IVO>>", context);
        mLog.toast(connected());

        //mQueue = Volley.newRequestQueue(mContext);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        BasicNetwork network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mQueue = new RequestQueue(cache, network);

        // Start the queue
        mQueue.start();

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
