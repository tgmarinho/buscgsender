package br.edu.buscgsender.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import br.com.livro.android.Ponto;
import br.com.livro.android.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;


public class OnibusLocation extends MapActivity implements LocationListener {
	private MapController map_controller;
	private MapView mapview;
	private MyLocationOverlay posicaoAtual;
	
	// Ciclo de vida da acitivity começa
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// Exibe o arquivo laytoute main.xml
		setContentView(R.layout.main);
		// Faz um binding (ligação com arquivo) com o componente MapView declarado do arquivo main.xml
		mapview = (MapView) findViewById(R.id.mapa);
		// A referencia map_controller assume o controle do mapa
		map_controller = mapview.getController();
		// com o controle do mapa, adiciona o zoom 16 (para ficar próximo a Terra
		map_controller.setZoom(16);
		// Adicciona o componente para controlar o zoom por toque na tela
		mapview.setBuiltInZoomControls(true);  
		
		// Centraliza o mapa na última localizão conhecida
		Location loc = getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// Se existe ultima localização converte para GeoPoint
		if (loc != null) {
			// Utiliza a classe Ponto que sabe fazer a conversão dos pontos geográficos (ver listagem 2)
			GeoPoint ponto_ultimalocalizacao = new Ponto(loc.getLatitude(), loc.getLatitude());
			map_controller.setCenter(ponto_ultimalocalizacao);
		}
		// Instancia a minha localização na referencia posicaoAtual, informando o contexto e o componente MapView
		posicaoAtual = new MyLocationOverlay(this, mapview);
		posicaoAtual.runOnFirstFix(new Runnable() {
			// Melhora a precisão da localização
			@Override
			public void run() {	}
		});
		// Adiciona no mapa a posição atual
		mapview.getOverlays().add(posicaoAtual);
		// Sempre que a localização alterar, chamar essa implementação
		getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	private LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Registra o listener
		posicaoAtual.enableMyLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Remove o listener
		posicaoAtual.disableMyLocation();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Remove o listener para não ficar atualizando mesmo depois de sair
		getLocationManager().removeUpdates(this);
	}

	public void onLocationChanged(Location location) {
		GeoPoint point = new Ponto(location.getLatitude(), location.getLongitude());
		map_controller.animateTo(point);
		mapview.invalidate();
	}
	// Como não estamos exibindo rota nesse exemplo, o returno pode ser false, mas caso contrário o retorno DEVE ser verdadeiro
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	public void onProviderDisabled(String provider) { }
	public void onProviderEnabled(String provider) { }
	public void onStatusChanged(String provider, int status, Bundle extras) { }
}
