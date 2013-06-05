package kidozen.samples.leadfiles;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;

/**
 * Created by christian on 6/3/13.
 */
public class FolderFragment extends Fragment
{
    private static final String TAG = "FolderFragment";
    private AdapterView.OnItemClickListener _callback;
    JSONArray _files = null;

    public FolderFragment(JSONArray files, AdapterView.OnItemClickListener callback)
    {
        super();
        _callback = callback;
        _files = files;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        try {

            View view = inflater.inflate(R.layout.sharefile,container,false);
            GridView gridView = (GridView) view.findViewById(R.id.gridview);
            gridView.setAdapter(new ShareFileGridViewAdapter(getActivity(), _files));
            gridView.setOnItemClickListener(_callback);
            return gridView;
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }


}
