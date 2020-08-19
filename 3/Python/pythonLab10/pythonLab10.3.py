class Figure:

    def __init__(self, c='White'):
        self.color = c

    def setColor(self, c):
        self.color = c
    
    def getColor(self):
        return self.color

    def getDescription(self):
        pass


class Oval(Figure):

    def __init__(self, minAxis, maxAxis, color='White'):
        self.minAxis = minAxis
        self.maxAxis = maxAxis
        self.color = color
    
    def getDescription(self):
        print("I'm " + self.color + ' ' + self.__class__.__name__)
        print('My minAxis = ' + str(self.minAxis))
        print('My maxAxis = ' + str(self.maxAxis))
        print()


class Square(Figure):

    def __init__(self, side, color='White'):
        self.sideLen = side
        self.color = color

    def getDescription(self):
        print("I'm " + self.color + ' ' + self.__class__.__name__)
        print('My side length = ' + str(self.sideLen))
        print()



oval1 = Oval(3, 4)
oval2 = Oval(4, 5, 'Cyan')
square1 = Square(6)
square2 = Square(7, 'Red')

oval1.getDescription()
oval2.getDescription()
square1.getDescription()
square2.getDescription()


