import numpy as np
from PyQt5 import QtCore, QtGui
from PyQt5.QtCore import Qt


class TableModel(QtCore.QAbstractTableModel):
    def __init__(self, data):
        super(TableModel, self).__init__()
        self._data = data
        self.min_time = data.min()['Time_taken']

    def setData(self, index, value):
        self.__data[index.row()][index.column()] = value
        return True

    def insertRows(self, item):
        self.beginInsertRows(QtCore.QModelIndex(), len(self._data), len(self._data) + 1)
        self._data.append(item)
        self.endInsertRows()
        return True

    def data(self, index, role):
        if role == Qt.DisplayRole:
            value = self._data.iloc[index.row()][index.column()]
            # if isinstance(value, float):
            #     # Render float to 2 dp
            #     return "%.2f" % value
            #
            # if isinstance(value, str):
            #     # Render strings with quotes
            #     return '"%s"' % value
            return str(value)

        if role == Qt.ForegroundRole and index.column() == 4:
            value = self._data.iloc[index.row()][index.column()]
            if (
                    (isinstance(value, np.int64) or isinstance(value, float))
                    and value == self.min_time
            ):
                return QtGui.QColor('red')

    def sort(self, column, order):
        pass

    def headerData(self, section, orientation, role):
        if role == Qt.DisplayRole:
            if orientation == Qt.Horizontal:
                return str(self._data.columns[section])

            if orientation == Qt.Vertical:
                return str(self._data.index[section])

    def rowCount(self, index):
        return self._data.shape[0]

    def columnCount(self, index):
        return self._data.shape[1]

    def setmydata(self):
        for n, key in enumerate(self.data):
            for m, item in enumerate(self.data[key]):
                newitem = QtGui.QTableWidgetItem(item)
                self.setItem(m, n, newitem)
