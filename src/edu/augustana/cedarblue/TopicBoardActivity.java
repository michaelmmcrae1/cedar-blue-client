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

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

// These packages are for testing WebSockets functionality.
// WebSockets create a persistent/open TCP connection that will continually
// notify the client when there are updates in the database.
// This is an alternative to polling or AJAX.
/*
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
*/

public class TopicBoardActivity extends Activity implements View.OnClickListener {
	public String getUrl = "http://192.168.1.27/getPostsFromDB.php";
	public String insertUrl = "http://192.168.1.27/insertPostsIntoDB.php";
	public static JSONArray jArray;
	public static String topicString;
	public static TextView[] textFields;
	public static TextView text1;
	public static TextView text2;
	public static TextView text3;
	public static TextView text4;
	public static TextView text5;
	public static ScrollView scroller;
	public TextView header;
	public EditText textBox;
	public Button homeButton;
	public Button submitButton;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	//private GoogleApiClient client;

	/*
	// WebSockets variables
	private static final String TAG = "de.tavendo.test1";

	private final WebSocketConnection mConnection = new WebSocketConnection();
	
	
	private void start() {

	      final String wsuri = "ws://192.168.0.107:8080/cblue/src/MyApp/Chat.php";

	      try {
	         mConnection.connect(wsuri, new WebSocketHandler() {

	            @Override
	            public void onOpen() {
	               Log.d(TAG, "Status: Connected to " + wsuri);
	               mConnection.sendTextMessage("Hello, world!");
	            }

	            @Override
	            public void onTextMessage(String payload) {
	               Log.d(TAG, "Got echo: " + payload);
	            }

	            @Override
	            public void onClose(int code, String reason) {
	               Log.d(TAG, "Connection lost. " + reason);
	            }
	         });
	      } catch (WebSocketException e) {

	         Log.d(TAG, e.toString());
	      }
	}

	*/

