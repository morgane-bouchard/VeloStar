package fr.vhb.sio.velostar;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Cette classe décrit une station de VéloStar
 *
 */
public class Station implements Serializable, Comparable<Station> {
	/** Membres privés */
	private int _id;					// id de la station
	private String _nom;				// nom de la station
	private boolean _ouvert;			// indique si la station est ouverte
	private int _nbAttachesDisponibles;	// nombre d'attaches disponibles
	private int _nbVelosDisponibles;	// nombre de v�los disponibles
	private Double _latitude;			// latitude de la station
	private Double _longitude;			// longitude de la station
	private String _etat;				// etat de la station
    private String _distance;              // distance de la station par rapport à la station de base

	/**
	 * Initialise une instance de station
	 * @param id
	 * @param nom
	 * @param ouvert
	 * @param nbAttachesDisponibles
	 * @param nbVelosDisponibles
	 */
	public Station(int id, String nom, boolean ouvert, int nbAttachesDisponibles, int nbVelosDisponibles, Double latitude, Double longitude, String etat)
	{	this._id = id;
		this._nom = nom.trim();
		this._ouvert = ouvert;
		this._nbAttachesDisponibles = nbAttachesDisponibles;
		this._nbVelosDisponibles = nbVelosDisponibles;
		this._latitude = latitude;
		this._longitude = longitude;
		this._etat = etat;
        this._distance = "";
	}
    public Station(int id, String nom, boolean ouvert, int nbAttachesDisponibles, int nbVelosDisponibles, Double latitude, Double longitude, String etat, String distance)
    {	this._id = id;
        this._nom = nom.trim();
        this._ouvert = ouvert;
        this._nbAttachesDisponibles = nbAttachesDisponibles;
        this._nbVelosDisponibles = nbVelosDisponibles;
        this._latitude = latitude;
        this._longitude = longitude;
        this._etat = etat;
        this._distance = distance;
    }
	
	/** Accesseurs */
	public int getId()
	{	return this._id;
	}
	public String getNom() {
		return _nom;
	}	
	public boolean isOuvert()
	{	return this._ouvert;
	}
	public int getNbAttachesDisponibles() {
		return _nbAttachesDisponibles;
	}
	public int getNbVelosDisponibles() {
		return _nbVelosDisponibles;
	}
	public Double getLatitude() {
		return _latitude;
	}
	public Double getLongitude() {
		return _longitude;
	}
	public String getEtat(){
		return _etat;
	}
    public String getDistance(){
        return _distance;
    }
	/**
	 * Fournit la représentation textuelle d'une station
	 * @return représentation textuelle
	 */
	public String toString() {
		return  this.getNom()  + ":" + getNbAttachesDisponibles() + ":" + this.getNbVelosDisponibles();
	}

	@Override
	public int compareTo(@NonNull Station s) {
		int i = Double.parseDouble(this.getDistance())>Double.parseDouble(s.getDistance())?1:Double.parseDouble(this.getDistance())<Double.parseDouble(s.getDistance())?-1:0;
		return i;
	}
}
