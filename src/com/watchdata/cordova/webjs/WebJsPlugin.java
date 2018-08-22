package com.watchdata.cordova.webjs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import com.watchdata.usdk.aidl.WDeviceProvider;

public class WebJsPlugin extends CordovaPlugin {
    private static final String TAG = "WebJsPlugin";
    private Context applicationContext;
    private WDeviceProvider cloudPayWD;

    private ServiceConnection connWD = new ServiceConnection() {
        public synchronized void onServiceConnected(ComponentName name, IBinder service) {
            cloudPayWD = WDeviceProvider.Stub.asInterface(service);
            Log.d(TAG,"onServiceConnected===>");
        }

        public void onServiceDisconnected(ComponentName name) {
            applicationContext.unbindService(connWD);
            cloudPayWD = null;
            Log.d(TAG,"onServiceDisconnected===>");
        }
    };
    private CallbackContext callbackContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.pluginInitialize();
        applicationContext =  cordova.getActivity().getApplicationContext();
        Log.d(TAG,"initialize===>");
        if (cloudPayWD == null) {
            onBindServiceWD();
        }
    }

    private void onBindServiceWD(){
        String service_name = "cloudpos.usdk.DEVICE_SERVICE";
        Intent intent = new Intent(service_name);
        applicationContext.bindService(intent, this.connWD, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if(TextUtils.isEmpty(action)){
            return false;
        }

        if(cloudPayWD == null) {
            //wait service started
            int cnt=300; //3s
            while(cnt > 0)
            {
                cnt--;
                if(cloudPayWD != null)
                    break;

                LOG.e(TAG, "   ret=null (" + cnt + ")");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(cloudPayWD == null) {
            //service still null
            return false;
        }
		
		//final Activity activity = this.cordova.getActivity();
		
        if (action.equals("getServiceVersion")){
            Log.d("WebJsHelper", "getServiceVersion===>");
            getServiceVersion();
        }else if (action.equals("printer_status")){
            Log.d("WebJsHelper", "printer_status===>");
            printer_status(args.getInt(0));
        }else if (action.equals("printer_setparam")){
            Log.d("WebJsHelper", "printer_setparam===" + args.getInt(1)+"===="+ args.getInt(2));
            printer_setparam(args.getInt(0),args.getInt(1),args.getInt(2),null);
        }else if (action.equals("printer_feedline")){
            Log.d("WebJsHelper", "printer_feedline==" + args.getInt(1));
            printer_feedline(args.getInt(0),args.getInt(1));
        }else if (action.equals("printer_drawhtml")){
            Log.d("WebJsHelper", "printer_drawhtml" + args.getString(1));
            printer_drawhtml(args.getInt(0),args.getString(1),args.getInt(2),args.getInt(3));
        }else{
            return false;
        }
        return true;
    }

    public void getServiceVersion() {
        String ver = "";
        try {
            Log.d(TAG, "getVersion()");
            if(cloudPayWD.getJSProvider() !=null)
                ver = cloudPayWD.getJSProvider().getServiceVersion();
        } catch (Exception var3) {
            var3.printStackTrace();
            ver = "";
        }
        Log.d(TAG, "   ret=" + ver);
        callbackContext.success(ver);
    }

    public void printer_status(int printer_id) {
		Log.d(TAG, "printer_status()");
        Log.d(TAG, "   printer_id=" + printer_id);

        int ret;
        try {
            ret = this.cloudPayWD.getJSProvider().printer_status(printer_id);
        } catch (Exception var4) {
            var4.printStackTrace();
            ret = -1;
        }
        Log.d(TAG, "   ret=" + ret);
        callbackContext.success(""+ret);
    }

    public void printer_setparam(int printer_id, int param_id, int arg1, String arg2) {
        Log.d(TAG, "Printer_Setparam()");
        Log.d(TAG, "   printer_id=" + printer_id);
        Log.d(TAG, "   param_id=" + param_id);
        Log.d(TAG, "   arg1=" + arg1);
        Log.d(TAG, "   arg2=" + (arg2 == null ? "null" : arg2));

        int ret;
        try {
            ret = this.cloudPayWD.getJSProvider().printer_setparam(printer_id, param_id, arg1, arg2);
        } catch (Exception var7) {
            var7.printStackTrace();
            ret = -1;
        }

        Log.d(TAG, "   ret=" + ret);
        if(ret == 0) {
            callbackContext.success("" + ret);
        }else{
            callbackContext.error("" + ret);
        }
    }

    public void printer_feedline(int printer_id, int pixel) {
        Log.d(TAG, "Printer_Setparam()");
        Log.d(TAG, "   printer_id=" + printer_id);
        Log.d(TAG, "   pixel=" + pixel);

        int ret;
        try {
            ret = this.cloudPayWD.getJSProvider().printer_feedline(printer_id, pixel);
        } catch (Exception var5) {
            var5.printStackTrace();
            ret = -1;
        }

        Log.d(TAG, "   ret=" + ret);
        if(ret == 0) {
            callbackContext.success(""+ret);
        }else{
            callbackContext.error(""+ret);
        }
    }

    public void printer_drawhtml(int printer_id, String html, int dummy_print, int saveBmp) {
        Log.d(TAG, "Printer_Printing()");
        Log.d(TAG, "   printer_id=" + printer_id);
        Log.d(TAG, "   html=" + (html == null ? "null" : html));
        Log.d(TAG, "   dummy_print=" + dummy_print);
        Log.d(TAG, "   saveBmp=" + saveBmp);

        String statusprint;
        String state = "";
        try {
            statusprint = this.cloudPayWD.getJSProvider().printer_drawhtml(printer_id, html, dummy_print != 0, saveBmp != 0);
            try {
                state = statusprint.split("\\|")[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusprint = "";
        }
        Log.d(TAG, "   ret=" + statusprint);
        if(state.equals("0")) {
            callbackContext.success(statusprint);
        }else{
            callbackContext.error(statusprint);
        }
    }

}
