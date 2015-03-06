package com.example.turnonleds;

//Classes
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class bluetoothConection extends Activity{
	
	//Declaration variables
	private static final int REQUEST_ENABLE_BT = 1;
	private Button bOn, bOff, bList, bFind;
	private TextView text;
	private BluetoothAdapter myAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private ListView devices;
	private ArrayAdapter <String> BTArrayAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conection);
		
		//Instance the bluetoothAdapter
		myAdapter = BluetoothAdapter.getDefaultAdapter();
		if(myAdapter == null){ //Verify if the bluetooth is empty
			bOn.setEnabled(false);
			bOff.setEnabled(false);
			bList.setEnabled(false);
			bFind.setEnabled(false);
			text.setText("Status: not supported");
			
			//Show a message if the device is compatible
			Toast.makeText(getApplicationContext(), 
					"Your device does not support Bluetooth", Toast.LENGTH_LONG).show();
			
		} else{
			text = (TextView) findViewById(R.id.tvStatus);
			
			//Set onclick to buttons
			bOn = (Button) findViewById(R.id.bOn);
			bOn.setOnClickListener( new OnClickListener(){
				
				public void onClick(View v){
					on(v);
				}
			});
			
			bOff = (Button) findViewById(R.id.bOff);
			bOff.setOnClickListener(new OnClickListener(){
				
				public void onClick(View v){
					off(v);
				}
			});
			
			bList = (Button) findViewById(R.id.bPaired);
			bList.setOnClickListener(new OnClickListener(){
				
				public void onClick(View v){
					list(v);
				}
			});
			
			bFind = (Button) findViewById(R.id.bSearch);
			bFind.setOnClickListener(new OnClickListener(){
				
				public void onClick(View v){
					find(v);
				}
			});
			
			devices = (ListView) findViewById(R.id.listDevices);
			
			// create the arrayAdapter that contains the BTDevices, and set it to the ListView
			BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
			devices.setAdapter(BTArrayAdapter);

		}
	}
	
	public void on(View v){
		if(!myAdapter.isEnabled()){
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(intent, REQUEST_ENABLE_BT);
			
			Toast.makeText(getApplicationContext(), "Bluetooth turn on", 
					Toast.LENGTH_LONG).show();
		} else{
			Toast.makeText(getApplicationContext(), "Bluetooth is already on", 
					Toast.LENGTH_LONG).show();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REQUEST_ENABLE_BT){
			if(myAdapter.isEnabled()){
				text.setText("Status: Enabled");
			} else {
				text.setText("Status: Disabled");
			}
		}
	}
	
	public void list(View v){
		//get paired devices
		pairedDevices = myAdapter.getBondedDevices();
		
		//put it's one to the adapter
		for(BluetoothDevice device: pairedDevices)
			BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		
		Toast.makeText(getApplicationContext(), "Show paired devices", 
				Toast.LENGTH_SHORT). show();
	}
	
	final BroadcastReceiver bReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent){
			String action = intent.getAction();
			//When discovers find a decive
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				//Get the bluetoothDevice object from the intent
				BluetoothDevice device = 
						intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//add the name and the MAC adress of the object to the arrayAdapter
				BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				BTArrayAdapter.notifyDataSetChanged();
			}
		}
	};
	
	public void find(View v){
		if(myAdapter.isDiscovering()){
			// the button is pressed when it discovers, so cancel the discovery
			myAdapter.cancelDiscovery();
		} else{
			BTArrayAdapter.clear();
			myAdapter.startDiscovery();
			
			registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		}
	}
	
	public void off(View v){
		myAdapter.disable();
		text.setText("Status: Disconnected");
		
		Toast.makeText(getApplicationContext(), "Bluetooth turn off", 
				Toast.LENGTH_LONG).show();
	}
	
	/*public void OnDestroy(){
		
		super.onDestroy();
		unregisterReceiver(bReceiver);
	}*/
	
}
