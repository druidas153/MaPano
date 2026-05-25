package com.myaplicacion.mapano.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Modelo que representa la respuesta JSON de la API de paradas de taxi de Zaragoza.
 */
public class TaxiApiResponse {

    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("result")
    private List<TaxiApi> result;

    public int getTotalCount() { return totalCount; }
    public List<TaxiApi> getResult() { return result; }

    public static class TaxiApi {

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("geometry")
        private Geometry geometry;

        @SerializedName("streetAddress")
        private String streetAddress;

        @SerializedName("description")
        private String description;

        @SerializedName("numPlazas")
        private int numPlazas;

        // Getters
        public int getId() { return id; }
        public String getTitle() { return title; }
        public Geometry getGeometry() { return geometry; }
        public String getStreetAddress() { return streetAddress; }
        public String getDescription() { return description; }
        public int getNumPlazas() { return numPlazas; }
    }

    /**
     * Coordenadas geográficas del punto.
     * Con srsname=wgs84, las coordenadas vienen en [longitud, latitud].
     */
    public static class Geometry {

        @SerializedName("type")
        private String type;

        @SerializedName("coordinates")
        private List<Double> coordinates;

        public String getType() { return type; }
        public List<Double> getCoordinates() { return coordinates; }

        public double getLongitud() {
            if (coordinates != null && coordinates.size() >= 2) {
                return coordinates.get(0);
            }
            return 0;
        }

        public double getLatitud() {
            if (coordinates != null && coordinates.size() >= 2) {
                return coordinates.get(1);
            }
            return 0;
        }
    }
}
