package photocentre.view.worker

import photocentre.controllers.WorkerController
import photocentre.dao.WorkerDao
import photocentre.dataClasses.WorkerModel
import photocentre.executors.WorkerExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class WorkerCRUDFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val workerModel: WorkerModel by inject()
    val workerDao = WorkerDao(photocentreDataSource)
    val workerController = WorkerController(WorkerExecutor(photocentreDataSource, workerDao))
    override val root = borderpane {
        center = vbox(10.0) {
            paddingAll = 10
            button("Create") {
                action {
                    val workerCreateFragment = WorkerCreateFragment(photocentreDataSource)
                    replaceWith(workerCreateFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
            }
            button("Update") {
                action {
                    val workerDetailsFragment = WorkerDetailsFragment(photocentreDataSource)
                    replaceWith(workerDetailsFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
            }
//            button("countKiosks") {
//                action {
//                    val countKiosksFragment = KioskSelectCountFragment(photocentreDataSource)
//                    replaceWith(countKiosksFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
//                }
//            }
        }
    }
}
