package com.monu.mobile.feature.services

import com.monu.mobile.domain.model.ServiceCoordinationSnapshot
import com.monu.mobile.domain.model.ServiceDescriptor

class MONUServiceCoordinator {

    fun snapshot(
        services: List<ServiceDescriptor> = emptyList()
    ): ServiceCoordinationSnapshot {
        return ServiceCoordinationSnapshot(
            services = services
        )
    }

    fun registeredServices(
        services: List<ServiceDescriptor>
    ): List<ServiceDescriptor> {
        return services
    }
}
