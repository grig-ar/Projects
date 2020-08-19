package photocentre.view.worker

import photocentre.controllers.WorkerController
import photocentre.dao.WorkerDao
import photocentre.dataClasses.Worker
import photocentre.dataClasses.WorkerModel
import photocentre.executors.WorkerExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class WorkerListFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val workerDao = WorkerDao(photocentreDataSource)
    val workerController = WorkerController(WorkerExecutor(photocentreDataSource, workerDao))
    val workerModel: WorkerModel by inject()

    init {
        workerModel.workers.set(workerController.getAllWorkers().asObservable())
    }

    override val root = borderpane {
        center = tableview(workerModel.workers) {
            prefHeight = 768.0
            column("ID", Worker::idProperty)
            column("Name", Worker::nameProperty)
            column("Area of work", Worker::areaOfWorkProperty)
            column("Position", Worker::positionProperty)
            bindSelected(workerModel)
            contextmenu {
                item("Delete").action {
                    workerController.deleteWorker(selectedItem?.id!!)
                    workerModel.workers.set(workerController.getAllWorkers().asObservable())
                }
            }
            columnResizePolicy = SmartResize.POLICY
        }
    }
}