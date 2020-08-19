package photocentre.executors

import photocentre.dao.WorkerDao
import photocentre.dataClasses.Worker
import photocentre.enums.AreaOfWork
import photocentre.main.PhotocentreDataSource

class WorkerExecutor(
        private val dataSource: PhotocentreDataSource,
        private val workerDao: WorkerDao
) {
    fun createWorker(toCreate: Worker): Long {
        return transaction(dataSource) {
            workerDao.createWorker(toCreate)
        }
    }

    fun createWorkers(toCreate: Iterable<Worker>): List<Long> {
        return transaction(dataSource) {
            workerDao.createWorkers(toCreate)
        }
    }

    fun findWorker(id: Long): Worker? {
        return transaction(dataSource) {
            workerDao.findWorker(id)
        }
    }

    fun updateWorker(worker: Worker) {
        return transaction(dataSource) {
            workerDao.updateWorker(worker)
        }
    }

    fun deleteWorker(id: Long) {
        return transaction(dataSource) {
            workerDao.deleteWorker(id)
        }
    }

    fun getBySpeciazization(areaOfWork: AreaOfWork): List<Worker> {
        return transaction(dataSource) {
            workerDao.getBySpeciazization(areaOfWork)
        }
    }

    fun getAllWorkers(): List<Worker> {
        return transaction(dataSource) {
            workerDao.getAll()
        }
    }
}