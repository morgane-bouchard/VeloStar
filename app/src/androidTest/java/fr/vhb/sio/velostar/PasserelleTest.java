package fr.vhb.sio.velostar;

import junit.framework.TestCase;

import java.util.ArrayList;

public class PasserelleTest extends TestCase {

	public void testGetLesStations() throws Exception {
		ArrayList<Station> lesStations;
		lesStations = Passerelle.getLesStations();
		assertNotNull(lesStations);
		assertEquals(83, lesStations.size());

		assertEquals("République", lesStations.get(0).getNom());
		assertEquals(1, lesStations.get(0).getId());
		assertEquals(true, lesStations.get(0).isOuvert());
	}
}
