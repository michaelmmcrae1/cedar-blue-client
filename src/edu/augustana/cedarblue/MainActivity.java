package edu.augustana.cedarblue;

import java.util.ArrayList;

import edu.augustana.cedarblue.R;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	public ListView topicList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		topicList = (ListView) findViewById(R.id.topicList);
		
		String[] interestTopics = new String[] {"Food", "Sports", "Greek", "Studying", "Other"};
		
		ArrayAdapter<String> topicListAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, interestTopics); 
		
		topicList.setAdapter(topicListAdapter);
		
		topicList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View chosen,
				int position, long id) {
			    Toast.makeText(getApplicationContext(),
			      "Click ListItem Number " + position, Toast.LENGTH_LONG)
			      .show();
			    Object itemClicked = parent.getItemAtPosition(position);
				String topicString = itemClicked.toString();
				Intent myIntentObject = new Intent(MainActivity.this, TopicBoardActivity.class);
				myIntentObject.putExtra("topicString", topicString);
				MainActivity.this.startActivity(myIntentObject);	
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
