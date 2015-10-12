package com.ivo.couchbaseevents;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.AuthenticatorFactory;
import com.couchbase.lite.replicator.Replication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
https://github.com/couchbaselabs/GrocerySync-Android
http://demo.mobile.couchbase.com/grocery-sync/
http://www.macupdate.com/app/mac/20490/http-scoop
 */

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "CouchbaseEvents";
    public static final String DB_NAME = "couchbaseevents";

    private Database database;
    private Manager manager;
    String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            helloCBL();
        } catch (CouchbaseLiteException e) {
            Log.e("", e.getMessage());
        } catch (IOException e) {
            Log.e("", e.getMessage());
        }

        // retrieve the document from the database
        Document doc = database.getDocument(documentId);
        if (doc != null) {
            // display the retrieved document
            Log.i(TAG, "retrievedDocument=" + String.valueOf(doc.getProperties()));
        } else {
            Log.e(TAG, "doc id not found id=" + documentId);
        }

        // delete the document
//        try {
//            doc.delete();
//            Log.i (TAG, "Deleted document, deletion status = " + doc.isDeleted());
//        } catch (CouchbaseLiteException e) {
//            Log.e (TAG, "Cannot delete document", e);
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
*/

    public Database getDatabase() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }

    public Manager getManager() throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    private void helloCBL() throws CouchbaseLiteException, IOException {
        Log.i(TAG, "Begin Couchbase Events App");

        try {
            getManager();
            getDatabase();
            startReplications();
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return;
        }
        // Create the document
        documentId = createDocument(database);
        Log.i(TAG, "Created document with Id=" + documentId);

//        // Get and output the contents
//        outputContents(database, documentId);
        // Update the document and add an attachment
        updateDoc(documentId);
        // Add an attachment
        addAttachment(documentId);
        // Get and output the contents with the attachment
        outputContentsWithAttachment(documentId);
        }

    private String createDocument(Database database) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Big Party");
        map.put("location", "My House");
        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    private void updateDoc(String documentId) throws CouchbaseLiteException {
        Document document = getDatabase().getDocument(documentId);
        try {
            // Update the document with more data
            Map<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.putAll(document.getProperties());
            updatedProperties.put("eventDescription", "Everyone is invited!");
            updatedProperties.put("address", "123 Elm St.");
            // Save to the Couchbase local Couchbase Lite DB
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }

    private void addAttachment(String documentId) throws CouchbaseLiteException {
        Document document = getDatabase().getDocument(documentId);
        try {
            // Add an attachment with sample data as POC
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0, 1, 2, 127 });
            UnsavedRevision revision = document.getCurrentRevision().createRevision();
            // Save doc & attachment to the local DB
            revision.setAttachment("binaryData", "application/octet-stream", inputStream);
            revision.save();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }

    private void outputContentsWithAttachment(String documentId) throws IOException, CouchbaseLiteException {
        Document fetchedSameDoc = getDatabase().getExistingDocument(documentId);
        SavedRevision saved = fetchedSameDoc.getCurrentRevision();
        // The content of the attachment is a byte[] we created
        Attachment attach = saved.getAttachment("binaryData");
        int i = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(attach.getContent()));
        StringBuffer values = new StringBuffer();
        while (i++ < 4) {
            // We knew the size of the byte array
            // This is the content of the attachment
            values.append(reader.read() + " ");
        }
        Log.i(TAG, "The docID: " + documentId + ", attachment contents was: " + values.toString());
    }

    private URL createSyncURL(boolean isEncrypted){
        URL syncURL = null;
        // NOTE special host for emulator!!!!
        String host = "http://10.0.2.2";  // "http://192.168.61.103";

        String port = "4984";
        //String dbName = "couchbaseevents";
        String dbName = DB_NAME;
        try {
            syncURL = new URL(host + ":" + port + "/" + dbName);
        } catch (MalformedURLException me) {
            Log.e("", me.getMessage());
        }
        return syncURL;
    }

    private void startReplications() throws CouchbaseLiteException {
        Replication pull = this.getDatabase().createPullReplication(this.createSyncURL(false));
        Replication push = this.getDatabase().createPushReplication(this.createSyncURL(false));
        Authenticator authenticator = AuthenticatorFactory.createBasicAuthenticator("couchbase_user", "mobile");
        pull.setAuthenticator(authenticator);
        push.setAuthenticator(authenticator);
        pull.setContinuous(true);
        push.setContinuous(true);
        pull.start();
        push.start();
    }

}
