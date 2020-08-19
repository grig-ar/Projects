package photocentre.controllers

import photocentre.dataClasses.Service
import photocentre.executors.ServiceExecutor

class ServiceController(private val executor: ServiceExecutor) {

    fun createService(service: Service): Service {
        val id = executor.createService(service)
        return Service(
                id = id,
                name = service.name,
                cost = service.cost,
                branchOffice = service.branchOffice
        )
    }

    fun createServices(services: List<Service>): List<Service> {
        val ids = executor.createServices(services)
        val newServices = ArrayList<Service>()

        for (i in 1..ids.size) {
            newServices += Service(
                    id = ids[i],
                    name = services[i].name,
                    cost = services[i].cost,
                    branchOffice = services[i].branchOffice
            )
        }
        return newServices
    }

    fun getService(id: Long): Service? {
        return executor.findService(id)
    }
    
    fun updateService(service: Service): String {
        return executor.updateService(service).toString()
    }
    
    fun deleteService(id: Long): String {
        return executor.deleteService(id).toString()
    }

    fun getAllServices(): List<Service> {
        return executor.getAllServices()
    }
}