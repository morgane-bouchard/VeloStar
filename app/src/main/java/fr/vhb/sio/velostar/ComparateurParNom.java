package fr.vhb.sio.velostar;

/**
 * Created by mbouchard on 17/04/2018.
 */
import java.util.Comparator;

public class ComparateurParNom implements Comparator<Station>{
    public int compare(Station eg, Station ed) {
        return eg.getNom().compareTo(ed.getNom());
    }
}
