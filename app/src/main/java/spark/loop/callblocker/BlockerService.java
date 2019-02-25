package spark.loop.callblocker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Method;

import static android.widget.Toast.LENGTH_SHORT;

public class BlockerService extends Service {

    IntentFilter intentFilter;

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String State = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (State.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                RejectCall();
            }


        }


    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(receiver, intentFilter);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(receiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }


    public void RejectCall(){

        try {

            String serviceManagerName,serviceManagerNativeName,telephonyName;

            serviceManagerName = "android.os.ServiceManager";
            serviceManagerNativeName = "android.os.ServiceManagerNative";
            telephonyName = "com.android.internal.telephony.ITelephony";

            Class<?> telephonyClass,telephonyStubClass,serviceManagerClass,serviceManagerNativeClass;
            Object telephonyObject,serviceManagerObject;
            Method telephonyEndCall,tempInterfaceMethod,getService;

            telephonyClass = Class.forName(telephonyName);
            serviceManagerClass = Class.forName(serviceManagerName);
            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
            telephonyStubClass = telephonyClass.getClasses()[0];

            getService =serviceManagerClass.getMethod("getService", String.class);
            tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
            Binder tmpBinder = new Binder();

            tmpBinder.attachInterface(null, "fake");
            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);

            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);

            telephonyObject = serviceMethod.invoke(null, retbinder);
            telephonyEndCall = telephonyClass.getMethod("endCall");
            telephonyEndCall.invoke(telephonyObject);

        } catch (Exception e) {
            e.printStackTrace();


        }
    }

}