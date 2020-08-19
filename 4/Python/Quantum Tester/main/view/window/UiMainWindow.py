import csv
import os
import sys

import qiskit as q
from PyQt5 import QtCore, QtWidgets
from PyQt5.QtCore import QThreadPool
from qiskit import IBMQ
from qiskit.qasm import QasmError
from qiskit.tools.monitor import job_monitor
from qiskit.visualization import plot_histogram
from quantuminspire.api import QuantumInspireAPI
from quantuminspire.credentials import enable_account
from quantuminspire.credentials import get_token_authentication
from quantuminspire.qiskit import QI

from main.data.DataRepository import DataRepository
from main.drawer.PlotDrawer import PlotDrawer
from main.model.MyTableWidgetItem import MyTableWidgetItem
from main.model.Result import Result
from main.quantum.circuit.GroverCircuit import GroverCircuit
from main.quantum.circuit.ShorCircuit import ShorCircuit
from main.quantum.test.QuantumTest import QuantumTest
from main.view.tab.ComputerInfoTab import ComputerInfoTab
from main.view.tab.TestInfoTab import TestInfoTab
from main.view.tab.TestResultTab import TestResultTab
from main.view.window.RegistrationDialogWindow import RegistrationDialogWindow
from main.view.window.TestCreationDialogWindow import TestCreationDialogWindow
from main.worker.Worker import Worker
from main.worker.Worker2 import Worker2
from main.writer.FileWriter import FileWriter


