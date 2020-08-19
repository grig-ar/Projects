package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import photocentre.enums.AreaOfWork
import photocentre.enums.Position
import tornadofx.*

class Worker
(
        id: Long = -1,
        name: String = "",
        areaOfWork: AreaOfWork? = null,
        position: Position? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Worker::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(Worker::name)

    var areaOfWork: AreaOfWork by property(areaOfWork)
    fun areaOfWorkProperty() = getProperty(Worker::areaOfWork)

    var position: Position? by property(position)
    fun positionProperty() = getProperty(Worker::position)
}

class WorkerModel : ItemViewModel<Worker>() {
    val workers = SimpleObjectProperty<ObservableList<Worker>>()
    val id = bind(Worker::idProperty)
    val name = bind(Worker::nameProperty)
    val areaOfWork = bind(Worker::areaOfWorkProperty)
    val position = bind(Worker::positionProperty)
}