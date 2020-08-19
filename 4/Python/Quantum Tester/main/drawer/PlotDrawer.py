from qiskit.visualization import plot_histogram


class PlotDrawer(object):

    def plot_circuit(self, tab, circuit):
        tab.axes.cla()
        circuit.draw(output='mpl', ax=tab.axes)
        tab.canvas.draw()
        tab.figure.canvas.draw()

    def plot_results2(self, tab, data):
        axes = tab.figure.add_subplot(111)
        axes.clear()
        plot_histogram(data, ax=axes)
        # for tick in axes.get_xticklabels():
        #     tick.set_rotation(0)
        tab.canvas.draw()

    def plot_results(self, tab, test):
        def plot():
            axes = tab.figure.add_subplot(111)
            axes.clear()
            plot_histogram(test.result.get_counts(test.circuit), ax=axes)
            tab.canvas.draw()
            tab.canvas.print_png('c:\\Users\\grig_\\PycharmProjects\\untitled\\lastTest.png')

        return plot
