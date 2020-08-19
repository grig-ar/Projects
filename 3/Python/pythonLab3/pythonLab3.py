import csv


###Task3.1###
f = open('pythonLab3Task1.csv', 'w', encoding = 'utf-8')
fieldNames = ['Id', 'Images', 'Title', 'Description', 'Price']
with open('pythonLab3.csv', encoding = 'utf-8') as csvfile:
    writer = csv.DictWriter(f, fieldnames = fieldNames)
    reader = csv.DictReader(csvfile)
    writer.writeheader()
    for row in reader:
        if (row['Images'].count(',') > 2):
            writer.writerow(row);
f.close();
###Task3.1###


###Task3.2###
f = open('pythonLab3Task2.csv', 'w', encoding = 'utf-8')
fieldNames = ['Id', 'Images', 'Title', 'Description', 'Price']
with open('pythonLab3.csv', encoding = 'utf-8') as csvfile:
    writer = csv.DictWriter(f, fieldnames = fieldNames)
    reader = csv.DictReader(csvfile)
    writer.writeheader()
    for row in reader:
        if (10000.0 < float(row['Price']) <= 50000.0):
            writer.writerow(row);
f.close();
###Task3.2###


###Task3.3###
fieldNamesNew = ['Id', 'Title', 'Price']
with open('pythonLab3.csv', 'r', encoding = 'utf-8') as csvIN, open('pythonLab3Task3.csv', 'w', encoding = 'utf-8') as csvOUT:
    reader = csv.DictReader(csvIN)
    writer = csv.DictWriter(csvOUT, fieldNamesNew, extrasaction = 'ignore')
    writer.writeheader()
    for row in reader:
        writer.writerow(row)
###Task3.3###