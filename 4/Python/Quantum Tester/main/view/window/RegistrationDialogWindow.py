from PyQt5 import QtWidgets


class RegistrationDialogWindow(QtWidgets.QDialog):

    @property
    def qiskit_token(self):
        return self.__qiskit_token

    @property
    def dwave_token(self):
        return self.__dwave_token

    @property
    def inspire_token(self):
        return self.__inspire_token

    @qiskit_token.setter
    def qiskit_token(self, value):
        self.qiskit_line.setText(value)
        self.__qiskit_token = value

    @dwave_token.setter
    def dwave_token(self, value):
        self.dwave_line.setText(value)
        self.__dwave_token = value

    @inspire_token.setter
    def inspire_token(self, value):
        self.inspire_line.setText(value)
        self.__inspire_token = value

    def provide_api_tokens(self):
        return self.qiskit_token, self.dwave_token, self.inspire_token

    def __init__(self, init=None, parent=None):
        QtWidgets.QDialog.__init__(self, parent)
        self.setMinimumSize(320, 140)
        self.setWindowTitle('API Tokens')
        self.dialog_layout = QtWidgets.QVBoxLayout(self)
        self.group_box = QtWidgets.QGroupBox(self)
        self.group_box.setTitle('Tokens')
        self.group_box_layout = QtWidgets.QGridLayout(self.group_box)

        self.qiskit_label = QtWidgets.QLabel(self)
        self.qiskit_label.setText('Qiskit api token:')
        self.dwave_label = QtWidgets.QLabel(self)
        self.dwave_label.setText('D-Wave api token:')
        self.inspire_label = QtWidgets.QLabel(self)
        self.inspire_label.setText('Quantum-inspire api token:')

        self.qiskit_line = QtWidgets.QLineEdit(self)
        self.dwave_line = QtWidgets.QLineEdit(self)
        self.inspire_line = QtWidgets.QLineEdit(self)

        self.group_box_layout.addWidget(self.qiskit_label, 0, 0, 1, 1)
        self.group_box_layout.addWidget(self.qiskit_line, 0, 1, 1, 1)
        self.group_box_layout.addWidget(self.dwave_label, 1, 0, 1, 1)
        self.group_box_layout.addWidget(self.dwave_line, 1, 1, 1, 1)
        self.group_box_layout.addWidget(self.inspire_label, 2, 0, 1, 1)
        self.group_box_layout.addWidget(self.inspire_line, 2, 1, 1, 1)

        self.buttons = QtWidgets.QDialogButtonBox(self)
        self.buttons.setStandardButtons(QtWidgets.QDialogButtonBox.Cancel | QtWidgets.QDialogButtonBox.Ok)
        self.buttons.accepted.connect(self.accept)
        self.buttons.rejected.connect(self.reject)

        self.dialog_layout.addWidget(self.group_box)
        self.dialog_layout.addWidget(self.buttons)

        if len(init) != 0:
            self.__qiskit_token = init[0]
            self.__dwave_token = init[1]
            self.__inspire_token = init[2]
        else:
            self.__qiskit_token = ''
            self.__dwave_token = ''
            self.__inspire_token = ''

        self.qiskit_line.setText(self.qiskit_token)
        self.dwave_line.setText(self.dwave_token)
        self.inspire_line.setText(self.inspire_token)
        self.qiskit_line.setCursorPosition(0)
        self.dwave_line.setCursorPosition(0)
        self.inspire_line.setCursorPosition(0)

        self.qiskit_line.textChanged.connect(self.set_qiskit)
        self.dwave_line.textChanged.connect(self.set_dwave)
        self.inspire_line.textChanged.connect(self.set_inspire)

    def set_qiskit(self, new_token):
        self.qiskit_token = new_token

    def set_dwave(self, new_token):
        self.dwave_token = new_token

    def set_inspire(self, new_token):
        self.inspire_token = new_token

    def show_dialog(self):
        self.exec_()
