from PyQt5 import QtWidgets
from main.canvas.MyMplCanvas import MyMplCanvas


class TestInfoTab(object):

    def __init__(self, tests, drawer):
        # self.simulator = Aer.get_backend('qasm_simulator')
        self.drawer = drawer

        self.quantum_tests = list(tests)
        self.current_test = self.quantum_tests[0]

        self.tab = QtWidgets.QWidget()
        self.name = 'Tests info'
        self.tab.setObjectName(self.name)
        self.canvas = MyMplCanvas(self.current_test.circuit, self.tab, width=2, height=2, dpi=100)
        self.group_box = QtWidgets.QGroupBox(self.tab)
        self.button = QtWidgets.QPushButton(self.group_box)
        self.button.setText('Resize')
        self.button.clicked.connect(self.action)
        self.group_box.setTitle('Quantum scheme')
        self.group_box_layout = QtWidgets.QVBoxLayout(self.group_box)
        self.group_box_layout.addWidget(self.canvas)
        self.group_box_layout.addWidget(self.button)

        self.grid_layout = QtWidgets.QGridLayout(self.tab)
        self.grid_layout.setObjectName("grid %s" % self.name)
        self.vertical_layout = QtWidgets.QVBoxLayout(self.tab)

        self.button_import = QtWidgets.QPushButton(self.tab)
        self.button_import.setText('Импортировать')
        self.button_export = QtWidgets.QPushButton(self.tab)
        self.button_export.setText('Экспортировать')

        self.buttons_layouts = QtWidgets.QHBoxLayout(self.tab)
        self.buttons_layouts.addWidget(self.button_import)
        self.buttons_layouts.addWidget(self.button_export)

        self.grid_layout.addLayout(self.vertical_layout, 0, 0, 1, 1)
        self.grid_layout.addWidget(self.group_box, 0, 1, 1, 1)

        self.title_label = QtWidgets.QLabel(self.tab)
        self.title_label.setText('Tests:')
        self.vertical_layout.addLayout(self.buttons_layouts)
        self.vertical_layout.addWidget(self.title_label)
        self.tests_combobox = QtWidgets.QComboBox(self.tab)

        for test in self.quantum_tests:
            self.tests_combobox.addItem(test.name)
        self.vertical_layout.addWidget(self.tests_combobox)
        self.tests_combobox.currentTextChanged.connect(self.text_changed)

    def action(self):
        self.canvas.do_resize_now()

    def get_selected_test(self):
        return next(x for x in self.quantum_tests if x.name == self.tests_combobox.currentText())

    def add_new_test(self, test):
        self.quantum_tests.append(test)
        self.tests_combobox.addItem(test.name)
        self.tests_combobox.setCurrentText(test.name)

    def text_changed(self):
        self.current_test = self.get_selected_test()
        self.canvas.compute_initial_figure(self.current_test.circuit)
