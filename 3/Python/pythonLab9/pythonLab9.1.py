from abc import ABC, abstractmethod
from math import pi as PI
from math import sqrt as Sqrt
from math import sin as Sin
from math import radians as Radians


class Figure(ABC):
    
    @abstractmethod
    def getColor(self):
        pass

    @abstractmethod
    def getSquare(self):
        pass

    @abstractmethod
    def getPerimeter(self):
        pass

    @abstractmethod
    def getDescription(self):
        pass


class Ellipse(Figure):
    
    def __init__(self, color, semiminorAxis, semimajorAxis):
        self.color = color
        self.semiminorAxis = semiminorAxis
        self.semimajorAxis = semimajorAxis
        
    def getColor(self):
        return self.color
    
    def getSquare(self):
        return PI*self.semiminorAxis*self.semimajorAxis
    
    def getPerimeter(self):
        return PI*(3*(self.semiminorAxis + self.semimajorAxis) - Sqrt((3*self.semiminorAxis + self.semimajorAxis)*(self.semiminorAxis + 3*self.semimajorAxis)))

    def getDescription(self):
        print("I'm " + self.color + ' ' + self.__class__.__name__)
        print('My square: ' + "{0:.2f}".format(self.getSquare()))
        print('My perimeter: ' + "{0:.2f}".format(self.getPerimeter()))


class Circle(Ellipse):

    def __init__(self, color, semiminorAxis, semimajorAxis):
        Ellipse.__init__(self, color, semiminorAxis, semimajorAxis)
    
    def getPerimeter(self):
        return 2*PI*self.semiminorAxis


class Polygon(Figure):
    
    def __init__(self, color, sideA, sideB):
        self.color = color
        self.sideA = sideA
        self.sideB = sideB

    def getColor(self):
        return self.color

    def getDescription(self):
        print("I'm " + self.color + ' ' + self.__class__.__name__)
        print('My square: ' + "{0:.2f}".format(self.getSquare()))
        print('My perimeter: ' + "{0:.2f}".format(self.getPerimeter()))


class Triangle(Polygon):
    
    def __init__(self, color, sideA, sideB, sideC, angleAB):
        Polygon.__init__(self, color, sideA, sideB)
        self.sideC = sideC
        self.angleAB = angleAB

    def getPerimeter(self):
        return self.sideA + self.sideB + self.sideC

    def getSquare(self):
        return self.sideA*self.sideB*Sin(Radians(self.angleAB))/2


class Rectangle(Polygon):

    def __init__(self, color, sideA, sideB):
        Polygon.__init__(self, color, sideA, sideB)

    def getPerimeter(self):
        return 2*(self.sideA + self.sideB)

    def getSquare(self):
        return self.sideA*self.sideB


class Box:
    
    figures = []

    def addItem(self, figure):
        if (isinstance(figure, Figure)):
            self.figures.append(figure)
        else:
            print("Can't add element")
    
    def whatsInside(self):
        for figure in self.figures:
            figure.getDescription()
            print()


circle1 = Circle('Red', 5, 5)
ellipse1 = Ellipse('Blue', 10, 4.5)
ellipse2 = Ellipse('Green', 4.5, 1.2)
triangle1 = Triangle('Yellow', 10.4, 13.2, 11.4, 44)
rectangle1 = Rectangle('Magenta', 4, 5)
rectangle2 = Rectangle('Cyan', 3, 3)

box = Box()
box.addItem(circle1)
box.addItem(ellipse1)
box.addItem(ellipse2)
box.addItem(triangle1)
box.addItem(rectangle1)
box.addItem(rectangle2)
box.addItem(5)
box.whatsInside()