	/**
	 * Called when the activity is first created.
	 */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_topicboard);

		// Get the topicString which tells us which topic we are in
		Bundle nameHashMap = this.getIntent().getExtras();
		topicString = nameHashMap.getString("topicString");
		Toast.makeText(getApplicationContext(),
				"Here is string result from topicList " + topicString, Toast.LENGTH_LONG)
				.show();
		Log.e("The topic String", topicString);

		homeButton = (Button) findViewById(R.id.button2);
		submitButton = (Button) findViewById(R.id.button1);

		text1 = (TextView) findViewById(R.id.textView1);
		text2 = (TextView) findViewById(R.id.textView2);
		text3 = (TextView) findViewById(R.id.textView3);
		text4 = (TextView) findViewById(R.id.textView4);
		text5 = (TextView) findViewById(R.id.textView5);

		textFields = new TextView[5];

		textFields[0] = text5;
		textFields[1] = text4;
		textFields[2] = text3;
		textFields[3] = text2;
		textFields[4] = text1;


		//scroller = (ScrollView) findViewById(R.id.ScrollView);

        /* This method is supposed to make the scroll bar default to the bottom
		 * but it doesn't seem to do anything for some reason
         
        scroller.post(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        */

		header = (TextView) findViewById(R.id.textView6);
		textBox = (EditText) findViewById(R.id.editText1);

		header.setText(topicString);

		homeButton.setOnClickListener(this);
		submitButton.setOnClickListener(this);

		GetJSONArrayTask JSONArrayTask = new GetJSONArrayTask();
		JSONArrayTask.execute(getUrl);

		// Call WebSockets method for testing
		// start();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}


	/*
	 * Take user's text from editText, and load it into correct topic table on server.
	 * Then, reload current topic and redisplay posts, including the user's recent submission
	 */
	public void onClick(View chosen) {
		Log.e("enter onClick", "Clicked id: " + chosen.getId());

		if (chosen == homeButton) {
			jArray = null;
			Intent myIntentObject = new Intent(this, MainActivity.class);
			this.startActivity(myIntentObject);
		} else if (chosen == submitButton && textBox.getText().toString().trim().length() != 0) {
			String userPost = textBox.getText().toString();

			//insert the text in textBox to the database
			InsertMessageTask insertTask = new InsertMessageTask();
			insertTask.execute(insertUrl, userPost);

			// Now get latest entries
			GetJSONArrayTask JSONArrayTask = new GetJSONArrayTask();
			JSONArrayTask.execute(getUrl);

			// clear the textBox
			textBox.setText("");
		}
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	/*
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("TopicBoard Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}
	*/
	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client.connect();
		//AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//AppIndex.AppIndexApi.end(client, getIndexApiAction());
		//client.disconnect();
	}


	public static class GetJSONArrayTask extends AsyncTask<String, Integer, JSONArray> {
		@Override
		protected JSONArray doInBackground(String... params) {

			/* ******************************
			URL urll = null;
			try {
				String registrationUrl = "http://192.168.1.27";
				urll = new URL(registrationUrl);
				Log.d("url", urll.toString());
				URLConnection connection = urll.openConnection();
				Log.d("connection", connection.toString());
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				Log.d("httpconnection", httpConnection.toString());
				int responseCode = httpConnection.getResponseCode();
				Log.d("response", "" + responseCode);
				if (responseCode == HttpURLConnection.HTTP_OK) {
					Log.d("MyApp", "access");
				} else {
					Log.w("MyApp", "failed: " + registrationUrl);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			 ***************************** */

			// This gives us the url
			String url = params[0];
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				// Bind values with List
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("topic", topicString.toLowerCase()));

				Log.d("B4 httppost.setEntity", "B4 httppost.setEntity");

				// Encode values on URL entity
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post, capture response
				HttpResponse response = httpclient.execute(httppost);

				// Begin to retrieve JSON data from server
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					//Log.d("entity isnt null: ", entity.toString() + "" + entity.getContent());
					InputStream is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));

					// Use StringBuilder sb to acquire entire response as a String
					StringBuilder sb = new StringBuilder();
					String line = null;
					Log.d("May 14", "Entity is not null");
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();

					String result = sb.toString();

					Log.d("May 14", "Response from server: " + result);

					/*
					 *  Instantiate JSONArray with our String
					 *  We now have the data in a JSONArray. We will use this JSONArray to populate TextViews
					 *  with messages in onPostExecute.
					 */
					jArray = new JSONArray(result);
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.d("NullPointerException", "Inside NullPointerException e");
			}

			// This jArray goes to onPostExecute
			return jArray;

		}

		protected void onPostExecute(JSONArray jsonResult) {
			Log.e("Get Post", "JSONArray jArray: " + jsonResult);
			//try {
			//	Log.e("our JSONArray", jsonResult.getJSONObject(2).toString());
			//} catch (JSONException e) {
			//	e.printStackTrace();
			//}
			/*
			 * Populate textfields with JSONArray result
			*/
			if (jsonResult == null) {

			} else {
				ArrayList<String> recentMessages = new ArrayList<String>();
				ArrayList<String> recentDates = new ArrayList<String>();
				try {
					for (int i = 0; i < jsonResult.length(); i++) {
						JSONObject post = jsonResult.getJSONObject(i);
						String message = post.getString("messages");
						String date = post.getString("timestamp");
						recentMessages.add(message);
						recentDates.add(date);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Log.e("recentMessages.length", "" + recentMessages.size() + " " + recentDates.size());
				for (int i = 0; i < recentMessages.size(); i++) {
					Log.e("check index", recentDates.get(i) + "\n" + recentMessages.get(i));
					textFields[i].setText(recentDates.get(i) + "\n" + recentMessages.get(i));
				}
			}
		}
	}


	public static class InsertMessageTask extends AsyncTask<String, Integer, Void> {
		protected Void doInBackground(String... params) {

			// get the url for the correct handling script
			String url = params[0];

			// get the text which user typed into textBox
			String message = params[1];

			Log.e("Insert doInBackground", "params[1]: " + params[1]);

			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("topic", topicString.toLowerCase()));
			Log.e("topic", topicString.toLowerCase());
			values.add(new BasicNameValuePair("message", message));
			Log.e("message", message);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			try {
				httppost.setEntity(new UrlEncodedFormEntity(values));
				httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute() {
			Log.e("onPostExecute", "Entered onPostExecute");
		}
	}
}
