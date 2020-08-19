from PyQt5.QtCore import QRunnable, pyqtSlot
import traceback
import sys

from main.worker.signal.WorkerSignals2 import WorkerSignals2


class Worker2(QRunnable):
    def __init__(self, fn, *args, **kwargs):
        super(Worker2, self).__init__()
        self.fn = fn
        self.args = args
        self.kwargs = kwargs
        self.signals = WorkerSignals2()

    def autoDelete(self):
        return True

    @pyqtSlot()
    def run(self):
        try:
            result = self.fn(*self.args, **self.kwargs)
        except:
            traceback.print_exc()
            exctype, value = sys.exc_info()[:2]
            self.signals.error.emit((exctype, value, traceback.format_exc()))
        else:
            self.signals.result.emit(result)
        finally:
            print('Signal finished:')
            print((str(self)))
            print((str(self.fn)))
            print((str(self.args)))
            print((str(self.kwargs)))
            self.signals.finished.emit()
