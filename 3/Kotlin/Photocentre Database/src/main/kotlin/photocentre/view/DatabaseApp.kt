package photocentre.view

import tornadofx.*
import javafx.scene.Scene

class DatabaseApp : App(MainView::class) {
    override fun createPrimaryScene(view: UIComponent) = Scene(view.root, 1280.0, 768.0)
}

//todo main теперь тут
fun main(args: Array<String>) {
    launch<DatabaseApp>(args)
}