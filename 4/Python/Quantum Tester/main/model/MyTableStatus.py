from PyQt5 import QtWidgets


class MyTableStatus(QtWidgets.QTableWidget):

    def __init__(self):
        QtWidgets.QTableWidget.__init__(self)
        self.setHorizontalHeaderLabels(
            ['Test_name', 'Backend_name', 'Job_status', 'Date', 'Shots_taken', 'Time_taken', 'Counts'])
        self.setSortingEnabled(True)

        self.data = {}
        self.set_data()

    def update_from_dict(self, new_data):
        self.data.clear()
        self.data.update(new_data)
        self.set_data()

    def set_data(self):
        for n, key in enumerate(self.data):
            for m, item in enumerate(self.data[key]):
                new_item = QtWidgets.QTableWidgetItem(item)
                self.setItem(m, n, new_item)