class UiMainWindow(object):
    __result = None

    def __init__(self, main_window_local):
        grover_circuit_constructor = GroverCircuit()
        shor_circuit_constructor = ShorCircuit()
        tokens = list(self.get_config().values())
        self.__qiskit = tokens[0]
        self.__dwave = tokens[1]
        self.__inspire = tokens[2]

        self.registration_dialog = RegistrationDialogWindow(init=tokens)

        self.ibm_backends = None
        self.dwave_backends = None
        self.inspire_backends = None

        self.window = main_window_local
        self.central_widget = QtWidgets.QWidget(main_window_local)
        self.central_widget.setObjectName("central_widget")
        self.repo = DataRepository()
        saved_results = self.repo.get_all()

        self.thread_pool = QThreadPool()
        self.thread_pool.setMaxThreadCount(8)

        self.inner_thread_pool = QThreadPool()
        self.inner_thread_pool.setMaxThreadCount(8)

        self.drawer = PlotDrawer()
        self.writer = FileWriter()
        self.tab_infos = []
        self.default_tests = []

        # quantumRegister = q.QuantumRegister(2)
        # classicalRegister = q.ClassicalRegister(2)
        # circuit = q.QuantumCircuit(quantumRegister, classicalRegister)
        # circuit.h(quantumRegister[0])
        # circuit.cx(quantumRegister[0], quantumRegister[1])
        # circuit.measure(quantumRegister, classicalRegister)
        # self.default_tests.append(QuantumTest("BellState", circuit, 1024))

        shor_answer = 4
        grover_2_answer = 3
        grover_3_answer = 6
        grover_4_answer = 2
        grover_5_answer = 21

        circuit3 = q.QuantumCircuit(5, 5)
        shor_circuit_constructor.add_circuit(circuit3)
        self.default_tests.append(QuantumTest('Shor_3_5', circuit3, 1024, shor_answer))

        circuit4 = q.QuantumCircuit(2, 2)
        grover_circuit_constructor.set_number(grover_2_answer)
        grover_circuit_constructor.add_circuit(circuit4)
        self.default_tests.append(QuantumTest('Grover_2_bits', circuit4, 1024, grover_2_answer))

        circuit_new = q.QuantumCircuit(3, 3)
        grover_circuit_constructor.set_number(grover_3_answer)
        grover_circuit_constructor.add_circuit(circuit_new)
        self.default_tests.append(QuantumTest('Grover_3_bits', circuit_new, 1024, grover_3_answer))

        circuit5 = q.QuantumCircuit(4, 4)
        grover_circuit_constructor.set_number(grover_4_answer)
        grover_circuit_constructor.add_circuit(circuit5)
        self.default_tests.append(QuantumTest('Grover_4_bits', circuit5, 1024, grover_4_answer))

        qubits = q.QuantumRegister(5)
        bits = q.ClassicalRegister(5)
        # controls = list()
        # controls.append(qubits[0])
        # controls.append(qubits[1])
        # controls.append(qubits[2])
        circuit6 = q.QuantumCircuit(qubits, bits)
        grover_circuit_constructor.set_number(grover_5_answer)
        grover_circuit_constructor.add_circuit(circuit6)

        self.default_tests.append(QuantumTest('Grover_5_bits', circuit6, 1024, grover_5_answer))

        self.test_info_tab = TestInfoTab(self.default_tests, self.drawer)
        self.results_info_tab = TestResultTab()

        self.setup_ui(main_window_local)

        self.setup_tabs()

        self.dialog = TestCreationDialogWindow(self.tab_infos, self.default_tests)
        self.dialog.setWindowTitle('Create test')

        for res in saved_results:
            self.add_result_from_db(res)

    def get_config(self):
        keys = ['qiskit', 'dwave', 'quantum-inspire']
        tokens = dict.fromkeys(keys)
        try:
            with open('main\\configuration\\config.ini') as config_file:
                lines = config_file.readlines()
                for line in lines:
                    api_name = line.strip().split('=')[0]
                    if api_name in keys:
                        tokens[api_name] = line.strip().split('=')[1]
        except OSError:
            print('Config file not found')
        except:
            print(sys.exc_info()[0])
            print('Corrupted file')
        return tokens

    def open_qasm_circuit(self):
        files_filter = 'QASM files (*.qasm);;All files (*.*)'
        name = QtWidgets.QFileDialog.getOpenFileName(self.window, 'Open File', filter=files_filter)
        extension = os.path.splitext(name[0])[1]
        if extension == '.qasm':
            try:
                new_circuit = q.QuantumCircuit().from_qasm_file(name[0])
                new_test = QuantumTest(os.path.basename(os.path.splitext(name[0])[0]), new_circuit, 1024)
                self.default_tests.append(new_test)
                self.test_info_tab.add_new_test(new_test)
                self.dialog.add_new_test(new_test)
            except QasmError:
                msg = QtWidgets.QMessageBox()
                msg.setIcon(QtWidgets.QMessageBox.Critical)
                msg.setText('File %s is corrupted!' % os.path.splitext(name[0])[0])
                msg.setWindowTitle('Error!')
                msg.exec_()

    def delete_results(self):
        indexes = self.results_info_tab.table_widget.selectionModel().selectedRows()
        for i in range(0, len(indexes)):
            self.results_info_tab.table_widget.removeRow(indexes[i].row())
        self.results_info_tab.table_widget.resizeColumnsToContents()

    def open_results(self):  # TODO
        files_filter = 'CSV files (*.csv);;All files (*.*)'
        name = QtWidgets.QFileDialog.getOpenFileName(self.window, 'Open File', filter=files_filter)
        extension = os.path.splitext(name[0])[1]
        if extension == '.csv':
            try:
                with open(name[0], newline='') as csvfile:
                    csv_reader = csv.reader(csvfile, delimiter=';')
                    headers = next(csv_reader, None)
                    if headers != ['Test_name', 'Result', 'Backend_name', 'Job_status', 'Date', 'Shots_taken',
                                   'Time_taken', 'Accuracy', 'Counts']:
                        raise RuntimeError('Incorrect file format!')
                    for row in csv_reader:
                        if len(row) == 0:
                            continue
                        self.results_info_tab.table_widget.insertRow(self.results_info_tab.table_widget.rowCount())
                        dict_data = dict.fromkeys(headers)
                        test = next((x for x in self.default_tests if x.name == row[0]),
                                    QuantumTest(row[0], None, row[5], row[1]))
                        if test not in self.default_tests:
                            self.default_tests.append(test)

                        dict_data[headers[0]] = row[0]
                        self.results_info_tab.table_widget.setItem(
                            self.results_info_tab.table_widget.rowCount() - 1, 0,
                            MyTableWidgetItem(str(row[0])))
                        for j in range(2, 8):
                            dict_data[headers[j]] = row[j]
                            self.results_info_tab.table_widget.setItem(
                                self.results_info_tab.table_widget.rowCount() - 1, j - 1,
                                MyTableWidgetItem(str(row[j])))
                        dict_data[headers[8]] = row[8]
                        test.results.append(dict_data)
                    self.results_info_tab.table_widget.resizeColumnsToContents()

            except:
                msg = QtWidgets.QMessageBox()
                msg.setIcon(QtWidgets.QMessageBox.Critical)
                msg.setText("Oops! %s occurred." % sys.exc_info()[0])
                msg.setWindowTitle('Error!')
                msg.exec_()

    def save_results(self):
        files_filter = 'CSV files (*.csv);;All files (*.*)'
        name = QtWidgets.QFileDialog.getSaveFileName(self.window, 'Save File', filter=files_filter)
        if all(name):
            func = self.writer.write_result(os.path.basename(os.path.splitext(name[0])[0]), self.default_tests)
            func()

    def save_qasm_circuit(self):
        files_filter = 'QASM files (*.qasm);;All files (*.*)'
        name = QtWidgets.QFileDialog.getSaveFileName(self.window, 'Save File', filter=files_filter)
        if all(name):
            file = open(name[0], 'w')
            text = self.test_info_tab.get_selected_test().circuit.qasm()
            file.write(text)
            file.close()

    def background_calc(self, cur_circuit, cur_computer):
        job = q.execute(cur_circuit, backend=cur_computer)
        job_monitor(job)
        self.__result = job.result()
        return job.result()

    def test_and_print(self, cur_test, computers):
        print('Starting test and print')
        worker = Worker(self.test_selected_computers, cur_test, computers)
        print('Pool start')
        self.thread_pool.start(worker)

    def print_s(self):
        print('s')

    def add_result_from_db(self, result: Result):
        self.results_info_tab.table_widget.insertRow(self.results_info_tab.table_widget.rowCount())
        res_list = list()
        res_list.append(result.test_name)
        res_list.append(result.backend_name)
        res_list.append(result.job_status)
        res_list.append(result.date)
        res_list.append(result.shots)
        res_list.append(result.time)
        res_list.append(result.accuracy)
        for j in range(0, 7):
            self.results_info_tab.table_widget.setItem(
                self.results_info_tab.table_widget.rowCount() - 1, j,
                MyTableWidgetItem(str(res_list[j])))
        self.results_info_tab.table_widget.resizeColumnsToContents()

    def add_new_result(self, result):
        self.results_info_tab.table_widget.insertRow(self.results_info_tab.table_widget.rowCount())
        res_list = list(result.values())
        result_value = result['Result']
        res_list.remove(result_value)
        for j in range(0, 7):
            self.results_info_tab.table_widget.setItem(
                self.results_info_tab.table_widget.rowCount() - 1, j,
                MyTableWidgetItem(str(res_list[j])))
        self.results_info_tab.table_widget.resizeColumnsToContents()

    def test_selected_computers(self, cur_test, computers):
        fields = ['Test_name', 'Backend_name', 'Job_status', 'Date', 'Shots_taken', 'Time_taken', 'Counts']
        print('start test_selected')
        print(str(self))
        print(str(cur_test))
        print(str(computers))
        tabs = []
        test = next(x for x in self.default_tests if x.name == cur_test)
        backend = None
        for computer in computers:
            try:
                backend = next(x for x in self.ibm_backends if str(x) == computer)
            except StopIteration:
                for back in self.inspire_backends:
                    if computer == back['name']:
                        backend = back
            if backend is None:
                raise RuntimeError('Unknown backend')
            tab = next(x for x in self.tab_infos if x.name == computer)
            tabs.append(tab)
            if computer in ['QX single-node simulator', 'QX single-node simulator SurfSara', 'Spin-2', 'Starmon-5']:
                worker = Worker2(test.run_inspire, backend, self.qi)
                print('Start inner pool')
                self.inner_thread_pool.start(worker)
            else:
                worker = Worker2(test.run_ibm, backend)
                print('Start inner pool')
                self.inner_thread_pool.start(worker)
        self.inner_thread_pool.waitForDone()

        for i in range(0, len(tabs)):
            self.drawer.plot_results2(tabs[i], test.not_drawn_res[i]['Counts'])
        print('Finished after testing work')
        for result in test.not_drawn_res:
            self.add_new_result(result)
            self.repo.insert_result(Result.create_result_from_dict(result))
        test.not_drawn_res.clear()

    def enter_keys(self):
        self.registration_dialog.show_dialog()
        if self.registration_dialog.result() == QtWidgets.QDialog.Accepted:
            qiskit_token, dwave_token, inspire_token = self.registration_dialog.provide_api_tokens()
            is_update_necessary = False
            if self.__qiskit != qiskit_token:
                self.__qiskit = qiskit_token
                is_update_necessary = True
            if self.__dwave != dwave_token:
                self.__dwave = dwave_token
                is_update_necessary = True
            if self.__inspire != inspire_token:
                self.__inspire = inspire_token
                is_update_necessary = True

            if is_update_necessary:
                self.setup_tabs()
                self.dialog = TestCreationDialogWindow(self.tab_infos, self.default_tests)

    def choose_test(self):
        self.dialog.show_dialog()
        if self.dialog.result() == QtWidgets.QDialog.Accepted:
            test = self.dialog.get_selected_test()
            computers = self.dialog.get_selected_computers()
            if computers:
                self.test_and_print(test, computers)

        elif self.dialog.result() == QtWidgets.QDialog.Rejected:
            self.status_label.setText('Rejected')

    def show_status(self, message):
        def show():
            self.status_label.setText(message)

        return show

    def setup_ui(self, main_window_local):
        main_window_local.setObjectName("main_window")
        main_window_local.resize(1024, 768)

        self.gridLayout_2 = QtWidgets.QGridLayout(self.central_widget)
        self.gridLayout_2.setObjectName("gridLayout_2")

        main_window_local.setCentralWidget(self.central_widget)
        self.menubar = QtWidgets.QMenuBar(main_window_local)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 766, 21))
        self.menubar.setObjectName("menubar")

        self.statusbar = QtWidgets.QStatusBar(main_window_local)
        self.statusbar.setObjectName("statusbar")
        main_window_local.setStatusBar(self.statusbar)

        self.menu_file = QtWidgets.QMenu(self.menubar)
        self.menu_file.setObjectName("menu_file")
        self.menu_service = QtWidgets.QMenu(self.menubar)
        self.menu_service.setObjectName("menu_service")
        self.menu_help = QtWidgets.QMenu(self.menubar)
        self.menu_help.setObjectName("menu_help")

        self.menu_edit = QtWidgets.QMenu(self.menubar)
        self.menu_edit.setObjectName("menu_edit")
        self.menu_edit.setStatusTip('Editing')

        main_window_local.setMenuBar(self.menubar)

        self.status_label = QtWidgets.QLabel(self.statusbar)
        self.status_label.setGeometry(5, -5, 500, 20)
        self.status_label.setObjectName('status_label')
        self.status_label.setText('')

        self.action_about = QtWidgets.QAction(main_window_local)
        self.action_about.setObjectName("action_about")
        self.action_about.setStatusTip('Подробнее о программе')

        self.action_registration = QtWidgets.QAction(main_window_local)
        self.action_registration.setObjectName('action_registration')
        self.action_registration.setStatusTip('Ввести ключи регистрации')
        self.action_registration.triggered.connect(self.enter_keys)

        self.action_new = QtWidgets.QAction(main_window_local)
        self.action_new.setObjectName("action_new")
        self.action_new.triggered.connect(self.choose_test)
        self.action_new.setStatusTip('Создать новый файл')

        self.test_info_tab.button_import.clicked.connect(self.open_qasm_circuit)
        self.test_info_tab.button_export.clicked.connect(self.save_qasm_circuit)

        self.results_info_tab.button_load.clicked.connect(self.open_results)
        self.results_info_tab.button_delete.clicked.connect(self.delete_results)

        self.action_open = QtWidgets.QAction(main_window_local)
        self.action_open.setObjectName("action_open")
        self.action_open.setStatusTip('Открыть файл результатов')

        self.action_save = QtWidgets.QAction(main_window_local)
        self.action_save.setObjectName("action_save")
        self.action_save.setStatusTip('Сохранить результаты')

        self.action_save_as = QtWidgets.QAction(main_window_local)
        self.action_save_as.setObjectName("action_save_as")
        self.action_save_as.setStatusTip('Сохранить результаты в новый файл')
        self.action_save_as.triggered.connect(self.save_results)

        self.action_close = QtWidgets.QAction(main_window_local)
        self.action_close.setObjectName("action_close")
        self.action_close.setStatusTip('Выйти из программы')

        self.action_update = QtWidgets.QAction(main_window_local)
        self.action_update.setObjectName("action_update")
        self.action_update.triggered.connect(self.clear_plots)
        self.action_update.setStatusTip('Очистить графики')

        self.action_test = QtWidgets.QAction(main_window_local)
        self.action_test.setObjectName("action_test")
        self.action_test.setStatusTip('Протестировать компьютеры')

        self.menu_file.addAction(self.action_new)
        self.menu_file.addAction(self.action_open)
        self.menu_file.addSeparator()
        self.menu_file.addAction(self.action_save)
        self.menu_file.addAction(self.action_save_as)
        self.menu_file.addSeparator()
        self.menu_file.addAction(self.action_close)
        self.menu_service.addAction(self.action_update)
        self.menu_service.addAction(self.action_test)
        self.menu_help.addAction(self.action_about)
        self.menu_help.addSeparator()
        self.menu_help.addAction(self.action_registration)
        self.menubar.addAction(self.menu_file.menuAction())
        self.menubar.addAction(self.menu_edit.menuAction())
        self.menubar.addAction(self.menu_service.menuAction())
        self.menubar.addAction(self.menu_help.menuAction())
        self.retranslate_ui(main_window_local)

        QtCore.QMetaObject.connectSlotsByName(main_window_local)

    def plot_result(self, cur_circuit, cur_result):
        def plot():
            for tab_info in self.tab_infos:
                axes = tab_info.figure.add_subplot(111)
                axes.clear()
                plot_histogram(self.__result.get_counts(cur_circuit), ax=axes)
                tab_info.canvas.draw()

        return plot

    def clear_plots(self):
        for tab_info in self.tab_infos:
            axes = tab_info.figure.add_subplot(111)
            axes.clear()
            tab_info.canvas.draw()

    def plot_circuit(self, cur_circuit):
        for tab_info in self.tab_infos:
            axes = tab_info.figure.add_subplot(111)
            axes.clear()
            cur_circuit.draw(output='mpl', ax=axes)
            tab_info.canvas.draw()

    def retranslate_ui(self, main_window_local):
        _translate = QtCore.QCoreApplication.translate
        main_window_local.setWindowTitle(_translate("main_window", "QuantumTester 0.0.1 x64"))
        self.menu_file.setTitle(_translate("main_window", "Файл"))
        self.menu_service.setTitle(_translate("main_window", "Сервис"))
        self.menu_help.setTitle(_translate("main_window", "Помощь"))
        self.menu_edit.setTitle(_translate("main_window", "Правка"))
        self.action_about.setText(_translate("main_window", "О программе..."))
        self.action_registration.setText(_translate("main_window", "Регистрация"))
        self.action_new.setText(_translate("main_window", "Новый"))
        self.action_new.setShortcut(_translate("main_window", "Ctrl+N"))
        self.action_open.setText(_translate("main_window", "Открыть"))
        self.action_open.setShortcut(_translate("main_window", "Ctrl+O"))
        self.action_save.setText(_translate("main_window", "Сохранить"))
        self.action_save.setShortcut(_translate("main_window", "Ctrl+S"))
        self.action_save_as.setText(_translate("main_window", "Сохранить как..."))
        self.action_close.setText(_translate("main_window", "Закрыть"))
        self.action_update.setText(_translate("main_window", "Обновить"))
        self.action_test.setText(_translate("main_window", "Протестировать"))

    def setup_tabs(self):
        _translate = QtCore.QCoreApplication.translate
        self.ibm_backends = None
        self.dwave_backends = None
        self.inspire_backends = None
        self.rigetti_backends = None
        self.tab_infos = []

        # backend1 = {'name': '9q-square-noisy-qvm', 'number_of_qubits': 9,
        #             'max_number_of_shots': 65536, 'max_experiments': 'unlimited',
        #             'description': 'v. 1.17', 'accuracy': 'not accurate',
        #             'remaining_jobs': 'unlimited'}
        # backend2 = {'name': '26q-qvm', 'number_of_qubits': 26,
        #             'max_number_of_shots': 65536, 'max_experiments': 'unlimited',
        #             'description': 'v. 1.17', 'accuracy': '0',
        #             'remaining_jobs': 'unlimited'}
        # self.rigetti_backends = [backend1, backend2]

        if self.__qiskit is not None:
            IBMQ.enable_account(self.__qiskit)
            ibm_provider = IBMQ.get_provider('ibm-q')
            self.ibm_backends = ibm_provider.backends()

        if self.__inspire is not None:
            enable_account(self.__inspire)
            QI.set_authentication()
            server_url = r'https://api.quantum-inspire.com'
            auth = get_token_authentication()
            self.qi = QuantumInspireAPI(server_url, auth)
            # backends = []
            # for back in self.qi.get_backend_types():
            #     current_backend = {'name': back.get('name'), 'number_of_qubits': back.get('number_of_qubits'),
            #                        'max_number_of_shots': back.get('max_number_of_shots'),
            #                        'max_experiments': back.get('max_number_of_simultaneous_jobs'),
            #                        'description': back.get('description'), 'accuracy': 'not measured',
            #                        'remaining_jobs': back.get('max_number_of_simultaneous_jobs')}
            #     backends.append(current_backend)
            self.inspire_backends = self.qi.get_backend_types()

        if self.ibm_backends is not None:
            for backend in self.ibm_backends:
                self.tab_infos.append(ComputerInfoTab(backend))

        # if self.inspire_backends is not None:
        #     for backend in self.inspire_backends:
        #         # server = r'https://api.quantum-inspire.com'
        #         # email = 'a.grigorovich@g.nsu.ru'
        #         # password = 'BuHao2L0gyaF%S%'
        #         # auth = HTTPBasicAuth(email, password)
        #         #
        #         # result = requests.get(f'{server}/projects', auth=auth)
        #         #
        #         # print(f'result status: {result.status_code}')
        #         # print(result.json())
        #         # server = r'https://api.quantum-inspire.com'
        #         # auth = coreapi.auth.TokenAuthentication(self.__inspire)
        #         # client = coreapi.Client(auth=auth)
        #         # content = client.get(f'{server}/backendtypes/1/calibration')
        #         # print(content)
        #         # action = ['projects', 'list']
        #         # result = client.action(schema, action)
        #         # print(result)
        #         # server = r'https://api.quantum-inspire.com'
        #         # email = 'a.grigorovich@g.nsu.ru'
        #         # password = 'BuHao2L0gyaF%S%'
        #         # auth = HTTPBasicAuth(email, password)
        #         # contents = urllib.request.urlopen(r'https://api.quantum-inspire.com/backendtypes/1/calibration/',).read()
        #         self.tab_infos.append(ComputerInfoTab(backend._QuantumInspireBackend__backend))

        if self.inspire_backends is not None:
            for backend in self.inspire_backends:
                self.tab_infos.append(ComputerInfoTab(backend))

        if self.rigetti_backends is not None:
            for backend in self.rigetti_backends:
                self.tab_infos.append(ComputerInfoTab(backend))

        self.tab_widget = QtWidgets.QTabWidget(self.central_widget)
        self.tab_widget.setObjectName("tabWidget")

        self.tab_widget.addTab(self.test_info_tab.tab, "")
        self.tab_widget.setTabText(self.tab_widget.indexOf(self.test_info_tab.tab),
                                   _translate('main_window', self.test_info_tab.name))

        self.tab_widget.addTab(self.results_info_tab.tab, "")
        self.tab_widget.setTabText(self.tab_widget.indexOf(self.results_info_tab.tab),
                                   _translate('main_window', self.results_info_tab.name))

        for tab_info in self.tab_infos:
            self.tab_widget.addTab(tab_info.tab, "")
            self.tab_widget.setTabText(self.tab_widget.indexOf(tab_info.tab),
                                       _translate("main_window", tab_info.tab.objectName()))

        self.gridLayout_2.addWidget(self.tab_widget, 0, 0, 1, 1)
        self.tab_widget.setCurrentIndex(0)
