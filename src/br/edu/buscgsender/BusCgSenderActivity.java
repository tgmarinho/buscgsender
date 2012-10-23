package br.edu.buscgsender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import br.edu.buscgsender.location.OnibusLocation;

/**
 * Classe principal, inicia o fluxo do aplicativo
 * Monta na tela o spinner e trata o evento de click do botão redirecionando para outras atividades
 * @author tgmarinho
 */

public class BusCgSenderActivity extends Activity {

	private int posicaoSpinner;
	private String nomeBusao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscgsender);

		
		// Mensagem de Alerta
		Toast.makeText(this, "MOTORISTA SELECIONE A LINHA DE ÔNIBUS QUE ESTARÁ DIRIGINDO...", Toast.LENGTH_LONG).show();
		
		
		Spinner combo = (Spinner) findViewById(R.id.comboLinhaOnibus);
		final Button btn_enviar = (Button) findViewById(R.id.btn_enviar);

		@SuppressWarnings("rawtypes")
		ArrayAdapter adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, todosOnibus());
		adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		combo.setAdapter(adaptador);

		// Se selecionar algum planeta atualiza a imagem
		combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
			
				posicao++; // para nao ter o Zero
				setPosicaoSpinner(posicao);
				setNomeBusao((String) parent.getItemAtPosition(posicao-1));
				System.out.println(getNomeBusao());
				System.out.println(getPosicaoSpinner());
				btn_enviar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent it = new Intent(v.getContext(), OnibusLocation.class);
						Bundle params = new Bundle();
						params.putInt("id_onibus", getPosicaoSpinner());
						params.putString("nome_onibus", getNomeBusao());
						it.putExtras(params);
						startActivity(it);

					}
				});

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}

			
		});
	}

	private String[] todosOnibus() {

		String[] bus = new String[] { "Arnaldo Estevão Figueredo", "Maria Ap Pedrossian - Oiti", "070", "Tiradentes" };

		return bus;
	}
	

	public int getPosicaoSpinner() {
		return posicaoSpinner;
	}

	public void setPosicaoSpinner(int posicaoSpinner) {
		this.posicaoSpinner = posicaoSpinner;
	}

	public String getNomeBusao() {
		return nomeBusao;
	}

	public void setNomeBusao(String nomeBusao) {
		this.nomeBusao = nomeBusao;
	}

}
