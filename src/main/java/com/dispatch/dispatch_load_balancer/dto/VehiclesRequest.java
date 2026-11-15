
package com.dispatch.dispatch_load_balancer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record VehiclesRequest(

        @NotEmpty(message = "At least one vehicle is required")
        List<@Valid VehicleRequest> vehicles

) {}
