package edu.augustana.cedarblue;

import org.apache.http.client.methods.HttpGet;


import edu.augustana.cedarblue.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	public Button foodButton;
	public Button sportButton;
	public Button greekButton;
	public Button studyButton;
	public Button otherButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		foodButton = (Button) findViewById(R.id.button1);
		sportButton = (Button) findViewById(R.id.button2);
		greekButton  = (Button) findViewById(R.id.button3);
		studyButton  = (Button) findViewById(R.id.button4);
		otherButton  = (Button) findViewById(R.id.button5);
			 
		foodButton.setOnClickListener(this);
		sportButton.setOnClickListener(this);
		greekButton.setOnClickListener(this);
		studyButton.setOnClickListener(this);
		otherButton.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}

	@Override
	public void onClick(View chosen) {
		// TODO Auto-generated method stub
		Button chosenButton = (Button) chosen;
		String topicString = chosenButton.getText().toString().toLowerCase();
		
		Intent myIntentObject = new Intent(this, TopicBoardActivity.class);
		myIntentObject.putExtra("topicString", topicString);
		this.startActivity(myIntentObject);	
	}

}
