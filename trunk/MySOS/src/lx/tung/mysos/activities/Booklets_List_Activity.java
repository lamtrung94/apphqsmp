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

public class Booklets_List_Activity extends Activity{
	private ListView mListView;
	private LayoutInflater mInflater;
	private String[] titleList;
	private FirstAid_Menu_Entity rd;
	private TextView mlblTitle;
	
	public void onBackPressed() {
		Intent i = new Intent();
		i.setClass(Booklets_List_Activity.this, Home_Activity.class);
		startActivity(i);
		this.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mysosbookletslist);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title_with_nobutton);
		mListView = (ListView) findViewById(R.id.list_view);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Initialize();
	}
	
	private void Initialize(){
		mlblTitle = (TextView) findViewById(R.id.lblTitle);
		mlblTitle.setText("");
		mlblTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.settingsicon), null, null, null);
		
		Vector<FirstAid_Menu_Entity> data = new Vector<FirstAid_Menu_Entity>();
		titleList = getResources().getStringArray(R.array.fa_titles);
		for (int i = 0; i < titleList.length; i++) {
			rd = new FirstAid_Menu_Entity(i, titleList[i]);
			data.add(rd);
		}

		Booklets_Menu_Adapter adapter = new Booklets_Menu_Adapter(
				this, R.layout.item_submenu_booklets_list, R.id.lblTitle,
				data, mInflater);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent i = new Intent();
				i.putExtra("POS_CLICKED", position);
				i.setClass(Booklets_List_Activity.this, Booklet_Activity.class);
				startActivity(i);
				Booklets_List_Activity.this.finish();
			}
		});
	}
}
