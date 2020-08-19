class Things:
    def __init__(self, n, t):
        self.namething = n
        self.total = t
###NEW1###
    def setColor(self, c):
        self.color = c

    def getColor(self):
        try:
            return self.color
        except:
            return 'No color'
###NEW1###

th1 = Things('table', 5)
th2 = Things('computer', 7)

print(th1.namething, th1.total)
print(th2.namething, th2.total)

#th1.color = 'green'

###NEW2###
th1.setColor('green')
###NEW2###

#print(th1.color)
#print(th2.color)#AttributeError: 'Things' object has no attribute 'color'

###NEW3###
print(th1.getColor())
print(th2.getColor())
###NEW3###
