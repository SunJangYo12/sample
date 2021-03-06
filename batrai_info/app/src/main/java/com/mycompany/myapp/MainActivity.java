package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.util.*;
import java.text.*;

public class MainActivity extends Activity
{

    private final String TAG = "SDP_BATTERY";
    private final String DEGREE_UNICODE = "\u00B0";

    private StringBuffer textBuffer = new StringBuffer();

    private TextView mStatusTextView;
    private TextView mBatteryStatusIcon;
	public static StringBuffer gh;

 
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBatteryStatusIcon = (TextView) this.findViewById(R.id.text1);
        mStatusTextView = (TextView) this.findViewById(R.id.text2);
    }

    /**
     * Once onResume is called, the activity has become visible (it is now "resumed"). Comes after onCreate
     */
    protected void onResume()
	{
        super.onResume();

        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);
    }

    /**
     * Another activity takes focus, so this activity goes to "paused" state
     */
    protected void onPause() 
	{
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * BroadcastReceiver is used for receiving intents (broadcasted messages) from the BatteryManager
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        private boolean isHealth = false;

        public void onReceive(Context context, Intent intent)
		{
            DecimalFormat formatter = new DecimalFormat();

            String action = intent.getAction();

            // store battery information received from BatteryManager
            if (action.equals(Intent.ACTION_BATTERY_CHANGED))
			{
            
				int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
				
                boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
				
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
                int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

                // display the battery icon that fits the current battery status (charging/discharging)
                mBatteryStatusIcon.setCompoundDrawablesWithIntrinsicBounds(icon_small, 0, 0, 0);

                // create TextView of the remaining information , to display to screen.
                String statusString = "";

                switch (status)
				{
					case BatteryManager.BATTERY_STATUS_UNKNOWN:
						statusString = "unknown";
						break;
					case BatteryManager.BATTERY_STATUS_CHARGING:
						statusString = "charging";
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						statusString = "discharging";
						break;
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						statusString = "not charging";
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						statusString = "full";
						break;
                }

                String healthString = "";

                switch (health) 
				{
					case BatteryManager.BATTERY_HEALTH_UNKNOWN:
						healthString = "unknown";
						break;
					case BatteryManager.BATTERY_HEALTH_GOOD:
						healthString = "good";
						isHealth = true;
						break;
					case BatteryManager.BATTERY_HEALTH_OVERHEAT:
						healthString = "overheat";
						break;
					case BatteryManager.BATTERY_HEALTH_DEAD:
						healthString = "dead";
						break;
					case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
						healthString = "over voltage";
						break;
					case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
						healthString = "unspecified failure";
						break;
                }

                String acString = "";

                switch (plugged) 
				{
					case BatteryManager.BATTERY_PLUGGED_AC:
						acString = "plugged AC";
						break;
					case BatteryManager.BATTERY_PLUGGED_USB:
						acString = "plugged USB";
						break;
					default:
						acString = "not plugged";
                }

                textBuffer =  new StringBuffer();
                textBuffer.append("status:" + statusString + "\n");

                formatter.applyPattern("#");
                String levelStr = formatter.format( (float)level/scale * 100 );
                textBuffer.append("level:" + levelStr + "% (out of 100)\n");
                textBuffer.append("health:" + healthString + "\n");

                textBuffer.append("present?:" + String.valueOf(present) + "\n");

                textBuffer.append("plugged?:" + acString + "\n");

                // voltage is reported in millivolts
                formatter.applyPattern(".##");
                String voltageStr = formatter.format( (float)voltage/1000 );
                textBuffer.append("voltage:" + voltageStr + "V\n");

                // temperature is reported in tenths of a degree Centigrade (from BatteryService.java)
                formatter.applyPattern(".#");
                String temperatureStr = formatter.format( (float)temperature/10 );
                textBuffer.append("temperature:" + temperatureStr  + "C" + DEGREE_UNICODE + "\n");

                textBuffer.append("technology:" + String.valueOf(technology) + "\n");

                
				gh = textBuffer;
          
				mStatusTextView.setText(gh);
				
            }
        }
    };
}
