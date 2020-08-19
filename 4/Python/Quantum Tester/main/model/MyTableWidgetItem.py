from PyQt5 import QtWidgets
from PyQt5.QtCore import Qt


class MyTableWidgetItem(QtWidgets.QTableWidgetItem):
    def __lt__(self, other):
        if isinstance(other, QtWidgets.QTableWidgetItem):
            try:
                my_value = float(self.data(Qt.EditRole))
                other_value = float(other.data(Qt.EditRole))
                return my_value < other_value
            except ValueError:
                return super(MyTableWidgetItem, self).__lt__(other)
