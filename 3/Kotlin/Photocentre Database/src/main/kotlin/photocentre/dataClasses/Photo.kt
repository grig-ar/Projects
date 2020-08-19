package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import photocentre.enums.PaperType
import photocentre.enums.PhotoFormat
import tornadofx.*

class Photo
(
        id: Long = -1,
        paperType: PaperType? = null,
        format: PhotoFormat? = null,
        film: Film? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Photo::id)

    var paperType: PaperType by property(paperType)
    fun paperTypeProperty() = getProperty(Photo::paperType)

    var format: PhotoFormat by property(format)
    fun formatProperty() = getProperty(Photo::format)

    var film: Film by property(film)
    fun filmProperty() = getProperty(Photo::film)
}

class PhotoModel : ItemViewModel<Photo>() {
    val photos = SimpleObjectProperty<ObservableList<Photo>>()
    val id = bind(Photo::idProperty)
    val paperType = bind(Photo::paperTypeProperty)
    val format = bind(Photo::formatProperty)
    val film = bind(Photo::filmProperty)
}