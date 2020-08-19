package photocentre.controllers

import photocentre.dataClasses.Worker
import photocentre.enums.AreaOfWork
import photocentre.executors.WorkerExecutor

class WorkerController(private val executor: WorkerExecutor) {

    fun createWorker(worker: Worker): Worker {

        val id = executor.createWorker(worker)
        return Worker(
                id = id,
                name = worker.name,
                areaOfWork = worker.areaOfWork,
                position = worker.position
        )
    }

    fun createWorkers(workers: List<Worker>): List<Worker> {

        val ids = executor.createWorkers(workers)
        val newWorkers = ArrayList<Worker>()

        for (i in 1..ids.size) {
            newWorkers += Worker(
                    id = ids[i],
                    name = workers[i].name,
                    areaOfWork = workers[i].areaOfWork,
                    position = workers[i].position
            )
        }
        return newWorkers
    }

    fun getWorker(id: Long): Worker? {
        return executor.findWorker(id)
    }

    fun updateWorker(worker: Worker): String {
        return executor.updateWorker(worker).toString()
    }

    fun deleteWorker(id: Long): String {
        return executor.deleteWorker(id).toString()
    }

    fun getBySpeciazization(areaOfWork: AreaOfWork): List<Worker> {
        return executor.getBySpeciazization(areaOfWork)
    }

    fun getAllWorkers():List<Worker> {
        return executor.getAllWorkers()
    }
}