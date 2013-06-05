package kidozen.samples.leadfiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by christian on 6/3/13.
 */
public class SetPreferenceActivity extends Activity {

    private static final String TAG = "SetPreferenceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
    }

}