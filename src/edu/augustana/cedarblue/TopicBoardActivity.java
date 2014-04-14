package edu.augustana.cedarblue;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class TopicBoardActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    Bundle nameHashMap = this.getIntent().getExtras();
        String topicString = nameHashMap.getString("topicString");
	    Toast.makeText(getApplicationContext(), "Topic: " + topicString , 
				   Toast.LENGTH_LONG).show();
	}

}
