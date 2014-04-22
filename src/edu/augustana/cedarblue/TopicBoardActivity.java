package edu.augustana.cedarblue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class TopicBoardActivity extends Activity {
	public String topicString;

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    /*
		 * Rather than create an AsyncTask to connect over HTTP, I just want to do it here
		 * as a demo. This is a quick hack that allows the main activity to use HTTP
		 */
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 
	
	    // TODO Auto-generated method stub
	    Bundle nameHashMap = this.getIntent().getExtras();
        topicString = nameHashMap.getString("topicString");
        try {
			getPosts();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@SuppressLint("NewApi")
	public void getPosts() throws JSONException {
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://10.100.31.21/cblue/topicController.php");
	    Toast.makeText(getApplicationContext(), "Topic: " + topicString, Toast.LENGTH_LONG).show();
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("topic", topicString));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        Log.e("log_tag", "In try");
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        Log.e("log_tag", "Made it past http.execute");
	        // Begin to retrieve JSON data from server
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream is = entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
	            
	            StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				
				String result = sb.toString();
				Log.e("log_tag", "Result: " + result);
	            JSONArray jArray = new JSONArray(result);
	            
	            Log.e("log_tag", "Just before jArray.toString()");
				Log.e("log_tag", "JSON: " + jArray.toString());
				//Toast.makeText(getApplicationContext(), "" + jObject.getString("message"), Toast.LENGTH_LONG).show();
				Log.e("log_tag", "JSON at 1: " + jArray.get(1));
	        }
	        Toast.makeText(getApplicationContext(), "Entity is null", Toast.LENGTH_LONG).show();
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	}
	
	

}
