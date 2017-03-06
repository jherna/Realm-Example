package cat.exemple.catfilms.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Film extends RealmObject {
	@Ignore	private int sessionId;

	@PrimaryKey	private String id;
	@Index private String versio;
	private String prioritat, titol, situacio, any, cartell, original, direccio, interprets,
					sinopsi, qualificacio, trailer, web, estrena;
	//private final String RES = "--";

	public Film(String id, String prioritat, String titol, String situacio,
			String any, String cartell, String original, String direccio,
			String interprets, String sinopsi, String versio,
			String qualificacio, String trailer, String web, String estrena) {
		
		this.id = id;
		this.prioritat = prioritat;
		this.titol = titol;
		this.situacio = situacio;
		this.any = any;
		this.cartell = cartell;
		this.original = original;
		this.direccio = direccio;
		this.interprets = interprets;
		this.sinopsi = sinopsi;
		this.versio = versio;
		this.qualificacio = qualificacio;
		this.trailer = trailer;
		this.web = web;
		this.estrena = estrena;
	}

	public Film() {
	/*	this.id = RES;
		this.prioritat = RES;
		this.titol = RES;
		this.situacio = RES;
		this.any = RES;
		this.cartell = RES;
		this.original = RES;
		this.direccio = RES;
		this.interprets = RES;
		this.sinopsi = RES;
		this.versio = RES;
		this.qualificacio = RES;
		this.trailer = RES;
		this.web = RES;
		this.estrena = RES;*/
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrioritat() {
		return prioritat;
	}

	public void setPrioritat(String prioritat) {
		this.prioritat = prioritat;
	}

	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public String getSituacio() {
		return situacio;
	}

	public void setSituacio(String situacio) {
		this.situacio = situacio;
	}

	public String getAny() {
		return any;
	}

	public void setAny(String any) {
		this.any = any;
	}

	public String getCartell() {
		return cartell;
	}

	public void setCartell(String cartell) {
		this.cartell = cartell;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getDireccio() {
		return direccio;
	}

	public void setDireccio(String direccio) {
		this.direccio = direccio;
	}

	public String getInterprets() {
		return interprets;
	}

	public void setInterprets(String interprets) {
		this.interprets = interprets;
	}

	public String getSinopsi() {
		return sinopsi;
	}

	public void setSinopsi(String sinopsi) {
		this.sinopsi = sinopsi;
	}

	public String getVersio() {
		return versio;
	}

	public void setVersio(String versio) {
		this.versio = versio;
	}

	public String getQualificacio() {
		return qualificacio;
	}

	public void setQualificacio(String qualificacio) {
		this.qualificacio = qualificacio;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getEstrena() {
		return estrena;
	}

	public void setEstrena(String estrena) {
		this.estrena = estrena;
	}

	@Override
	public String toString() {
		return "Film{" +
				"titol='" + titol + '\'' +
				", any='" + any + '\'' +
				", direccio='" + direccio + '\'' +
				'}';
	}
}
