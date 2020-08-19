from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg
from PyQt5 import QtCore, QtGui, QtWidgets
from matplotlib.figure import Figure


class MyMplCanvas(FigureCanvasQTAgg):
    """Ultimately, this is a QWidget (as well as a FigureCanvasAgg, etc.)."""

    def __init__(self, circuit, parent=None, width=5, height=4, dpi=60):
        fig = Figure(figsize=(width, height), dpi=dpi)
        self.lastEvent = []
        self.axes = fig.add_subplot(111)
        FigureCanvasQTAgg.__init__(self, fig)
        self.compute_initial_figure(circuit)

        self.setParent(parent)

        FigureCanvasQTAgg.setSizePolicy(self,
                                        QtWidgets.QSizePolicy.Expanding,
                                        QtWidgets.QSizePolicy.Expanding)
        FigureCanvasQTAgg.updateGeometry(self)

    def resizeEvent(self, event):
        if not self.lastEvent:
            # at the start of the app, allow resizing to happen.
            super(MyMplCanvas, self).resizeEvent(event)
        # store the event size for later use
        self.lastEvent = (event.size().width(), event.size().height())
        # print("try to resize, I don't let you.")

    def do_resize_now(self):
        # recall last resize event's size
        newsize = QtCore.QSize(self.lastEvent[0], self.lastEvent[1])
        # create new event from the stored size
        event = QtGui.QResizeEvent(newsize, QtCore.QSize(1, 1))
        # and propagate the event to let the canvas resize.
        super(MyMplCanvas, self).resizeEvent(event)

    def compute_initial_figure(self, circuit):
        self.axes.cla()
        circuit.draw(output='mpl', ax=self.axes)
        self.draw()
