import abc
import qiskit as q


class ICircuit(metaclass=abc.ABCMeta):

    @abc.abstractmethod
    def add_circuit(self, circuit: q.QuantumCircuit):
        raise NotImplementedError
