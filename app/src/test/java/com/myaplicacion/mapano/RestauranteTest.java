package com.myaplicacion.mapano;

import org.junit.Test;

import static org.junit.Assert.*;

import com.myaplicacion.mapano.model.Restaurante;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RestauranteTest {
    //@Test
   /* public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    */

    @Test
    public void crearRestaurante_datosCorrectos() {
        Restaurante restaurante = new Restaurante(
                "La Parrilla", "Restaurante de carnes",
                41.6488, -0.8891, "Calle Mayor 1",
                4, "Española"
        );

        assertEquals("La Parrilla", restaurante.getDatosComunes().getNombre());
        assertEquals(41.6488, restaurante.getDatosComunes().getLatitud(), 0.001);
        assertEquals(-0.8891, restaurante.getDatosComunes().getLongitud(), 0.001);
        assertEquals(4, restaurante.getTenedores());
        assertEquals("Española", restaurante.getTipoCocina());
    }

    @Test
    public void getTenedoresVisual_devuelveEmojisCorrectos() {
        Restaurante restaurante = new Restaurante();
        restaurante.setTenedores(3);

        assertEquals("🍴🍴🍴", restaurante.getTenedoresVisual());
    }

    @Test
    public void getTenedoresVisual_ceroTenedores_devuelveVacio() {
        Restaurante restaurante = new Restaurante();
        restaurante.setTenedores(0);

        assertEquals("", restaurante.getTenedoresVisual());
    }

    @Test
    public void constructorVacio_valoresPorDefecto() {
        Restaurante restaurante = new Restaurante();

        assertNotNull(restaurante.getDatosComunes());
        assertEquals(0, restaurante.getPrioridadMapa());
        assertFalse(restaurante.isAceptaReservas());
    }



}