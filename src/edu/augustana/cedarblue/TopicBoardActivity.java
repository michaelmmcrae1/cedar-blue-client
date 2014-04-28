package edu.augustana.cedarblue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
	public TextView header;
	public EditText textBox;
	public Button homeButton;
	public Button submitButton;

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_topicboard);
	    
	    /*
		 * Rather than create an AsyncTask to connect over HTTP, I just want to do it here
		 * as a demo. This is a quick hack that allows the UI thread to use HTTP
		 */
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 
	
	    // Get the most recent posts for this topic, store in jArray
	    Bundle nameHashMap = this.getIntent().getExtras();
        topicString = nameHashMap.getString("topicString");
        Toast.makeText(getApplicationContext(), "Topic: " + topicString, Toast.LENGTH_LONG).show();
        
        homeButton = (Button) findViewById(R.id.button2);
        submitButton = (Button) findViewById(R.id.button1);
        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);
        text5 = (TextView) findViewById(R.id.textView5);
        header = (TextView) findViewById(R.id.textView6);
        textBox = (EditText) findViewById(R.id.editText1);
        
        //Log.e("log_tag", "Text 1: " + text1 + "\nText 2: " + text2
        //		+ "\nText 3: " + text3 + "\nText 4" + text4);
        
        homeButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        text1.setOnClickListener(this);
        
        try {
			getPosts();
			text1.setText("" + jArray.get(0));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}
	
	
	@SuppressLint("NewApi")
	public void getPosts() throws JSONException {
		try {
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://10.100.31.21/cblue/getPostsFromDB.php");
	    
	        // Bind values with List
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("topic", topicString));
	        
	        // Encode values on URL entity
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        // Execute HTTP Post, capture response
	        HttpResponse response = httpclient.execute(httppost);
	        
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
				
				/*
				 *  Instantiate JSONArray with our String
				 *  We now have the data in a JSONArray. We will use this JSONArray to populate TextViews
				 *  with messages.
				 */
	            jArray = new JSONArray(result);
	            
				//Toast.makeText(getApplicationContext(), "" + jObject.getString("message"), Toast.LENGTH_LONG).show();
				Log.e("log_tag", "JSON at 0: " + jArray.getString(0));
				Log.e("log_tag", "JSON at 0: " + jArray.getString(1));
	        }
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    } catch (NullPointerException e) {
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
	public void onClick(View chosen) {
		Button chosenButton = (Button) chosen;
		String buttonClicked = chosenButton.getText().toString().toLowerCase();
		Toast.makeText(getApplicationContext(), "Clicked: " + buttonClicked, Toast.LENGTH_LONG).show();

		if (buttonClicked.equals("home")) {
			Intent myIntentObject = new Intent(this, MainActivity.class);
			this.startActivity(myIntentObject);	
		} else if (buttonClicked.equals("submit")) {
			String userPost = textBox.getText().toString();
			Toast.makeText(getApplicationContext(), "User Post: " + userPost, Toast.LENGTH_LONG).show();
			
			try {
				// Create a new HttpClient and Post Header
			    HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://10.100.31.21/cblue/insertPostsIntoDB.php");
			     // Bind values with List
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("content", userPost));
		        nameValuePairs.add(new BasicNameValuePair("topic", topicString));
		        
		        // Encode values on URL entity
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        
		        // Execute HTTP Post, capture response
		        HttpResponse response = httpclient.execute(httppost);
		        
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
		            populateTextViews();
					Log.e("log_tag", "JSONArray: " + jArray.toString());
					//Toast.makeText(getApplicationContext(), "" + jObject.getString("message"), Toast.LENGTH_LONG).show();
					Log.e("log_tag", "JSON at 0: " + jArray.get(0));
		        }
		        
		    } catch (NullPointerException e) {
		    	e.printStackTrace();
		    } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		
	}
	
	

}
