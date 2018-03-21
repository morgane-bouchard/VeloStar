package fr.vhb.sio.velostar;
/**
 * Cette classe décrit une station de VéloStar
 *
 */
public class Station {
	/** Membres privés */
	private int _id;					// id de la station
	private String _nom;				// nom de la station
	private boolean _ouvert;			// indique si la station est ouverte
	private int _nbAttachesDisponibles;	// nombre d'attaches disponibles
	private int _nbVelosDisponibles;	// nombre de v�los disponibles

	/**
	 * Initialise une instance de station
	 * @param id
	 * @param nom
	 * @param ouvert
	 * @param nbAttachesDisponibles
	 * @param nbVelosDisponibles
	 */
	public Station(int id, String nom, boolean ouvert, int nbAttachesDisponibles, int nbVelosDisponibles)
	{	this._id = id;
		this._nom = nom.trim();
		this._ouvert = ouvert;
		this._nbAttachesDisponibles = nbAttachesDisponibles;
		this._nbVelosDisponibles = nbVelosDisponibles;
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
	/**
	 * Fournit la représentation textuelle d'une station
	 * @return représentation textuelle
	 */
	public String toString() {
		return  this.getNom()  + ":" + getNbAttachesDisponibles() + ":" + this.getNbVelosDisponibles();
	}
}
