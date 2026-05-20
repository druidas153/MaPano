package com.myaplicacion.mapano.database;

import android.content.Context;

import com.myaplicacion.mapano.model.DatosComunes;
import com.myaplicacion.mapano.model.Evento;
import com.myaplicacion.mapano.model.Farmacia;
import com.myaplicacion.mapano.model.ParadaTaxi;
import com.myaplicacion.mapano.model.Restaurante;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase para insertar datos de prueba (mock) en la base de datos.
 * Usa coordenadas reales de Zaragoza.
 * En el futuro, estos datos vendrán de las APIs de datos abiertos.
 */
public class DatabaseSeeder {

    private final AppDatabase database;
    private final ExecutorService executor;

    public DatabaseSeeder(Context context) {
        this.database = AppDatabase.getInstance(context);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserta todos los datos mock si la base de datos está vacía.
     */
    public void seedIfEmpty() {
        executor.execute(() -> {
            // Solo inserta si no hay restaurantes (primera vez)
            if (database.restauranteDao().contarRestaurantes() == 0)
                //if (database.restauranteDao().obtenerTodos().getValue() == null)
            {
                seedRestaurantes();
                seedEventos();
                seedFarmacias();
                seedParadasTaxi();
            }
        });
    }

    /**
     * Fuerza la inserción de datos mock (borra los existentes).
     */
    public void forceReseed() {
        executor.execute(() -> {
            database.restauranteDao().eliminarTodos();
            database.eventoDao().eliminarTodos();
            database.farmaciaDao().eliminarTodas();
            database.paradaTaxiDao().eliminarTodas();

            seedRestaurantes();
            seedEventos();
            seedFarmacias();
            seedParadasTaxi();
        });
    }

    // ========================
    // RESTAURANTES MOCK
    // ========================

    private void seedRestaurantes() {
        List<Restaurante> restaurantes = new ArrayList<>();

        Restaurante r1 = new Restaurante(
                "La Miguería", "Bocadillos gourmet y tapas creativas",
                41.6516, -0.8847, "Calle Méndez Núñez, 38",
                3, "Española"
        );
        r1.setTelefono("976 123 456");
        r1.setHorario("12:00 - 23:00");
        restaurantes.add(r1);

        Restaurante r2 = new Restaurante(
                "Restaurante La Bastilla", "Cocina francesa con toques aragoneses",
                41.6492, -0.8899, "Calle Coso, 177",
                4, "Francesa"
        );
        r2.setTelefono("976 234 567");
        r2.setHorario("13:00 - 16:00, 20:00 - 23:30");
        restaurantes.add(r2);

        Restaurante r3 = new Restaurante(
                "Sakura Sushi", "Sushi fresco y ramen artesanal",
                41.6530, -0.8790, "Paseo de la Independencia, 22",
                3, "Japonesa"
        );
        r3.setTelefono("976 345 678");
        r3.setHorario("12:30 - 16:00, 19:30 - 23:00");
        restaurantes.add(r3);

        Restaurante r4 = new Restaurante(
                "Trattoria Bella Napoli", "Pizza napolitana al horno de leña",
                41.6561, -0.8765, "Calle San Miguel, 10",
                2, "Italiana"
        );
        r4.setTelefono("976 456 789");
        r4.setHorario("13:00 - 00:00");
        restaurantes.add(r4);

        Restaurante r5 = new Restaurante(
                "El Cachirulo", "Cocina tradicional aragonesa",
                41.6488, -0.8891, "Plaza del Pilar, 5",
                5, "Aragonesa"
        );
        r5.setTelefono("976 567 890");
        r5.setHorario("13:00 - 16:00, 20:30 - 23:30");
        r5.getDatosComunes().setEsPremium(true);
        r5.setPrioridadMapa(8);
        r5.setMensajePromo("¡Menú degustación aragonés por 35€!");
        r5.setAceptaReservas(true);
        r5.setUrlReserva("https://elcachirulo.com/reservas");
        restaurantes.add(r5);

        database.restauranteDao().insertarTodos(restaurantes);
    }

    // ========================
    // EVENTOS MOCK
    // ========================

    private void seedEventos() {
        List<Evento> eventos = new ArrayList<>();

        Evento e1 = new Evento(
                "Fiestas del Pilar 2026", "Las fiestas más importantes de Zaragoza",
                41.6575, -0.8789, "Plaza del Pilar",
                "2026-10-12", "10:00", "fiesta"
        );
        e1.setFechaFin("2026-10-20");
        e1.setEsGratuito(true);
        e1.setOrganizador("Ayuntamiento de Zaragoza");
        eventos.add(e1);

        Evento e2 = new Evento(
                "Concierto Amaral", "Gira 2026 - Salto al Color",
                41.6423, -0.8980, "Sala Multiusos del Auditorio",
                "2026-06-15", "21:00", "concierto"
        );
        e2.setPrecio(35.0);
        e2.setEsGratuito(false);
        e2.setUrlEntradas("https://entradas.com/amaral");
        e2.setOrganizador("Live Nation");
        eventos.add(e2);

        Evento e3 = new Evento(
                "Mercado Medieval", "Recreación histórica con puestos artesanales",
                41.6555, -0.8760, "Plaza San Bruno",
                "2026-05-20", "11:00", "mercado"
        );
        e3.setFechaFin("2026-05-22");
        e3.setHoraFin("22:00");
        e3.setEsGratuito(true);
        e3.setOrganizador("Asociación Cultural Medieval");
        eventos.add(e3);

        Evento e4 = new Evento(
                "Maratón de Zaragoza", "42km por las calles de Zaragoza",
                41.6490, -0.8870, "Parque Grande José Antonio Labordeta",
                "2026-03-15", "08:30", "deporte"
        );
        e4.setPrecio(25.0);
        e4.setEsGratuito(false);
        e4.setUrlEntradas("https://maratonzaragoza.com");
        e4.setOrganizador("Federación Aragonesa de Atletismo");
        eventos.add(e4);

        Evento e5 = new Evento(
                "Teatro: La Casa de Bernarda Alba", "Clásico de Federico García Lorca",
                41.6500, -0.8830, "Teatro Principal",
                "2026-04-10", "20:00", "teatro"
        );
        e5.setPrecio(18.0);
        e5.setEsGratuito(false);
        e5.setUrlEntradas("https://teatroprincipal.com");
        e5.setOrganizador("Compañía Nacional de Teatro");
        e5.getDatosComunes().setEsPremium(true);
        e5.setEsPatrocinado(true);
        e5.setPrioridadMapa(7);
        e5.setMensajePromo("¡Últimas entradas disponibles!");
        eventos.add(e5);

        database.eventoDao().insertarTodos(eventos);
    }

    // ========================
    // FARMACIAS MOCK
    // ========================

    private void seedFarmacias() {
        List<Farmacia> farmacias = new ArrayList<>();

        Farmacia f1 = new Farmacia(
                "Farmacia Central", "Farmacia 24h en el centro",
                41.6495, -0.8885, "Calle Alfonso I, 15",
                true, true
        );
        f1.setTelefono("976 111 222");
        f1.setTitular("María García López");
        f1.setHorarioApertura("00:00");
        f1.setHorarioCierre("23:59");
        farmacias.add(f1);

        Farmacia f2 = new Farmacia(
                "Farmacia Plaza España", "Amplio surtido de parafarmacia",
                41.6520, -0.8830, "Plaza España, 3",
                true, false
        );
        f2.setTelefono("976 222 333");
        f2.setTitular("José Martínez Ruiz");
        f2.setHorarioApertura("09:00");
        f2.setHorarioCierre("21:30");
        farmacias.add(f2);

        Farmacia f3 = new Farmacia(
                "Farmacia Delicias", "Especialidad en homeopatía",
                41.6440, -0.9050, "Avenida Madrid, 52",
                false, false
        );
        f3.setTelefono("976 333 444");
        f3.setTitular("Ana Pérez Sánchez");
        f3.setHorarioApertura("09:30");
        f3.setHorarioCierre("20:00");
        farmacias.add(f3);

        Farmacia f4 = new Farmacia(
                "Farmacia Actur", "Farmacia de guardia nocturna",
                41.6680, -0.8890, "Calle María Zambrano, 8",
                true, true
        );
        f4.setTelefono("976 444 555");
        f4.setTitular("Carlos Fernández Díaz");
        f4.setHorarioApertura("00:00");
        f4.setHorarioCierre("23:59");
        farmacias.add(f4);

        Farmacia f5 = new Farmacia(
                "Farmacia San José", "Cerca del Hospital Miguel Servet",
                41.6380, -0.8780, "Paseo Isabel la Católica, 3",
                true, false
        );
        f5.setTelefono("976 555 666");
        f5.setTitular("Laura Gómez Torres");
        f5.setHorarioApertura("08:30");
        f5.setHorarioCierre("21:00");
        farmacias.add(f5);

        database.farmaciaDao().insertarTodas(farmacias);
    }

    // ========================
    // PARADAS DE TAXI MOCK
    // ========================

    private void seedParadasTaxi() {
        List<ParadaTaxi> paradas = new ArrayList<>();

        ParadaTaxi t1 = new ParadaTaxi(
                "Parada Estación Delicias", "Junto a la estación AVE",
                41.6590, -0.9120, "Avenida Navarra, s/n",
                8, 15
        );
        t1.setTieneAdaptados(true);
        paradas.add(t1);

        ParadaTaxi t2 = new ParadaTaxi(
                "Parada Plaza España", "Centro de la ciudad",
                41.6518, -0.8835, "Plaza España",
                3, 8
        );
        t2.setTieneAdaptados(false);
        paradas.add(t2);

        ParadaTaxi t3 = new ParadaTaxi(
                "Parada Plaza del Pilar", "Zona turística",
                41.6575, -0.8789, "Plaza del Pilar",
                5, 10
        );
        t3.setTieneAdaptados(true);
        paradas.add(t3);

        ParadaTaxi t4 = new ParadaTaxi(
                "Parada Hospital Miguel Servet", "Urgencias",
                41.6370, -0.8770, "Paseo Isabel la Católica, 1",
                2, 6
        );
        t4.setTieneAdaptados(true);
        paradas.add(t4);

        ParadaTaxi t5 = new ParadaTaxi(
                "Parada Centro Comercial Gran Casa", "Zona comercial",
                41.6650, -0.8940, "Avenida María Zambrano, s/n",
                0, 5
        );
        t5.setTieneAdaptados(false);
        paradas.add(t5);

        database.paradaTaxiDao().insertarTodas(paradas);
    }
}

