package com.myaplicacion.mapano.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Modelo que representa la respuesta JSON de la API de restaurantes de Zaragoza.
 */
public class RestauranteApiResponse {

    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("result")
    private List<RestauranteApi> result;

    public int getTotalCount() {
        return totalCount;
    }

    public List<RestauranteApi> getResult() {
        return result;
    }

    /**
     * Cada restaurante que viene de la API.
     */
    public static class RestauranteApi {

        @SerializedName("id")
        private String id;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("geometry")
        private Geometry geometry;

        @SerializedName("address")
        private String address;

        @SerializedName("tenedores")
        private String tenedores;
        @SerializedName("phone")
        private String phone;

        @SerializedName("link")
        private String link;




        // Getters

        public String getTenedores() {return tenedores;}

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Geometry getGeometry() { return geometry; }
        public String getAddress() { return address; }
        public String getPhone() { return phone; }
        public String getLink() { return link; }
    }

    /**
     * Coordenadas geográficas del punto.
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
