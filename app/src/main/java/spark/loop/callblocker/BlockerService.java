package spark.loop.callblocker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import Data.Helper;
import Data.User;
import Database.Holder;
import Database.RejectedCalls;
import Database.State;

public class BlockerService extends Service implements User {
    IntentFilter intentFilter;
    Holder holder;
    State state;
    boolean ischecked = false;
    Date date;
    RejectedCalls rejectedCalls;
    SimpleDateFormat dateFormat;
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String State = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (State.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                if (ischecked) {
                    RejectCall();
                    RejectAll(incomingNumber);

                } else {
                    holder.getData(BlockerService.this, incomingNumber);
                }

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
        holder = new Holder(this);
        state = new State(this);
        rejectedCalls = new RejectedCalls(this);
        dateFormat = new SimpleDateFormat("dd-MMMM-yy\nhh:mm aa");
        intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(receiver, intentFilter);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ischecked = state.isChecked();
                handler.postDelayed(this, 1000);
            }
        }, 1000);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        holder = new Holder(this);
        state = new State(this);
        rejectedCalls = new RejectedCalls(this);
        dateFormat = new SimpleDateFormat("dd-MMMM-yy\nhh:mm aa");
        registerReceiver(receiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void Details(String name, String number, String incomingNumber) {
        if (!number.equals("-1")) {
            if (incomingNumber.contains(number)) {
                RejectCall();
                date = new Date();
                String calldate = dateFormat.format(date);
                rejectedCalls.insertRejected(name, incomingNumber, calldate);

            }

        }

    }

    public void RejectAll(String incomingNumber) {
        date = new Date();
        String calldate = dateFormat.format(date);
        String name = getContactName(incomingNumber, this);
        rejectedCalls.insertRejected(name, incomingNumber, calldate);

    }


    public String getContactName(String incomingNumber, Context context) {
        String ContactHolderName = "Unknown";
        String[] Projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.HAS_PHONE_NUMBER};
        Uri contactUriObject = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
        Cursor cursor = context.getContentResolver().query(contactUriObject, Projection, null, null, null);
        if (cursor.moveToNext()) {
            ContactHolderName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        return ContactHolderName;
    }


    public void RejectCall() {

        try {

            String serviceManagerName, serviceManagerNativeName, telephonyName;

            serviceManagerName = "android.os.ServiceManager";
            serviceManagerNativeName = "android.os.ServiceManagerNative";
            telephonyName = "com.android.internal.telephony.ITelephony";

            Class<?> telephonyClass, telephonyStubClass, serviceManagerClass, serviceManagerNativeClass;
            Object telephonyObject, serviceManagerObject;
            Method telephonyEndCall, tempInterfaceMethod, getService;

            telephonyClass = Class.forName(telephonyName);
            serviceManagerClass = Class.forName(serviceManagerName);
            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
            telephonyStubClass = telephonyClass.getClasses()[0];

            getService = serviceManagerClass.getMethod("getService", String.class);
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