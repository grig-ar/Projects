from PyQt5 import QtCore, QtWidgets
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg
from matplotlib.figure import Figure

# backends = []
# for back in self.qi.get_backend_types():
#     current_backend = {'name': back.get('name'), 'number_of_qubits': back.get('number_of_qubits'),
#                        'max_number_of_shots': back.get('max_number_of_shots'),
#                        'max_experiments': back.get('max_number_of_simultaneous_jobs'),
#                        'description': back.get('description'), 'accuracy': 'not measured',
#                        'remaining_jobs': back.get('max_number_of_simultaneous_jobs')}
#     backends.append(current_backend)

class ComputerInfoTab(object):
    __labels_amount = 8
    __label_args = ['Qubits', 'Max shots', 'Max experiments', 'Version', 'CX Gate Error', 'Remaining jobs',
                    'Temperature']

    def __init__(self, backend):
        self.num_qubits = None
        self.is_inspire = False
        max_shots = None
        max_experiments = None
        backend_version = None
        accuracy = None
        remaining_jobs = None
        temperature = None
        if isinstance(backend, dict):
            self.is_inspire = True
            self.name = backend['name']
            self.num_qubits = backend['number_of_qubits']
            max_shots = backend['max_number_of_shots']
            max_experiments = backend['max_number_of_simultaneous_jobs']
            backend_version = backend['description']
            accuracy = 0
            remaining_jobs = backend['max_number_of_simultaneous_jobs']
            temperature = 0
        else:
            self.name = backend.name()
            self.configuration = backend.configuration()
            self.num_qubits = self.configuration.num_qubits
            max_shots = self.configuration.max_shots
            max_experiments = self.configuration.max_experiments
            backend_version = self.configuration.backend_version
            if self.name == 'ibmq_qasm_simulator' or self.configuration.num_qubits == 1:
                accuracy = 0
            else:
                accuracy = backend.properties().gate_error('cx', [0, 1])
            remaining_jobs = backend.remaining_jobs_count()
            temperature = 'TODO'

        self.tab = QtWidgets.QWidget()
        self.tab.setObjectName(self.name)
        self.grid_layout = QtWidgets.QGridLayout(self.tab)
        self.grid_layout.setContentsMargins(50, 0, 300, 0)
        self.grid_layout.setObjectName("grid %s" % self.name)
        self.labels = []
        self.lines = []

        for i in range(0, self.__labels_amount):
            self.current_label = QtWidgets.QLabel(self.tab)
            self.current_label.setObjectName("current_label_%s" % i)
            self.labels.append(self.current_label)
            self.grid_layout.addWidget(self.current_label, i, 0)

            if i == 0:
                self.current_label.setMaximumSize(QtCore.QSize(16777215, 100))

            if i > 0:
                self.current_line = QtWidgets.QLineEdit(self.tab)
                self.current_line.setMaximumWidth(100)
                self.current_line.setReadOnly(True)
                self.current_line.setObjectName("current_line_%s" % i)
                self.lines.append(self.current_line)
                self.grid_layout.addWidget(self.current_line, i, 1)

        self.labels[0].setText(self.name)
        for i in range(1, 8):
            self.labels[i].setText(self.__label_args[i - 1])
        self.lines[0].setText(str(self.num_qubits))
        self.lines[1].setText(str(max_shots))
        self.lines[2].setText(str(max_experiments))
        self.lines[3].setText(str(backend_version))
        self.lines[4].setText(str(accuracy))
        self.lines[5].setText(str(remaining_jobs))
        self.lines[6].setText(str(temperature))

        self.figure = Figure(figsize=(6, 9), dpi=95)
        self.canvas = FigureCanvasQTAgg(self.figure)
        self.canvas.setObjectName("canvas %s" % self.name)
        self.grid_layout.addWidget(self.canvas, 0, 1)

    def get_is_inspire(self):
        return self.is_inspire
