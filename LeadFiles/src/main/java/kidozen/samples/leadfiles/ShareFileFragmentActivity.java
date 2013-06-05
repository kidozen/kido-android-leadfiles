package kidozen.samples.leadfiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kidozen.client.ServiceEvent;

/**
 * Created by christian on 5/30/13.
 */

public class ShareFileFragmentActivity extends Activity implements AdapterView.OnItemClickListener,
        ModelCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener
{
    private final String TAG="ShareFileFragmentActivity";
    private static Uri.Builder _uri = new Uri.Builder();

    private LeadFilesModel model = new LeadFilesModel();
    private final String rootFolder = "/";
    private boolean preferencesChanged = false;
    ProgressDialog mProgressDialog;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createProgressDialog();
        initializeModel();
    }

    private void initializeModel() {
        mProgressDialog.setMessage("Initializing, please wait ...");
        mProgressDialog.show();

        model.Tenant = mSharedPreferences.getString("edittext_tenant","");
        model.App = mSharedPreferences.getString("edittext_app","");
        model.User = mSharedPreferences.getString("edittext_user","");
        model.Password = mSharedPreferences.getString("edittext_password","");

        model.ShareFilePass = mSharedPreferences.getString("edittext_sharefile_password","");
        model.ShareFileUser = mSharedPreferences.getString("edittext_sharefile_user","");

        model.InitializeAndAuthenticate(this);
    }

    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {
        Uri uri = _uri.build();
        int count = uri.getPathSegments().size();
        if (count==1) {
            _uri = new Uri.Builder();
        }
        else {
            ArrayList<String> segments = new ArrayList<String>(uri.getPathSegments());
            segments.remove(count - 1);
            Iterator<String> it = segments.iterator();
            _uri = new Uri.Builder();
            while (it.hasNext())
                _uri.appendPath(it.next());
        }
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject tag =null;
        try {
            tag= (JSONObject) view.getTag();
            if (tag.getString("type").equals("file")){
                //todo: show file details
                return;
            }
        }
        catch (Exception ex){};
        if (mProgressDialog==null){
            createProgressDialog();
        }
        mProgressDialog.show();
        mProgressDialog.setMessage("Updating folder contents, please wait ...");

        String currentFolder = ((TextView)view.findViewById(R.id.title)).getText().toString();
        _uri.appendPath(currentFolder);
        model.GetFiles(_uri.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.setClass(ShareFileFragmentActivity.this, SetPreferenceActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (preferencesChanged)
        {
            model.Tenant = mSharedPreferences.getString("edittext_tenant","");
            model.App = mSharedPreferences.getString("edittext_app","");
            model.User = mSharedPreferences.getString("edittext_user","");
            model.Password = mSharedPreferences.getString("edittext_password","");

            model.ShareFilePass = mSharedPreferences.getString("edittext_sharefile_password","");
            model.ShareFileUser = mSharedPreferences.getString("edittext_sharefile_user","");
            this.initializeModel();
        }
    }

    @Override
    public void onInitAndAuthenticate(ServiceEvent e) {
        if (e.StatusCode== HttpStatus.SC_OK) {
            mProgressDialog.setMessage("Retrieving folder contents, please wait ...");
            model.GetFiles(rootFolder);
        }
        else {
            mProgressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Check the settings and try again").setTitle("Initialization error");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    public void onShareFileAdapterResponse(JSONArray files) {
        if (files!=null || files.length()>0) {
            mProgressDialog.dismiss();
        }
        Fragment newFragment = new kidozen.samples.leadfiles.FolderFragment(files, this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (_uri.toString().equals(rootFolder)) {
            ft.add(R.id.simple_fragment, newFragment);
        }
        else {
            ft.replace(R.id.simple_fragment, newFragment);
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        preferencesChanged = true;
        mSharedPreferences = sharedPreferences;
    }
}
