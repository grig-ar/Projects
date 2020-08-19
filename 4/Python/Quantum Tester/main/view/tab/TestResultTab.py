from PyQt5 import QtWidgets


class TestResultTab(object):
    def __init__(self):
        self.tab = QtWidgets.QWidget()
        self.table_widget = QtWidgets.QTableWidget()
        self.row_count = 0
        self.name = 'Results'
        self.tab.setObjectName(self.name)
        columns = ['Test_name', 'Backend_name', 'Job_status', 'Date', 'Shots_taken', 'Time_taken', 'Accuracy']
        self.raw_data = [[1, 2, 3, 4, 5, 6], [7, 8, 9, 10, 1, 12]]
        self.table_widget.setColumnCount(len(columns))
        self.table_widget.setSortingEnabled(True)
        self.table_widget.setHorizontalHeaderLabels(columns)
        self.table_widget.horizontalHeader().sectionClicked.connect(self.print_s)
        self.button_load = QtWidgets.QPushButton()
        self.button_load.setText('Load results')
        self.button_delete = QtWidgets.QPushButton()
        self.button_delete.setText('Delete selected')
        self.layout = QtWidgets.QVBoxLayout(self.tab)
        self.button_layout = QtWidgets.QHBoxLayout(self.tab)
        self.button_layout.addWidget(self.button_load)
        self.button_layout.addWidget(self.button_delete)
        self.layout.addWidget(self.table_widget)
        self.layout.addLayout(self.button_layout)

    def print_s(self, index):
        print(self.table_widget.horizontalHeaderItem(index).text())
