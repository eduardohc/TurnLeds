package com.example.turnonleds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	public void lanzarLeds(View v){
		Intent turn = new Intent(this, turnLeds.class);
		startActivity(turn);
	}
	
	public void lanzarBluetooth(View v){
		Intent conection = new Intent(this, bluetoothConection.class);
		startActivity(conection);
	}
	
	public void lanzarSalir(View v){
		Intent exit = new Intent(this, Salir.class);
		finish();
	}
	
}
