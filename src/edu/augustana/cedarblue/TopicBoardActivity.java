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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TopicBoardActivity extends Activity implements View.OnClickListener {
	public JSONArray jArray;
	public String topicString;
	public TextView text1;
	public TextView text2;
	public TextView text3;
	public TextView text4;
	public TextView text5;
	public Button submitButton;

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    /*
		 * Rather than create an AsyncTask to connect over HTTP, I just want to do it here
		 * as a demo. This is a quick hack that allows the UI thread to use HTTP
		 */
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 
	
	    // Get the most recent posts for this topic, store in jArray
	    Bundle nameHashMap = this.getIntent().getExtras();
        topicString = nameHashMap.getString("topicString");
        
        submitButton = (Button) findViewById(R.id.button1);
        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);
        text5 = (TextView) findViewById(R.id.textView5);
        
        submitButton.setOnClickListener(this);
        
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
	    HttpPost httppost = new HttpPost("http://10.100.31.21/cblue/getPostsFromDB.php");
	    Toast.makeText(getApplicationContext(), "Topic: " + topicString, Toast.LENGTH_LONG).show();
	    try {
	        // Bind values with List
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("topic", topicString));
	        
	        // Encode values on URL entity
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        Log.e("log_tag", "In try");
	        
	        // Execute HTTP Post, capture response
	        HttpResponse response = httpclient.execute(httppost);
	        Log.e("log_tag", "Made it past http.execute");
	        
	        // Begin to retrieve JSON data from server
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream is = entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
	            // Use StringBuilder sb to acquire entire response as a String
	            StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				
				String result = sb.toString();
				Log.e("log_tag", "Result: " + result);
				
				/*
				 *  Instantiate JSONArray with our String
				 *  We now have the data in a JSONArray. We will use this JSONArray to populate TextViews
				 *  with messages.
				 */
	            jArray = new JSONArray(result);
	            
				Log.e("log_tag", "JSONArray: " + jArray.toString());
				//Toast.makeText(getApplicationContext(), "" + jObject.getString("message"), Toast.LENGTH_LONG).show();
				Log.e("log_tag", "JSON at 0: " + jArray.get(0));
	        }
	        Toast.makeText(getApplicationContext(), "Entity is null", Toast.LENGTH_LONG).show();
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void populateTextViews() {
		
	}
	
	
	/*
	 * Take user's text from editText, and load it into correct topic table on server.
	 * Then, reload current topic and redisplay posts, including the user's recent submission
	 */
	@Override
	public void onClick(View arg0) {
		Toast.makeText(getApplicationContext(), "Clicked: " + arg0.toString(), Toast.LENGTH_LONG).show();
		
	}
	

}
