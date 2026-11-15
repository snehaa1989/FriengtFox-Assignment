
package com.dispatch.dispatch_load_balancer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrdersRequest(

        @NotEmpty(message = "At least one delivery order is required")
        List<@Valid OrderRequest> orders

) {}
