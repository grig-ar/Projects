package photocentre.executors

import photocentre.dao.ServiceDao
import photocentre.dataClasses.Service
import photocentre.main.PhotocentreDataSource

class ServiceExecutor(
        private val dataSource: PhotocentreDataSource,
        private val serviceDao: ServiceDao
) {
    fun createService(toCreate: Service): Long {
        return transaction(dataSource) {
            serviceDao.createService(toCreate)
        }
    }

    fun createServices(toCreate: Iterable<Service>): List<Long> {
        return transaction(dataSource) {
            serviceDao.createServices(toCreate)
        }
    }

    fun findService(id: Long): Service? {
        return transaction(dataSource) {
            serviceDao.findService(id)
        }
    }

    fun updateService(service: Service) {
        return transaction(dataSource) {
            serviceDao.updateService(service)
        }
    }

    fun deleteService(id: Long) {
        return transaction(dataSource) {
            serviceDao.deleteService(id)
        }
    }

    fun getAllServices(): List<Service> {
        return transaction(dataSource) {
            serviceDao.gelAll()
        }
    }
}