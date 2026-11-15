
package com.dispatch.dispatch_load_balancer.dto;

import jakarta.validation.constraints.*;

public record VehicleRequest(

        @NotBlank(message = "Vehicle ID is required")
        String vehicleId,

        @NotNull(message = "Vehicle capacity is required")
        @Positive(message = "Vehicle capacity must be greater than zero")
        Double capacity,

        @NotNull(message = "Current latitude is required")
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double currentLatitude,

        @NotNull(message = "Current longitude is required")
        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double currentLongitude,

        @NotBlank(message = "Current address is required")
        String currentAddress

) {}
