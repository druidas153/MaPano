package com.myaplicacion.mapano.util;

/**
 * Utilidad para convertir coordenadas UTM (zona 30N) a WGS84 (lat/lon).
 * La API de datos abiertos de Zaragoza devuelve coordenadas en UTM zona 30N.
 */
public class CoordenadasUtil {

    /**
     * Convierte coordenadas UTM zona 30N a latitud/longitud WGS84.
     *
     * @param utmX Coordenada X (Easting) en metros
     * @param utmY Coordenada Y (Northing) en metros
     * @return Array de 2 elementos: [latitud, longitud]
     */
    public static double[] utmToLatLon(double utmX, double utmY) {
        // Parámetros del elipsoide WGS84
        double a = 6378137.0;           // Semieje mayor
        double f = 1 / 298.257223563;   // Aplanamiento
        double b = a * (1 - f);         // Semieje menor
        double e2 = (a * a - b * b) / (a * a); // Excentricidad al cuadrado
        double e_prime2 = (a * a - b * b) / (b * b);

        // Parámetros UTM
        double k0 = 0.9996;
        int zone = 30; // Zaragoza está en la zona 30N
        double lonOrigin = (zone - 1) * 6 - 180 + 3; // Meridiano central de la zona

        // Ajustar coordenadas
        double x = utmX - 500000.0; // Quitar el falso easting
        double y = utmY; // En hemisferio norte no se ajusta

        double M = y / k0;
        double mu = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));

        double e1 = (1 - Math.sqrt(1 - e2)) / (1 + Math.sqrt(1 - e2));

        double phi1 = mu + (3 * e1 / 2 - 27 * Math.pow(e1, 3) / 32) * Math.sin(2 * mu)
                + (21 * e1 * e1 / 16 - 55 * Math.pow(e1, 4) / 32) * Math.sin(4 * mu)
                + (151 * Math.pow(e1, 3) / 96) * Math.sin(6 * mu)
                + (1097 * Math.pow(e1, 4) / 512) * Math.sin(8 * mu);

        double N1 = a / Math.sqrt(1 - e2 * Math.sin(phi1) * Math.sin(phi1));
        double T1 = Math.tan(phi1) * Math.tan(phi1);
        double C1 = e_prime2 * Math.cos(phi1) * Math.cos(phi1);
        double R1 = a * (1 - e2) / Math.pow(1 - e2 * Math.sin(phi1) * Math.sin(phi1), 1.5);
        double D = x / (N1 * k0);

        double lat = phi1 - (N1 * Math.tan(phi1) / R1) * (
                D * D / 2
                        - (5 + 3 * T1 + 10 * C1 - 4 * C1 * C1 - 9 * e_prime2) * Math.pow(D, 4) / 24
                        + (61 + 90 * T1 + 298 * C1 + 45 * T1 * T1 - 252 * e_prime2 - 3 * C1 * C1) * Math.pow(D, 6) / 720
        );

        double lon = (D - (1 + 2 * T1 + C1) * Math.pow(D, 3) / 6
                + (5 - 2 * C1 + 28 * T1 - 3 * C1 * C1 + 8 * e_prime2 + 24 * T1 * T1) * Math.pow(D, 5) / 120)
                / Math.cos(phi1);

        // Convertir a grados
        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon) + lonOrigin;

        return new double[]{lat, lon};
    }
}

