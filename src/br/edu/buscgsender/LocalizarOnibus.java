package br.edu.buscgsender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import br.edu.buscgsender.location.OnibusLocation;
import br.edu.buscgsender.webservice.ClienteRest;

public class LocalizarOnibus extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exemplo_spinner);

		final Spinner combo = (Spinner) findViewById(R.id.comboPlanetas);

		ArrayAdapter adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, todosOnibus());
		// adaptador.setDropDownViewResource(android.R.layout.simple_spinner_item);
		adaptador
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		combo.setAdapter(adaptador);

		// Se selecionar algum planeta atualiza a imagem
		combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v, int posicao, long id) {
				
				Intent it = new Intent(v.getContext(), OnibusLocation.class);
                Bundle params = new Bundle();
                params.putInt("id_onibus", posicao);
                it.putExtras(params);
                // inicio a atividade OnibusLocatino que recebe os dados da coordenada geográfica
                startActivity(it);
	
			}

			private void localizarOnibus(int id) {
				ClienteRest cliREST = new ClienteRest();
				try {
					
					
				} catch (Exception e) {
					e.printStackTrace();
					gerarToast(e.getMessage());
				}

			}

			public void onNothingSelected(AdapterView parent) {
			}
		});
	}

	private String[] todosOnibus() {

		String[] bus = new String[] { "Maria Ap Pedrossian - Oiti",
				"Arnaldo Estevão Figueredo", "070", "Tiradentes" };

		return bus;
	}

	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}

}
