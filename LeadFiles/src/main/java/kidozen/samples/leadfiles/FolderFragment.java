package kidozen.samples.leadfiles;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
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
    private AdapterView.OnItemClickListener _onItemClickCallback;
    JSONArray _files = null;

    public FolderFragment(JSONArray files, AdapterView.OnItemClickListener callback)
    {
        super();
        _onItemClickCallback = callback;
        _files = files;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        try {

            View view = inflater.inflate(R.layout.sharefile,container,false);
            GridView gridView = (GridView) view.findViewById(R.id.gridview);
            gridView.setAdapter(new ShareFileGridViewAdapter(getActivity(), _files));
            gridView.setOnItemClickListener(_onItemClickCallback);
            registerForContextMenu(gridView);
            return gridView;
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, v.getId(), 0, "Download");
        menu.add(Menu.NONE, v.getId(), 1, "Leads");
        menu.add(Menu.NONE, v.getId(), 2, "Email");
        menu.add(Menu.NONE, v.getId(), 3, "Assign");
    }
}

