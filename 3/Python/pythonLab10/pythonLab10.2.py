class Table:
    
    def __init__(self, l, w, h):
        self.long = l
        self.width = w
        self.height = h

    def outing(self):
        print(self.long, self.width, self.height)


class Kitchen(Table):

    def howplaces(self, n):
        if n < 2:
            print("It is not kitchen table")
        else:
            self.places = n

    def outplaces(self):
        print(self.places)


class Alive(Table):
    
    def getSquare(self):
        return self.width*self.long

    def grow(self, l, w):
        self.long += l
        self.width += w
        print('Table is evolving....')

t_room1 = Kitchen(2, 1, 0.5)
t_room1.outing()
t_room1.howplaces(5)
t_room1.outplaces()

t_2 = Alive(1, 2, 0.5)
print('current table square = ' + str(t_2.getSquare()))
t_2.grow(1, 1)
print('new table square = ' + str(t_2.getSquare()))


