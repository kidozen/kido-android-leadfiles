package kidozen.samples.leadfiles;

//
import android.util.Log;
import kidozen.client.*;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by christian on 5/28/13.
 */
public class LeadFilesModel {
    private static final String TAG = "LeadFilesModel";
    private final String Provider = "Kidozen";

    public String Tenant;
    public String App;
    public String User;
    public String Password;

    public String ShareFilePass;
    public String ShareFileUser;

    private ShareFile _sharefile = null;

    KZApplication application = null;
    ModelCallbacks _initCallback = null;

    private ServiceEventListener onAuthCallback = new ServiceEventListener() {
        @Override
        public void onFinish(ServiceEvent serviceEvent) {
            _initCallback.onInitAndAuthenticate(serviceEvent);
        }
    };
    private ServiceEventListener onInitCallback = new ServiceEventListener() {
        @Override
        public void onFinish(ServiceEvent serviceEvent) {
            application.Authenticate(Provider,User,Password, onAuthCallback);
        }
    };

    public void InitializeAndAuthenticate(ModelCallbacks callback) {
        try {
            _initCallback = callback;
            application = new KZApplication(Tenant,App,true, onInitCallback);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    public void GetFiles(String path)
    {
        if (_sharefile==null)
            _sharefile = new ShareFile(ShareFileUser,ShareFilePass);
        _sharefile.GetFiles(path,_initCallback);
    }

    private class ShareFile {
        private static final String KZ_SHAREFILE_GETAUTHID_METHODID = "getAuthID";
        private static final String KZ_SHAREFILE_GETFILES_METHODID = "folder";
        private static final String SHAREFILE_SERVICE = "sharefile";

        private String authId = null;
        private String _username = null;
        private String _password = null;
        private String _path = null;

        private kidozen.client.Service fileshare = null;
        private ModelCallbacks _onShareFileCallback;

        private ServiceEventListener onFolderInvokeCallback =  new ServiceEventListener() {
            @Override
            public void onFinish(ServiceEvent e) {
                if (e.StatusCode == HttpStatus.SC_OK) {
                    JSONArray filesAndFolder = (JSONArray) e.Response;
                    _onShareFileCallback.onShareFileAdapterResponse(filesAndFolder);
                } else {
                    //TODO: handle error
                    Log.e(TAG, e.Body);
                }
            }
        };


        private ServiceEventListener onGetAuthIdCallback = new ServiceEventListener() {
            @Override
            public void onFinish(ServiceEvent e) {
                try {
                    if (e.StatusCode == HttpStatus.SC_OK) {
                        authId = ((JSONObject) e.Response).getString("authid");
                        GetFiles(_path, _onShareFileCallback);
                    } else {
                        //TODO: handle error
                        Log.e(TAG, e.Body);
                    }
                }
                catch (JSONException je)
                {
                    //TODO: handle error
                    Log.e(TAG, je.getMessage());
                }
            }
        };


        public ShareFile(String username, String password)
        {
            _username = username;
            _password = password;
            fileshare = application.LOBService(SHAREFILE_SERVICE);
        }

        public void GetFiles(String path, ModelCallbacks callback)
        {
            _onShareFileCallback = callback;
            _path = path;
            JSONObject data = new JSONObject();
            try {
                if (authId==null) {
                    data.put("username",_username);
                    data.put("password",_password);
                    fileshare.InvokeMethod(KZ_SHAREFILE_GETAUTHID_METHODID, data, onGetAuthIdCallback);
                }
                else {
                    data.put("authid",authId);
                    data.put("path",path);
                    fileshare.InvokeMethod(KZ_SHAREFILE_GETFILES_METHODID, data, onFolderInvokeCallback);
                }
            }
           catch (Exception e)
           {
               //TODO: handle
               Log.e(TAG, e.getMessage());
           }
        }
    }

}
