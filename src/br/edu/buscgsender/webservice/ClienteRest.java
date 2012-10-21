/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.buscgsender.webservice;

import br.edu.pojos.Onibus;

import com.google.gson.Gson;

public class ClienteRest {

	// externo
	private static final String URL_WS = "http://tgmarinhotcc.no-ip.info:8089/ServerBus/rest/onibus/";

	// local
	// private static final String URL_WS =
	// "http://192.168.1.103:8089/ServerBus/rest/onibus/";

	public Onibus get(int id) throws Exception {

		String[] resposta = new WebServiceClient().get(URL_WS + id);

		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			Onibus o = gson.fromJson(resposta[1], Onibus.class);
			return o;
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public String inserir(Onibus o) throws Exception {

		Gson gson = new Gson();
		String onibusJSON = gson.toJson(o);
		String[] resposta = new WebServiceClient().post(URL_WS, onibusJSON);
		if (resposta[0].equals("200")) {
			return resposta[1];
		} else {
			throw new Exception(resposta[1]);
		}
	}

	public String atualizar(Onibus o) {

		try {
			if (o != null) {
				Gson gson = new Gson();
				String onibusJSON = gson.toJson(o);
				String[] resposta = new WebServiceClient().put(URL_WS, onibusJSON);
				if (resposta != null) {
					if (resposta[0].equals("200")) {
						System.out.println(resposta[0]);
						System.out.println(resposta[1]);
						System.out.println("SUCESSO ATUALIZEI");
						return resposta[1];
					} else {
						System.out.println("vixi " + resposta);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return null;

	}

	public String deletar(int id) {
		String[] resposta = new WebServiceClient().get(URL_WS + "delete/" + id);
		return resposta[1];
	}
}
