import numpy as np


print('import numpy as np')
print()
a = np.zeros(10, dtype = int)
print('Task1 Code:\na = np.zeros(10, dtype = int)\nprint(a)\nResult:',a)
print()
a = np.zeros(10, dtype = int)
a[3] = 1
print('Task2 Code:\na = np.zeros(10, dtype = int)\na[3] = 1\nprint(a)\nResult:',a)
print()
a = np.full(10, 14.8)
print('Task3 Code:\na = np.full(10, 14.8)\nprint(a)\nResult:',a)
print()
a = np.arange(23, 39, dtype = int)
print('Task4 Code:\na = np.arange(23, 39, dtype = int)\nprint(a)\nResult:',a)
print()
a = np.flip(a)
print('Task5 Code:\na = np.arange(23, 39, dtype = int)\na = np.flip(a)\nprint(a)\nResult:',a)
print()
a = np.arange(23, 32)
a = a.reshape((3, 3))
print('Task6 Code:\na = np.arange(23, 32)\na = a.reshape((3, 3))\nprint(a)\nResult:\n',a)
print()
a = np.arange(0, 9).reshape((3, 3))
print('Task7 Code:\na = np.arange(0, 9).reshape((3, 3))\nResult:\n',a)
print()
a = np.array([1, 2, 0, 0, 4, 0], int)
b = a.nonzero()
print('Task8 Code:\na = np.array([1, 2, 0, 0, 4, 0], int)\nb = a.nonzero()\nprint(b)\nResult:',b)
print()
a = np.random.random((10,10))
minA = a.min()
maxA = a.max()
print('Task9 Code:\na = np.random.random((10,10))\nminA = a.min()\nmaxA = a.max()\nprint(''min ='', minA)\nprint(''max ='', maxA)\nResult:')
print('min =', minA)
print('max =', maxA)
print()
a = np.random.random(15)
indexMinA = a.argmin()
indexMaxA = a.argmax()
print('Task10 Code:\na = np.random.random(15)\nindexMinA = a.argmin()\nindexMaxA = a.argmax()\nprint(''index of min element ='', indexMinA)\nprint(''index of max element ='', indexMaxA)\nResult:')
print('index of min element =', indexMinA)
print('index of max element =', indexMaxA)
print()
a = np.random.random(30)
print('Task11 Code:\na = np.random.random(30)\nprint(a.mean())\nResult:',a.mean())
print()
a = np.random.random((3, 10))
b = np.mean(a, axis = 0)
c = np.mean(a, axis = 1)
print('Task12 Code:\na = np.random.random((3, 10))\nb = np.mean(a, axis = 0)\nc = np.mean(a, axis = 1)\nprint(b)\nprint(c)\nResult:\n',b,'\n',c)
print()
a = np.array([[1, 1, 1], [1, 1, 1]], int)
def pad_with(vector, pad_width, iaxis, kwargs):
    pad_value = kwargs.get('padder', 10)
    vector[:pad_width[0]] = pad_value
    vector[-pad_width[1]:] = pad_value
    return vector
a = np.pad(a, 1, pad_with, padder = 0)
print('Task13 Code:\na = np.array([[1, 1, 1], [1, 1, 1]], int)\ndef pad_with(vector, pad_width, iaxis, kwargs):\n    pad_value = kwargs.get(''padder'', 10)')
print('    vector[:pad_width[0]] = pad_value\n    vector[-pad_width[1]:] = pad_value\n    return vector\na = np.pad(a, 1, pad_with, padder = 0)\nprint(a)\nResult:\n',a)
print()
a = np.random.random((5, 3))
b = np.random.random((3, 2))
print('Task14 Code:\na = np.random.random((5, 3))\nb = np.random.random((3, 2))\nprint(np.dot(a,b))\nResult:\n',np.dot(a,b))
print()
a = np.random.randint(3, size = 10)
b = np.random.randint(5, size = 12)
print('Task15 Code:\na = np.random.randint(3, size = 10)\nb = np.random.randint(5, size = 12)\nprint(np.intersect1d(a, b))\nResult:',np.intersect1d(a, b))
print()
a = np.random.randint(10, size = 10)
print('Task16 Code:\na = np.random.randint(10, size = 10)\nprint(a)\na.sort()\nprint(a)\nResult:')
print(a)
a.sort()
print(a)
print()
a = np.random.randint(15, size = 10)
print('Task17 Code:\na = np.random.randint(15, size = 10)\nprint(a)\nnp.place(a, a == a.max(), 0)\nprint(a)\nResult:')
print(a)
np.place(a, a == a.max(), 0)
print(a)
print()
a = np.random.randint(10, size = (3,3))
print('Task18 Code:\na = np.random.randint(10, size = (3,3))\nprint(a)\nprint(a.flatten())\nprint(a.reshape(1, 9))\nResult:')
print(a)
print(a.flatten())
print(a.reshape(1, 9))
print()
a = np.random.randint(15, size = (4,4))
print('Task19 Code:\na = np.random.randint(15, size = (4,4))\nprint(a)\nprint(a - a.max())\nResult:\n',a, '\n', a - a.max())
print()
a = np.random.random((2,2))
print('Task20 Code:\na = np.random.random((2,2))\nprint(a)\na.tofile(''pythonLab4Task20.txt'')\nb = np.fromfile(''pythonLab4Task20.txt'').reshape((2,2))\nprint(b * 2)\nResult:')
print(a)
a.tofile('pythonLab4Task20.txt')
b = np.fromfile('pythonLab4Task20.txt').reshape((2,2))
print(b * 2)


