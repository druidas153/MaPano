package com.myaplicacion.mapano;
import static org.junit.Assert.*;

import com.myaplicacion.mapano.model.DatosComunes;

import org.junit.Test;

/**
 * Pruebas unitarias para la clase DatosComunes.
 */

public class DatosComunesTest {

    @Test
    public void crearDatosComunes_todosLosCampos() {
        DatosComunes datos = new DatosComunes(
                "Test Lugar", "Descripción test",
                41.6488, -0.8891, "Calle Test 123"
        );

        assertEquals("Test Lugar", datos.getNombre());
        assertEquals("Descripción test", datos.getDescripcion());
        assertEquals(41.6488, datos.getLatitud(), 0.001);
        assertEquals(-0.8891, datos.getLongitud(), 0.001);
        assertEquals("Calle Test 123", datos.getDireccion());
    }

    @Test
    public void setOrigenDatos_guardaCorrectamente() {
        DatosComunes datos = new DatosComunes();
        datos.setOrigenDatos("API_ZARAGOZA");

        assertEquals("API_ZARAGOZA", datos.getOrigenDatos());
    }

    @Test
    public void coordenadasZaragoza_sonValidas() {
        DatosComunes datos = new DatosComunes(
                "Plaza del Pilar", "",
                41.6560, -0.8773, "Plaza del Pilar"
        );

        // Verificar que las coordenadas están en el rango de Zaragoza
        assertTrue(datos.getLatitud() > 41.0 && datos.getLatitud() < 42.0);
        assertTrue(datos.getLongitud() > -1.5 && datos.getLongitud() < 0.0);
    }
}
