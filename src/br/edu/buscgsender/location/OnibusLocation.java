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
		Toast.makeText(this, "Aplicação iniciada", Toast.LENGTH_SHORT).show();
		setContentView(R.layout.main);
		// Obtendo a referencia do SO para aplicação trabalhar com a o sistema de localização
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		it = getIntent();
		params =  it.getExtras();
		
		android.util.Log.i("BUSCG", "nome do onibus: " + params.getString("nome_onibus"));
		android.util.Log.i("BUSCG", "id do onibus: " + params.getInt("id_onibus"));
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "Aplicação em execução", Toast.LENGTH_SHORT).show();
		// GPS é o fornecedor de localização, 0 segundos, 0 metros, contexto
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,	0, this);
	}

	@Override
	protected void onPause() {
		Toast.makeText(this, "Aplicação pausada", Toast.LENGTH_SHORT).show();
		// Para de obter a localização, econmiza a bateria do smartphone
		locationManager.removeUpdates(this);
		super.onPause();
	}

	@Override
	public void finish() {
		Toast.makeText(this, "Aplicação terminada", Toast.LENGTH_SHORT).show();
		super.finish();
	}

	@Override
	protected void onStop() {
		Toast.makeText(this, "Aplicação parada", Toast.LENGTH_SHORT).show();
		super.onStop();
	}
	
	// Toda vez que a localização é atualizada esse método é chamado automaticamente pelo sistema
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
			if(resposta!=null){
				gerarToast(resposta);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	private void gerarToast(String resposta) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(getApplicationContext(), "o que ocorreu: " + resposta, duration);
		toast.show();
	}
	

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "GPS está desligado", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "GPS está ligado", Toast.LENGTH_SHORT).show();
	}

	
	// toda vez que ocorre alguma mudança de status no provedor de localização esse método é chamado
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Toast.makeText(this, "Status Modificado: Sem serviço",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Toast.makeText(this, "Status Modificado: Temporariamente indisponível",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.AVAILABLE:
			Toast.makeText(this, "Status Modificado: Disponível",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}
}