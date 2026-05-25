package com.myaplicacion.mapano.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Modelo que representa la respuesta JSON de la API de eventos/agenda de Zaragoza.
 */
public class EventoApiResponse {

    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("result")
    private List<EventoApi> result;

    public int getTotalCount() { return totalCount; }
    public List<EventoApi> getResult() { return result; }

    public static class EventoApi {

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("geometry")
        private Geometry geometry;

        @SerializedName("startDate")
        private String startDate;

        @SerializedName("endDate")
        private String endDate;

        @SerializedName("lugar")
        private String lugar;

        @SerializedName("tematica")
        private String tematica;

        @SerializedName("link")
        private String link;

        @SerializedName("streetAddress")
        private String streetAddress;

        // Getters
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Geometry getGeometry() { return geometry; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getLugar() { return lugar; }
        public String getTematica() { return tematica; }
        public String getLink() { return link; }
        public String getStreetAddress() { return streetAddress; }
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

        /**
         * Devuelve la longitud (primer valor en GeoJSON).
         */
        public double getLongitud() {
            if (coordinates != null && coordinates.size() >= 2) {
                return coordinates.get(0);
            }
            return 0;
        }

        /**
         * Devuelve la latitud (segundo valor en GeoJSON).
         */
        public double getLatitud() {
            if (coordinates != null && coordinates.size() >= 2) {
                return coordinates.get(1);
            }
            return 0;
        }
    }
}
