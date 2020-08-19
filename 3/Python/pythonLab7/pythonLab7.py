###TASK7.1###
import time


while(True):
    i = float(input("Enter timeout: "))
    if (i < 0):
        break
    else:
        time.sleep(i)
###TASK7.1###


###TASK7.2###
import time
import datetime


t = time.perf_counter_ns()
a = 1958753 % 12387
t = time.perf_counter_ns() - t
fName = str(datetime.datetime.now()).replace(':', '_')
f = open(fName, 'a')
f.write(str(t))
f.close()
###TASK7.2###


###TASK7.3###
import time
import datetime


date_entry = input('Enter a date in DD-MM-YYYY format: ')
day, month, year = map(int, date_entry.split('-'))
date1 = datetime.date(year, month, day)
print((datetime.datetime.now().date() - date1).days)
###TASK7.3###