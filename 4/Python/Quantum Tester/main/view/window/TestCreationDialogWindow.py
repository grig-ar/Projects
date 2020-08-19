from PyQt5 import QtCore, QtWidgets


class TestCreationDialogWindow(QtWidgets.QDialog):

    def select_all_computers(self):
        for checkbox in self.computer_checkboxes:
            if checkbox.isEnabled():
                checkbox.setChecked(True)

    def deselect_all_computers(self):
        for checkbox in self.computer_checkboxes:
            checkbox.setChecked(False)

    @property
    def current_shots(self):
        return self._current_shots

    @current_shots.setter
    def current_shots(self, value):
        if (value == '') or (int(value) < 1):
            value = 1
        if int(value) > 8192:
            value = 8192

        self.shots_line.setText(str(value))
        self._current_shots = value
        self.current_test.shots = value

    def set_shots(self, shots):
        self.current_shots = int(shots)

    def __init__(self, cur_computers, cur_tests, parent=None):
        QtWidgets.QDialog.__init__(self, parent)
        self.shots_line = QtWidgets.QLineEdit(self)
        self.shots_line.textChanged.connect(self.set_shots)
        self.group_box = QtWidgets.QGroupBox(self)
        self.group_box_layout = QtWidgets.QVBoxLayout(self.group_box)
        self.computer_checkboxes = []
        for item in cur_computers:
            cur_checkbox = QtWidgets.QCheckBox(self.group_box)
            cur_checkbox.setText(item.name)
            self.group_box_layout.addWidget(cur_checkbox)
            self.computer_checkboxes.append(cur_checkbox)

        self._current_shots = 1

        self.grid_layout = QtWidgets.QGridLayout(self)
        self.quantum_tests = list(cur_tests)
        self.quantum_computers = list(cur_computers)
        self.tests = QtWidgets.QComboBox(self)
        self.tests.currentTextChanged.connect(self.text_changed)
        for item in cur_tests:
            self.tests.addItem(item.name)
        self.current_test = cur_tests[0]
        self.current_shots = self.current_test.shots

        self.group_box.setTitle('Computers')

        self.tech_buttons_layout = QtWidgets.QHBoxLayout(self.group_box)
        self.shots_layout = QtWidgets.QHBoxLayout(self)
        self.shots_layout.setContentsMargins(0, 291, 0, 0)
        self.shots_label = QtWidgets.QLabel(self)
        self.shots_label.setText('Shots: ')
        self.shots_layout.addWidget(self.shots_label)
        self.shots_layout.addWidget(self.shots_line)
        self.button_select_all = QtWidgets.QPushButton(self.group_box)
        self.button_select_all.setText('Select all')
        self.button_select_all.clicked.connect(self.select_all_computers)
        self.button_deselect_all = QtWidgets.QPushButton(self.group_box)
        self.button_deselect_all.setText('Deselect all')
        self.button_deselect_all.clicked.connect(self.deselect_all_computers)
        self.tech_buttons_layout.addWidget(self.button_select_all)
        self.tech_buttons_layout.addWidget(self.button_deselect_all)

        self.group_box_layout.addLayout(self.tech_buttons_layout)

        self.buttons = QtWidgets.QDialogButtonBox(self)
        self.buttons.setStandardButtons(QtWidgets.QDialogButtonBox.Cancel | QtWidgets.QDialogButtonBox.Ok)
        self.buttons.accepted.connect(self.accept)
        self.buttons.rejected.connect(self.reject)
        self.grid_layout.addWidget(self.tests, 1, 0, 1, 1)
        self.grid_layout.addWidget(self.group_box, 0, 1, 1, 1)
        self.grid_layout.addLayout(self.shots_layout, 0, 0, 1, 1)
        self.grid_layout.addWidget(self.buttons, 1, 1, 1, 1)
        self.setWindowTitle("Dialog")
        self.setWindowModality(QtCore.Qt.ApplicationModal)

    def get_selected_test(self):
        return str(self.tests.currentText())

    def text_changed(self):
        self.current_test = next(x for x in self.quantum_tests if x.name == self.tests.currentText())

        for item in self.computer_checkboxes:
            item.setDisabled(False)

        for i in range(0, len(self.quantum_computers)):
            if self.current_test.circuit.num_qubits > self.quantum_computers[i].num_qubits:
                self.computer_checkboxes[i].setDisabled(True)

        self.current_shots = self.current_test.shots

    def add_new_test(self, test):
        self.tests.addItem(test.name)
        self.quantum_tests.append(test)

    def get_selected_computers(self):
        computers = []
        for checkbox in self.computer_checkboxes:
            if checkbox.isChecked():
                computers.append(checkbox.text())

        return computers

    def show_dialog(self):
        self.exec_()
