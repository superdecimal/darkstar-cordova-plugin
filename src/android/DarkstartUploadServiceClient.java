package gr.darkstar.darkstaruploadservice;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import android.app.Activity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.RemoteException;
import android.util.Log;
import android.content.ServiceConnection;
import android.content.Intent;
import android.content.ComponentName;
import android.os.IBinder;
import android.content.Context;
import android.os.Messenger;
import android.os.Message;
import android.os.Bundle;

public class DarkstarUploadServiceClient extends CordovaPlugin {
    private final String ACTION_SETTOKEN = "setToken";
    private final String ACTION_PING = "ping";
    private final String ACTION_BIND = "bindService";
    Messenger mService = null;
    boolean mBound;
    private Context context;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        context = cordova.getActivity().getApplicationContext();
        super.initialize(cordova, webView);

        Intent rServiceIntent = new Intent("gr.darkstar.darkstaruploadservice.DarkstarUploadService");
        rServiceIntent.setPackage("gr.darkstar.darkstaruploadservice");
        context.bindService(rServiceIntent, mConnection, Context.BIND_AUTO_CREATE  );

    }

    @Override
    public void onResume(boolean multitasking) {

    }


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            Log.e("DarkstarUploadServiceClient", "Service connected");
            mBound = true;
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.e("DarkstarUploadServiceClient", "Service disconnected");
            mService = null;
            mBound = false;
        }
    };


    public void sendToken(String token) {
     Log.e("DarkstarkUploadServiceClient", token);
     if (!mBound) return;
        // Create and send a message to the service, using a supported 'what' value
     Message msg = Message.obtain(null, 1, 0, 0);
     try {
        Bundle bundle = new Bundle();
        bundle.putString("token", token);
        msg.setData(bundle);
        mService.send(msg);
    } catch (RemoteException e) {
        e.printStackTrace();
    }
}

public void ping(String pingText){
 if (!mBound) return;
        // Create and send a message to the service, using a supported 'what' value
 Message msg = Message.obtain(null, 2, 0, 0);
 try {
    Bundle bundle = new Bundle();
    bundle.putString("pingTest", pingText);
    msg.setData(bundle);
    mService.send(msg);
} catch (RemoteException e) {
    e.printStackTrace();
}
}


@Override
public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{
    if(action.equals(ACTION_SETTOKEN)){
        String token = args.getString(0);
        sendToken(token);
        callbackContext.success();
        return true
    }
    if (action.equals(ACTION_PING)){
        ping(args.getString(0));
        callbackContext.success();
        return true;
    }

    return false;
}

}
