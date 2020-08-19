import unittest
import numpy as np

def disc(a,b,c):
    d = b*b - 4*a*c
    return d

def average(*args):
    return(sum(args)/len(args))

def checkNum(*args):
    for ar in args:
        if not (ar.isnumeric()):
            return False
    return True

def rmNegative(arr):
    for a in arr:
        if (a < 0):
            arr[arr.index(a)] = 0
    return arr

def fillArray(arr, elem, count):
    for i in range(0, count):
        arr.append(elem)
    return arr

def replaceInArray(arr, old, new):
    for a in arr:
        arr[arr.index(a)] = arr[arr.index(a)].replace(old, new)
    return arr

def sumArray(arr):
    b = []
    for i in range(0, len(arr)-1):
        b.append( arr[i] + arr[i+1] )
    return b

def isDateIn(s):
    arr = s.split(' ')
    months = ['январь', 'января', 'январе', 'январем', 'февраль', 'февраля', 'феврале', 'февралем', 'март', 'марта', 'марте', 'мартовским','апрель', 'апреля', 'апреле', 'апрелем',
               'май', 'мая', 'мае', 'маем', 'июнь', 'июня', 'июне', 'июнем', 'июль', 'июля', 'июле', 'июлем', 'август', 'августа', 'августе', 'августовским'
               'сентябрь', 'сентября', 'сентябре', 'сентябрьским', 'октябрь', 'октября', 'октябре', 'октябрьским', 'ноябрь', 'ноября', 'ноябре', 'ноябрьским', 'декабрь', 'декабря', 'декабре', 'декабрьским']
    seasons = ['зимой', 'весной', 'летом', 'осенью']
    #print(arr)
    for i in range(0, len(arr)):
        if ((i > 0) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and (arr[i] == 'году')):
            return True
        if ((i > 0) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and (arr[i] == 'год')):
            return True
        if ((i > 0) and (arr[i-2] in seasons) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and (arr[i] == 'года')):
            return True
        if ((i > 0) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and (arr[i] == 'веке')):
            return True
        #if ((i > 0) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and (arr[i] == 'лет')):
            #return True
        if ((i > 0) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and (arr[i] == 'г')):
            return True
        if ((i > 0) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and ((i + 2) <= len(arr)) and (arr[i] == 'до') and (arr[i+1] == 'нашей') and (arr[i+2] == 'эры')):
            return True
        if ((arr[i].isnumeric()) and (int(arr[i]) > 0) and ((i + 3) <= len(arr)) and (arr[i+1] == 'до') and (arr[i+2] == 'нашей') and (arr[i+3] == 'эры')):
            return True
        if ((i > 0) and (arr[i-1].isnumeric()) and (int(arr[i-1]) > 0) and (arr[i] in months)):
            return True
        if (arr[i] in months):
            return True
    return False
class TestFunctions(unittest.TestCase):
    def test_disc(self):
        a = disc(2, -5, 4)
        self.assertGreater(a, -10)
    def test_average(self):
        b = average(1, 5, 0)
        self.assertLess(b, 4.0)
    def test_checkNum(self):
        self.assertTrue(checkNum('1', '2', '3'))
    def test_rmNegative(self):
        arr = [-1, 0, 1]
        arr = rmNegative(arr)
        self.assertNotIn(-1, arr)
    def testfillArray(self):
        a = []
        a = fillArray(a, 5, 5)
        self.assertIn(5, a)
    def test_replaceInArray(self):
        a = ['abcd', 'qwaberty']
        a = replaceInArray(a, 'ab', 'ss')
        self.assertFalse('ab' in a)
    def test_sumArray(self):
        a = [0, 1, 2]
        b = sumArray(a)
        c = [1, 3]
        self.assertCountEqual(b, c)
    def test_oneDigit(self):
        n = int(input('Enter n: '))
        a = np.random.randint(9, size = n)
        for i in range(0, n):
            with self.subTest(i=i):
                self.assertLess(a[i], 10)
    def test_isDateIn(self):
        test0 = 'я родился в 1990 году'
        test1 = 'с тех пор прошло 3 года'
        test2 = 'бюджет в 2018 году'
        test3 = 'в 6 веке'
        test4 = '5 марта'
        test5 = 'почти половина мужчин не доживает до 65 лет'
        test6 = 'в 1897 г'
        test7 = '1978 до нашей эры'
        test8 = 'в мае 2018 года'
        self.assertTrue(isDateIn(test0))
        self.assertFalse(isDateIn(test1))
        self.assertTrue(isDateIn(test2))
        self.assertTrue(isDateIn(test3))
        self.assertTrue(isDateIn(test4))
        self.assertFalse(isDateIn(test5))
        self.assertTrue(isDateIn(test6))
        self.assertTrue(isDateIn(test7))
        self.assertTrue(isDateIn(test8))
unittest.main()
