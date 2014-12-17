package lx.tung.mysos.activities;

import java.util.Vector;

import lx.tung.mysos.R;
import lx.tung.mysos.adapters.Booklets_Menu_Adapter;
import lx.tung.mysos.entities.FirstAid_Menu_Entity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Booklet_Activity extends Activity{
	private String[] titleList, contentList;
	private TextView mlblTitle, mtvTop, mtvContent;
	private int position = 0;
	
	public void onBackPressed() {
		Intent i = new Intent();
		i.setClass(Booklet_Activity.this, Booklets_List_Activity.class);
		startActivity(i);
		this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mysosbooklet);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title_with_nobutton);
		Initialize();
	}
	
	private void Initialize(){
		position = getIntent().getIntExtra("POS_CLICKED", -1);
		if(position == -1){
			Toast.makeText(Booklet_Activity.this, "Error!", Toast.LENGTH_LONG).show();
			Intent i = new Intent();
			i.setClass(Booklet_Activity.this, Booklets_List_Activity.class);
			startActivity(i);
			this.finish();
		}
		
		titleList = getResources().getStringArray(R.array.fa_titles);
		contentList = getResources().getStringArray(R.array.fa_contents);
		
		mlblTitle = (TextView) findViewById(R.id.lblTitle);
		mlblTitle.setText("");
		mlblTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.settingsicon), null, null, null);
		
		mtvTop = (TextView) findViewById(R.id.topTv);
		mtvTop.setText(titleList[position]);
		
		mtvContent = (TextView) findViewById(R.id.contentTv);
		mtvContent.setText(contentList[position]);
	}
}
