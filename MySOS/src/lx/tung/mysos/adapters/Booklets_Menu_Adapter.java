package lx.tung.mysos.adapters;

import java.util.List;

import lx.tung.mysos.R;
import lx.tung.mysos.entities.FirstAid_Menu_Entity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Booklets_Menu_Adapter extends
		ArrayAdapter<FirstAid_Menu_Entity> {
	private LayoutInflater mInflater;
	private int mResource;

	public Booklets_Menu_Adapter(Context context,
			int resource, int textViewResourceId,
			List<FirstAid_Menu_Entity> objects, LayoutInflater minflacter) {
		super(context, resource, textViewResourceId, objects);
		mInflater = minflacter;
		mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		TextView title = null;
		FirstAid_Menu_Entity rowData = getItem(position);
		if (null == convertView) {
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		title = holder.getTitle();
		title.setText(rowData.mTitle);
		return convertView;
	}

	private class ViewHolder {
		private View mRow;
		private TextView title = null;

		public ViewHolder(View row) {
			mRow = row;
		}

		public TextView getTitle() {
			if (null == title) {
				title = (TextView) mRow.findViewById(R.id.lblTitle);
			}
			return title;
		}
	}

	@Override
	public int getCount() {
		return super.getCount();
	}
	
	@Override
	public FirstAid_Menu_Entity getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	@Override
	public int getPosition(FirstAid_Menu_Entity item) {
		return super.getPosition(item);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
