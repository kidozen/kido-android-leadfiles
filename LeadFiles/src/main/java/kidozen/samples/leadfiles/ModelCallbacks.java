package kidozen.samples.leadfiles;

/**
 * Created by christian on 5/28/13.
 */
//
import kidozen.client.*;
import org.json.JSONArray;

public interface ModelCallbacks {
    void onInitAndAuthenticate(ServiceEvent e);
    void onShareFileAdapterResponse(JSONArray files);
}
