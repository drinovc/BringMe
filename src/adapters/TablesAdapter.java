package adapters;

import java.util.Hashtable;

import com.banda.bringme.R;

import DataSources.Table;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TablesAdapter extends BaseAdapter {

	private Activity context;
	private Hashtable<Long,Table> tables;
	
	public TablesAdapter(Activity context, Hashtable<Long,Table> tables) {
		super();
		this.context = context;
		this.tables = tables;
	}
	
	@Override
    public int getCount() {
        try {
            int size = tables.size();
            return size;
        } catch(NullPointerException ex) {
            return 0;
        }
    }
	
    @Override
    public Table getItem(int i) {
		return ((Table)(tables.values().toArray()[i]));
    }
    
    @Override
	public long getItemId(int position) {
		return ((Table)(tables.values().toArray()[position])).getID();
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item_table, null, true);
		Table table = ((Table)(tables.values().toArray()[position]));
		rowView.setTag(R.string.table_id,table.getID());
		
		TextView numberBox = (TextView) rowView.findViewById(R.id.numberBox);		
		TextView nameBox = (TextView) rowView.findViewById(R.id.nameBox);
		nameBox.setText(table.getTableName());
		numberBox.setText(String.valueOf(table.getNumber()));
		
		TextView alertBox = (TextView) rowView.findViewById(R.id.alertBox);
		int alerts = table.getRequests();
		alertBox.setText(String.valueOf(alerts));
		if(alerts > 0){
			alertBox.setTextColor(Color.RED);
		}
		
		return rowView;
	}
}