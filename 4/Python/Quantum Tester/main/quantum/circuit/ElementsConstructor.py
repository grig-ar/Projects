import qiskit as q
from math import pi as PI


class ElementsConstructor(object):

    @staticmethod
    def add_h_gate_to_all(circuit: q.QuantumCircuit):
        for i in range(0, circuit.num_qubits):
            circuit.h(i)

    @staticmethod
    def add_x_gate_to_all(circuit: q.QuantumCircuit):
        for i in range(0, circuit.num_qubits):
            circuit.x(i)

    @staticmethod
    def add_binary_number(circuit: q.QuantumCircuit, binary_number: str):
        qubits_amount = circuit.num_qubits
        number_length = len(binary_number)
        if qubits_amount > 5 or qubits_amount < 2:
            raise NotImplementedError("Only circuits with num_qubits in range [2;5] are implemented")

        for i in range(0, number_length):
            if binary_number[i] == "0":
                circuit.x(number_length - 1 - i)

    @staticmethod
    def add_measure(circuit: q.QuantumCircuit):
        for i in range(0, circuit.num_qubits):
            circuit.measure(i, i)

    @staticmethod
    def add_oracle_element(circuit: q.QuantumCircuit, binary_number=""):
        qubits_amount = circuit.num_qubits
        if qubits_amount == 2:
            if binary_number != "":
                ElementsConstructor.add_binary_number(circuit, binary_number)
            circuit.h(1)
            circuit.cx(0, 1)
            circuit.h(1)
            if binary_number != "":
                ElementsConstructor.add_binary_number(circuit, binary_number)
        elif qubits_amount == 3:
            if binary_number != "":
                ElementsConstructor.add_binary_number(circuit, binary_number)
            circuit.h(2)
            circuit.ccx(0, 1, 2)
            circuit.h(2)
            if binary_number != "":
                ElementsConstructor.add_binary_number(circuit, binary_number)
        elif qubits_amount == 4:
            if binary_number != "":
                ElementsConstructor.add_binary_number(circuit, binary_number)
            circuit.cu1(PI / 4, 0, 3)
            circuit.cx(0, 1)
            circuit.cu1(-PI / 4, 1, 3)
            circuit.cx(0, 1)
            circuit.cu1(PI / 4, 1, 3)
            circuit.cx(1, 2)
            circuit.cu1(-PI / 4, 2, 3)
            circuit.cx(0, 2)
            circuit.cu1(PI / 4, 2, 3)
            circuit.cx(1, 2)
            circuit.cu1(-PI / 4, 2, 3)
            circuit.cx(0, 2)
            circuit.cu1(PI / 4, 2, 3)
            if binary_number != "":
                ElementsConstructor.add_binary_number(circuit, binary_number)
        elif qubits_amount == 5:
            if (binary_number != ""):
                ElementsConstructor.add_binary_number(circuit, binary_number)
            controls = list()
            qubits = circuit.qubits
            controls.append(circuit.qubits[0])
            controls.append(circuit.qubits[1])
            controls.append(circuit.qubits[2])
            circuit.cu1(PI / 4, qubits[3], qubits[4])
            circuit.mcx(controls, qubits[3], None, 'noancilla')
            circuit.cu1(-PI / 4, qubits[3], qubits[4])
            circuit.mcx(controls, qubits[3], None, 'noancilla')
            circuit.mcu1(PI / 4, controls, qubits[4])
            if (binary_number != ""):
                ElementsConstructor.add_binary_number(circuit, binary_number)

    @staticmethod
    def add_amplification_element(circuit: q.QuantumCircuit):
        ElementsConstructor.add_h_gate_to_all(circuit)
        ElementsConstructor.add_x_gate_to_all(circuit)
        ElementsConstructor.add_oracle_element(circuit)
        ElementsConstructor.add_x_gate_to_all(circuit)
        ElementsConstructor.add_h_gate_to_all(circuit)
