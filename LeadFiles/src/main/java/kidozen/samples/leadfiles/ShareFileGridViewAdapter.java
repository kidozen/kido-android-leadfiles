package kidozen.samples.leadfiles;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by christian on 5/29/13.
 */
public class ShareFileGridViewAdapter extends BaseAdapter {
    private final JSONArray _filesAndFolder;
    private Context mContext;
    private LayoutInflater inflater;
    private final String Tag = "ShareFileGridViewAdapter";

    public ShareFileGridViewAdapter(Context c, JSONArray response) {
        _filesAndFolder = response;
        mContext = c;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return _filesAndFolder.length();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        try {
            JSONObject sharefile = _filesAndFolder.getJSONObject(position);
            if(convertView == null) {
                view = inflater.inflate(R.layout.gridview_cell, null);
                ImageView image = (ImageView)view.findViewById(R.id.image);
                image.setImageResource(mThumbIds.get(sharefile.getString("type")));
                TextView text = (TextView)view.findViewById(R.id.title);
                text.setText(sharefile.getString("displayname"));
                view.setTag(sharefile);
            }
            else
                view = convertView;
        }
        catch (Exception e)
        {
            Log.e(Tag,e.getMessage());
            view =null;
        }
        return view;
    }
    private HashMap<String, Integer> mThumbIds = new HashMap<String, Integer>(){
        {
            put("folder",R.drawable.folder);
            put("file", R.drawable.file);
            put("box", R.drawable.folder);
        }
    };
}