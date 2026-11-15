
package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.enums.Priority;
import jakarta.validation.constraints.*;

public record OrderRequest(

        @NotBlank(message = "Order ID is required")
        String orderId,

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double latitude,

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double longitude,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Package weight is required")
        @Positive(message = "Package weight must be greater than zero")
        Double packageWeight,

        @NotNull(message = "Priority is required")
        Priority priority

) {}
