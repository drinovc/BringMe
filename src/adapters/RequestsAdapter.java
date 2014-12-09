package adapters;

import java.util.List;

import com.banda.bringme.R;
import com.banda.bringme.Request;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RequestsAdapter extends BaseAdapter {

	private Activity context;
	private List<Request> requests;
	
	public RequestsAdapter(Activity context, List<Request> requests) {
		super();
		this.context = context;
		this.requests = requests;
	}
	
	@Override
    public int getCount() {
        try {
            int size = requests.size();
            return size;
        } catch(NullPointerException ex) {
            return 0;
        }
    }
	
    @Override
    public Request getItem(int i) {
        return requests.get(i);
    }
    
    @Override
	public long getItemId(int position) {
		return 0; // TODO
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		
		View rowView = inflater.inflate(R.layout.list_item_request, null, true);
		Request request = requests.get(position);
		
		TextView requestTitle = (TextView) rowView.findViewById(R.id.requestTitle);
		requestTitle.setText(request.table);	
		
		TextView requestDescription  = (TextView) rowView.findViewById(R.id.requestDescription);
		requestDescription.setText(request.type);
				
		return rowView;
	}
	
	public void updateRequests(List<Request> requests) {
		this.requests.clear();
		this.requests.addAll(requests);
		this.notifyDataSetChanged();
	}	
}