package br.edu.buscgsender.location;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.Toast;
import br.edu.buscgsender.R;
import br.edu.buscgsender.webservice.ClienteRest;
import br.edu.pojos.Onibus;

/**
 * Classe principal do projeto de TCC.
 * 
 * Ela contém o objeto LocationManager que como o próprio nome diz, ela gerencia localização, e os provedores de localização
 * coordenada geográfica como GPS.
 * 
 * A implementação da interface LocationListenter é primordial, pois é ela que fica verificando de tempo em tempo, 
 * se a localizacão foi alterada.
 * 
 * @author tgmarinho
 */

public class OnibusLocation extends Activity implements LocationListener {

	private LocationManager locationManager;
	Bundle params;
	Intent it;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		it = getIntent();
		params =  it.getExtras();
		
		System.err.println("nome do onibus: " + params.getString("nome_onibus"));
		System.err.println("id do onibus: " + params.getInt("id_onibus"));
	}

	@Override
	protected void onResume() {
		Toast.makeText(this, "app resumed", Toast.LENGTH_SHORT).show();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0f, this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		Toast.makeText(this, "app paused", Toast.LENGTH_SHORT).show();
		// turn off to save battery
		locationManager.removeUpdates(this);
		super.onPause();
	}

	@Override
	public void finish() {
		// save some information before exit
		Toast.makeText(this, "activity killed", Toast.LENGTH_SHORT).show();
		super.finish();
	}

	@Override
	protected void onStop() {
		// save some information before exit
		Toast.makeText(this, "app stoped", Toast.LENGTH_SHORT).show();
		super.onStop();
	}

	@Override
	public void onLocationChanged(Location location) {
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		double altitude = location.getAltitude();
		float accurancy = location.getAccuracy();
		long time = location.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		String tempo = sdf.format(c.getTime());
		String s = "Location Changed: longitude[" + longitude + "] \nlatitude["
				+ latitude + "] \naltitude[" + altitude + "] \naccurancy["
				+ accurancy + "] \ntime[" + tempo + "]";

		// Chamo uma clases qye vau mintar o a katutyde e lingtude e evniar para
		// oservidor

		// Pego o codigo do onibus para passar para webservice
		ClienteRest cliREST = new ClienteRest();
		Onibus o = new Onibus();
		o.setId(params.getInt("id_onibus"));
		o.setNome(params.getString("nome_onibus"));
		o.setLatitude(latitude);
		o.setLongitude(longitude);

		try {
			String resposta = cliREST.atualizar(o);
			gerarToast(resposta);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	private void gerarToast(String resposta) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(getApplicationContext(), "o que ocorreu: "
				+ resposta, duration);
		toast.show();
	}
	
	

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Provider is disable", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Provider is enable", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Toast.makeText(this, "Status Changed: Out of Service",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Toast.makeText(this, "Status Changed: Temporarily Unavailable",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.AVAILABLE:
			Toast.makeText(this, "Status Changed: Available",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}


}
