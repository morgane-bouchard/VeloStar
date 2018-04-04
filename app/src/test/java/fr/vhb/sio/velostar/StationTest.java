package fr.vhb.sio.velostar;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class StationTest {
    @Test
    public void creerStationTest() {
        Station uneStation;
        uneStation = new Station(1, "Villejean", true, 15, 20, 0.0, 0.0, "");
        assertNotNull(uneStation);
        assertEquals("Villejean", uneStation.getNom());
    }
    @Test
    public void getNbVelosDisponiblesTest() {
        Station uneStation;
        uneStation = new Station(2, "John Kennedy", true, 20, 25,  0.0, 0.0, "");
        assertNotNull(uneStation);
        assertEquals(25, uneStation.getNbVelosDisponibles());
    }
    @Test
    public void getNbAttachesDisponiblesTest() {
        Station uneStation;
        uneStation = new Station(2, "John Kennedy", true, 20, 25,  0.0, 0.0, "");
        assertNotNull(uneStation);
        assertEquals(20, uneStation.getNbAttachesDisponibles());
    }
    @Test
    public void getEstOuvert() {
        Station uneStation;
        uneStation = new Station(2, "John Kennedy", true, 20, 25,  0.0, 0.0, "");
        assertNotNull(uneStation);
        assertTrue(uneStation.isOuvert());
    }
}