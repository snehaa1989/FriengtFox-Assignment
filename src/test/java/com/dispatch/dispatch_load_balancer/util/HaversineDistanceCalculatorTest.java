
package com.dispatch.dispatch_load_balancer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineDistanceCalculatorTest {

    @Test
    void sameCoordinatesShouldReturnZero() {
        double distance =
                HaversineDistanceCalculator.calculateDistance(
                        28.6139, 77.2090,
                        28.6139, 77.2090
                );

        assertEquals(0.0, distance, 0.000001);
    }

    @Test
    void distanceShouldBePositiveForDifferentCoordinates() {
        double distance =
                HaversineDistanceCalculator.calculateDistance(
                        28.6139, 77.2090,
                        28.5355, 77.3910
                );

        assertTrue(distance > 0);
    }

    @Test
    void distanceShouldBeSymmetric() {
        double forward =
                HaversineDistanceCalculator.calculateDistance(
                        28.6139, 77.2090,
                        28.5355, 77.3910
                );

        double backward =
                HaversineDistanceCalculator.calculateDistance(
                        28.5355, 77.3910,
                        28.6139, 77.2090
                );

        assertEquals(forward, backward, 0.000001);
    }
}
