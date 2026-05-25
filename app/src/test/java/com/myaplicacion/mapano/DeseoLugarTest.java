package com.myaplicacion.mapano;

import org.junit.Test;
import static org.junit.Assert.*;

import com.myaplicacion.mapano.model.DeseoLugar;


public class DeseoLugarTest {

    @Test
    public void crearDeseo_datosCorrectos() {
        DeseoLugar deseo = new DeseoLugar(
                123, "restaurante", "Restaurante Test",
                41.6488, -0.8891
        );

        assertEquals("Restaurante Test", deseo.getNombreLugar());//nombreLugar
        assertEquals(123, deseo.getIdPuntoInteres());
        assertEquals("restaurante", deseo.getCategoria());
        assertEquals(41.6488, deseo.getLatitud(), 0.001);
        assertEquals(-0.8891, deseo.getLongitud(), 0.001);
    }

    @Test
    public void deseo_noVisitadoPorDefecto() {
        DeseoLugar deseo = new DeseoLugar(
                1, "evento", "Test", 41.0, -0.8
        );

        assertFalse(deseo.isVisitado());
    }

    @Test
    public void marcarComoVisitado_cambiaEstado() {
        DeseoLugar deseo = new DeseoLugar(
                1, "farmacia", "Test", 41.0, -0.8
        );

        deseo.setVisitado(true);
        assertTrue(deseo.isVisitado());
    }
}
