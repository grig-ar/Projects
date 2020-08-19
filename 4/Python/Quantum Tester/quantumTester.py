from PyQt5 import QtWidgets
import sys

from main.view.window.UiMainWindow import UiMainWindow

# def bind(object_name, property_name, property_type):
#     def getter(self):
#         return property_type(self.findChild(QObject, object_name).property(property_name).toPyObject())
#
#     def setter(self, value):
#         self.findChild(QObject, object_name).setProperty(property_name, QtCore.QVariant(value))
#
#     return property(getter, setter)


if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    main_window = QtWidgets.QMainWindow()
    ui = UiMainWindow(main_window)
    main_window.show()
    sys.exit(app.exec_())
