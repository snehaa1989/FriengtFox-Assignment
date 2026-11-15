
package com.dispatch.dispatch_load_balancer.util;

public final class HaversineDistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private HaversineDistanceCalculator() {
        // Utility class
    }

    public static double calculateDistance(
            double latitude1,
            double longitude1,
            double latitude2,
            double longitude2) {

        double lat1 = Math.toRadians(latitude1);
        double lon1 = Math.toRadians(longitude1);
        double lat2 = Math.toRadians(latitude2);
        double lon2 = Math.toRadians(longitude2);

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(deltaLon / 2), 2);

        // Protect against floating-point rounding errors.
        a = Math.max(0.0, Math.min(1.0, a));

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return EARTH_RADIUS_KM * c;
    }
}
