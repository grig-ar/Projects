import math

import qiskit as q
from main.quantum.circuit.ElementsConstructor import ElementsConstructor
from main.quantum.circuit.ICircuit import ICircuit


class GroverCircuit(ICircuit):

    def __init__(self):
        self.constructor = ElementsConstructor()
        self.__number = 0

    def set_number(self, new_number: int):
        self.__number = new_number

    def get_number(self):
        return self.__number

    def add_circuit(self, circuit: q.QuantumCircuit):
        qubits_amount = circuit.num_qubits

        if qubits_amount < 2 or qubits_amount > 5:
            raise q.exceptions.QiskitError("num_qubits must be in range [2;5] for this circuit")

        pi_3_4 = 0.7854
        n = round(math.pow(2, qubits_amount))
        if n <= self.get_number():
            raise RuntimeError("Number is too big")

        if qubits_amount == 2:
            iterations = 1
        else:
            iterations = round(0.1 + pi_3_4 * math.sqrt(n))

        number = self.get_number()
        format_template = ':0={}b'.format(qubits_amount)
        format_template = "{%s}" % format_template
        binary_number = format_template.format(number)

        self.constructor.add_h_gate_to_all(circuit)
        for i in range(0, iterations):
            self.constructor.add_oracle_element(circuit, binary_number=binary_number)
            self.constructor.add_amplification_element(circuit)
        self.constructor.add_measure(circuit)

    @staticmethod
    def get_inspire_circuit(qubits: int, backend_name: str):
        if qubits < 2 or qubits > 5:
            raise q.exceptions.QiskitError("num_qubits must be in range [2;5] for this circuit")

        if qubits == 2:
            if backend_name == 'Starmon-5':
                return '''
                version 1.0
                
                qubits 5
                
                {H q[1] | H q[2]}
                
                H q[2]
                CNOT q[1], q[2]
                H q[2]
                
                {H q[1] | H q[2]}
                {X q[1] | X q[2]}
                
                H q[2]
                CNOT q[1], q[2]
                H q[2]
               
                {X q[1] | X q[2]}
                {H q[1] | H q[2]}
                
                measure_z q[1]
                measure_z q[2]
                '''
            else:
                return '''
                version 1.0
            
                qubits 2
            
                {H q[0] | H q[1]}
            
                H q[1]
                CNOT q[0], q[1]
                H q[1]
            
                {H q[0] | H q[1]}
                {X q[0] | X q[1]}
            
                H q[1]
                CNOT q[0], q[1]
                H q[1]
            
                {X q[0] | X q[1]}
                {H q[0] | H q[1]}
            
                measure_z q[0]
                measure_z q[1]
                '''
        elif qubits == 3:
            return '''
            version 1.0
        
            qubits 3
        
            {H q[0] | H q[1] | H q[2]}
        
            X q[0]
        
            H q[2]
            Toffoli q[0], q[1], q[2]
            H q[2]
        
            X q[0]
        
            {H q[0] | H q[1] | H q[2]}
            {X q[0] | X q[1] | X q[2]}
        
            H q[2]
            Toffoli q[0], q[1], q[2]
            H q[2]
        
            {X q[0] | X q[1] | X q[2]}
            {H q[0] | H q[1] | H q[2]}
        
            X q[0]
        
            H q[2]
            Toffoli q[0], q[1], q[2]
            H q[2]
        
            X q[0]
        
            {H q[0] | H q[1] | H q[2]}
            {X q[0] | X q[1] | X q[2]}
        
            H q[2]
            Toffoli q[0], q[1], q[2]
            H q[2]
        
            {X q[0] | X q[1] | X q[2]}
            {H q[0] | H q[1] | H q[2]}
        
            measure_z q[0]
            measure_z q[1]
            measure_z q[2]
            '''
        elif qubits == 4:
            return '''
            version 1.0
        
            qubits 4
        
            {H q[0] | H q[1] | H q[2] | H q[3]}
        
            {X q[0] | X q[2] | X q[3]}
            RZ q[0], 0.3927
            CNOT q[0], q[3]
            RZ q[3], -0.3927
            CNOT q[0], q[3]
            RZ q[3], 0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            {X q[0] | X q[2] | X q[3]}
        
            {H q[0] | H q[1] | H q[2] | H q[3]}
            {X q[0] | X q[1] | X q[2] | X q[3]}
            RZ q[0], 0.3927
            CNOT q[0], q[3]
            RZ q[3], -0.3927
            CNOT q[0], q[3]
            RZ q[3], 0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            {X q[0] | X q[1] | X q[2] | X q[3]}
            {H q[0] | H q[1] | H q[2] | H q[3]}
        
            {X q[0] | X q[2] | X q[3]}
            RZ q[0], 0.3927
            CNOT q[0], q[3]
            RZ q[3], -0.3927
            CNOT q[0], q[3]
            RZ q[3], 0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            {X q[0] | X q[2] | X q[3]}
        
            {H q[0] | H q[1] | H q[2] | H q[3]}
            {X q[0] | X q[1] | X q[2] | X q[3]}
            RZ q[0], 0.3927
            CNOT q[0], q[3]
            RZ q[3], -0.3927
            CNOT q[0], q[3]
            RZ q[3], 0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            {X q[0] | X q[1] | X q[2] | X q[3]}
            {H q[0] | H q[1] | H q[2] | H q[3]}
        
            {X q[0] | X q[2] | X q[3]}
            RZ q[0], 0.3927
            CNOT q[0], q[3]
            RZ q[3], -0.3927
            CNOT q[0], q[3]
            RZ q[3], 0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            {X q[0] | X q[2] | X q[3]}
        
            {H q[0] | H q[1] | H q[2] | H q[3]}
            {X q[0] | X q[1] | X q[2] | X q[3]}
            RZ q[0], 0.3927
            CNOT q[0], q[3]
            RZ q[3], -0.3927
            CNOT q[0], q[3]
            RZ q[3], 0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[1]
        
            RZ q[1], 0.3927
            CNOT q[1], q[3]
            RZ q[3], -0.3927
            CNOT q[1], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
        
            CNOT q[1], q[2]
        
            RZ q[2], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
        
            CNOT q[0], q[2]
        
            RZ q[2], 0.3927
            CNOT q[2], q[3]
            RZ q[3], -0.3927
            CNOT q[2], q[3]
            RZ q[3], 0.3927
            {X q[0] | X q[1] | X q[2] | X q[3]}
            {H q[0] | H q[1] | H q[2] | H q[3]}
        
            measure_z q[0]
            measure_z q[1]
            measure_z q[2]
            measure_z q[3]
            '''
        elif qubits == 5:
            return '''
    version 1.0

    qubits 5

    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}
    {X q[1] | X q[3]}

    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[1] | X q[3]}

    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}

    {X q[1] | X q[3]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[1] | X q[3]}

    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}

    {X q[1] | X q[3]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[1] | X q[3]}

    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}

    {X q[1] | X q[3]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[1] | X q[3]}

    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}

    {X q[1] | X q[3]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[1] | X q[3]}

    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    RZ q[3], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[3], -0.3927
    CNOT q[3], q[4]
    RZ q[4], 0.3927
    CNOT q[3], q[4]
    RZ q[4], -0.3927

    RZ q[2], 0.3927
    H q[3]
    CNOT q[2], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[3]
    RZ q[3], 0.3927
    CNOT q[2], q[1]
    RZ q[1], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[1]
    RZ q[1], 0.3927
    CNOT q[1], q[3]
    RZ q[3], -0.3927
    CNOT q[1], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[1], q[0]
    RZ q[0], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[2], q[0]
    RZ q[0], 0.3927
    CNOT q[0], q[3]
    RZ q[3], -0.3927
    CNOT q[0], q[3]
    RZ q[3], 0.3927
    H q[3]

    RZ q[2], 0.0982
    CNOT q[2], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[4]
    RZ q[4], 0.0982
    CNOT q[2], q[1]
    RZ q[1], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[1]
    RZ q[1], 0.0982
    CNOT q[1], q[4]
    RZ q[4], -0.0982
    CNOT q[1], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[1], q[0]
    RZ q[0], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[2], q[0]
    RZ q[0], 0.0982
    CNOT q[0], q[4]
    RZ q[4], -0.0982
    CNOT q[0], q[4]
    RZ q[4], 0.0982
    {X q[0] | X q[1] | X q[2] | X q[3] | X q[4]}
    {H q[0] | H q[1] | H q[2] | H q[3] | H q[4]}

    measure_z q[0]
    measure_z q[1]
    measure_z q[2]
    measure_z q[3]
    measure_z q[4]
    '''
