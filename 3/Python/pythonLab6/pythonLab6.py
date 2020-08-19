import sys, xml.etree.ElementTree as ET
import collections


###TASK6.1.1###
count = collections.Counter()
params = []
notIncluded = ['PNCT', 'PRCL', 'PREP', 'CONJ']
tree = ET.parse('OpenCorpora.xml')
root = tree.getroot()
for attr in root.iter('token'):
    name = attr.get('text').lower()
    for subAtt in attr.iter('g'):
        wordType = subAtt.get('v')
	params.append(wordType)
    result = not any(elem in params for elem in notIncluded)
    params.clear()
    if (result):
        count[name] += 1
print(count.most_common(20))
###TASK6.1.1###


###TASK6.1.2###
count = collections.Counter()
params = []
notIncluded = ['PNCT', 'PRCL', 'PREP', 'CONJ']
tree = ET.parse('OpenCorpora.xml')
root = tree.getroot()
for attr in root.iter('token'):
    name = attr.get('text').lower()
    for subAtt in attr.iter('g'):
        wordType = subAtt.get('v')
	params.append(wordType)
        result = not any(elem in params for elem in notIncluded)
        params.clear()
	if (result):
            count[name] += 1
#print(count.most_common()[-20:])
print (list(reversed(count.most_common()[-20:])))
###TASK6.1.2###


###TASK6.1.3###
count = collections.Counter()
params = []
notIncluded = ['PNCT', 'PRCL', 'PREP', 'CONJ']
tree = ET.parse('OpenCorpora.xml')
root = tree.getroot()
for attr in root.iter('token'):
    name = attr.get('text').lower()
    for subAtt in attr.iter('g'):
        wordType = subAtt.get('v')
	params.append(wordType)
        result = not any(elem in params for elem in notIncluded)
        params.clear()
	if (result):
            count[name] += 1            
longest = max(count.items(), key=lambda s: len(s[0]))
print (longest)
###TASK6.1.3###


###TASK6.1.4###
phraseLen = 2
curLen = 0
phrase = ''
count = collections.Counter()
params = []
notIncluded = ['PNCT', 'PRCL', 'PREP', 'CONJ']
tree = ET.parse('OpenCorpora.xml')
root = tree.getroot()
for attr in root.iter('token'):
    name = attr.get('text').lower()
    for subAtt in attr.iter('g'):
        wordType = subAtt.get('v')
        params.append(wordType)
    result = not any(elem in params for elem in notIncluded)
    params.clear()
    if (result):
        phrase += name
        phrase += ' '
        curLen += 1
        curLen %= phraseLen
        if (curLen == 0):
            count[phrase] += 1
            listphr = phrase.split()
            phrase = listphr[1] + ' '
            curLen = 1
print(count.most_common(20))
###TASK6.1.4###


###TASK6.1.5###
text = ''
params = []
notIncluded = ['PNCT', 'PRCL', 'PREP', 'CONJ']
tree = ET.parse('OpenCorpora.xml')
root = tree.getroot()
for attr in root.iter('token'):
    name = attr.get('text').lower()
    for subAtt in attr.iter('g'):
        wordType = subAtt.get('v')
        params.append(wordType)
    result = not any(elem in params for elem in notIncluded)
    params.clear()
    if (result):
        text += name
        text += ' '
print(len(Counter(text.split())))
###TASK6.1.5###


###TASK6.1.6###
text = ''
params = []
notIncluded = ['PNCT', 'PRCL', 'PREP', 'CONJ']
tree = ET.parse('OpenCorpora.xml')
root = tree.getroot()
for attr in root.iter('token'):
    name = attr.get('text').lower()
    for subAtt in attr.iter('g'):
        wordType = subAtt.get('v')
        params.append(wordType)
    result = not any(elem in params for elem in notIncluded)
    params.clear()
    if (result):
        text += name
        text += ' '
print(len(text.split()))
###TASK6.1.6###


###TASK6.2.1###
import collections
import nltk
import json
import operator

count = collections.Counter()
notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(1):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                says = str(data["acts"][i]["scenes"][j]["action"][k]["says"])
                tokens = nltk.word_tokenize(says)
                tagged = nltk.pos_tag(tokens)
                for u in range(0, len(tagged)):
                    if not any(x in tagged[u] for x in notIncluded):
                        count[(str(tagged[u]).split(',')[0])[1:]] += 1
print(count.most_common(20)) 
###TASK6.2.1###


###TASK6.2.2###
count = collections.Counter()
notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(1):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                says = str(data["acts"][i]["scenes"][j]["action"][k]["says"])
                tokens = nltk.word_tokenize(says)
                tagged = nltk.pos_tag(tokens)
                for u in range(0, len(tagged)):
                    #print(tagged[u])
                    if not any(x in tagged[u] for x in notIncluded):
                        count[(str(tagged[u]).split(',')[0])[1:]] += 1
print (list(reversed(count.most_common()[-20:])))
###TASK6.2.2###


###TASK6.2.3###
count = collections.Counter()
notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(1):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                says = str(data["acts"][i]["scenes"][j]["action"][k]["says"])
                tokens = nltk.word_tokenize(says)
                tagged = nltk.pos_tag(tokens)
                for u in range(0, len(tagged)):
                    if not any(x in tagged[u] for x in notIncluded):
                        count[(str(tagged[u]).split(',')[0])[1:]] += 1
longest = max(count.items(), key=lambda s: len(s[0]))
print (longest)
###TASK6.2.3###


###TASK6.2.4###
phraseLen = 2
curLen = 0
phrase = ''
count = collections.Counter()
notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(1):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                says = str(data["acts"][i]["scenes"][j]["action"][k]["says"])
                tokens = nltk.word_tokenize(says)
                tagged = nltk.pos_tag(tokens)
                for u in range(0, len(tagged)):
                    if not any(x in tagged[u] for x in notIncluded):
                        phrase += (str(tagged[u]).split(',')[0])[1:]
                        phrase += ' '
                        curLen += 1
                        curLen %= phraseLen
                        if (curLen == 0):
                    		count[phrase] += 1
                    		listphr = phrase.split()
                    		phrase = listphr[1] + ' '
                    		curLen = 1
print(count.most_common(20))
###TASK6.2.4###


###TASK6.2.5###
from collections import Counter
import nltk
import json
import operator


text = ''
notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(1):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                says = str(data["acts"][i]["scenes"][j]["action"][k]["says"])
                tokens = nltk.word_tokenize(says)
                tagged = nltk.pos_tag(tokens)
                for u in range(0, len(tagged)):
                    if not any(x in tagged[u] for x in notIncluded):
                        text += (str(tagged[u]).split(',')[0])[1:]
                        text += ' '
print(len(Counter(text.split())))
###TASK6.2.5###


###TASK6.2.6###
text = ''
notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(1):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                says = str(data["acts"][i]["scenes"][j]["action"][k]["says"])
                tokens = nltk.word_tokenize(says)
                tagged = nltk.pos_tag(tokens)
                for u in range(0, len(tagged)):
                    if not any(x in tagged[u] for x in notIncluded):
                        text += (str(tagged[u]).split(',')[0])[1:]
                        text += ' '
print(len(text.split()))
###TASK6.2.6###


###TASK6.3.1###
count = collections.Counter()
notIncluded = ['NONLEX', 'PART', 'PR', 'NUM=ciph', 'CONJ']
with open('Avito.csv', 'r', encoding = 'utf-8') as csvIN:
    reader = csv.DictReader(csvIN)
    for row in reader:
        says = str(row['Title'] + ' ' + row['Description'])
        tokens = nltk.word_tokenize(says)
        tagged = nltk.pos_tag(tokens, lang = 'rus')
        for u in range(0, len(tagged)):
            if not any(x in tagged[u] for x in notIncluded):
                count[(str(tagged[u]).split(',')[0])[1:]] += 1
print(count.most_common(20))  
###TASK6.3.1###


###TASK6.3.2###
count = collections.Counter()
#notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
notIncluded = ['NONLEX', 'PART', 'PR', 'NUM=ciph', 'CONJ']
with open('testLab6.csv', 'r', encoding = 'utf-8') as csvIN:
    reader = csv.DictReader(csvIN)
    for row in reader:
        says = str(row['Title'] + ' ' + row['Description'])
        tokens = nltk.word_tokenize(says)
        tagged = nltk.pos_tag(tokens, lang = 'rus')
        for u in range(0, len(tagged)):
            if not any(x in tagged[u] for x in notIncluded):
                count[(str(tagged[u]).split(',')[0])[1:]] += 1
print (list(reversed(count.most_common()[-20:]))) 
###TASK6.3.2###


###TASK6.3.3###
count = collections.Counter()
#notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
notIncluded = ['NONLEX', 'PART', 'PR', 'NUM=ciph', 'CONJ']
with open('testLab6.csv', 'r', encoding = 'utf-8') as csvIN:
    reader = csv.DictReader(csvIN)
    for row in reader:
        says = str(row['Title'] + ' ' + row['Description'])
        tokens = nltk.word_tokenize(says)
        tagged = nltk.pos_tag(tokens, lang = 'rus')
        for u in range(0, len(tagged)):
            if not any(x in tagged[u] for x in notIncluded):
                count[(str(tagged[u]).split(',')[0])[1:]] += 1
longest = max(count.items(), key=lambda s: len(s[0]))
print (longest) 
###TASK6.3.3###


###TASK6.3.4###
phraseLen = 2
curLen = 0
phrase = ''
count = collections.Counter()
#notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
#notIncluded = ['NONLEX', 'PART', 'PR', 'NUM=ciph', 'CONJ']
notIncluded = ['NONLEX', 'NUM=ciph'] 
with open('testLab6.csv', 'r', encoding = 'utf-8') as csvIN:
    reader = csv.DictReader(csvIN)
    for row in reader:
        says = str(row['Title'] + ' ' + row['Description'])
        tokens = nltk.word_tokenize(says)
        tagged = nltk.pos_tag(tokens, lang = 'rus')
        for u in range(0, len(tagged)):
            if not any(x in tagged[u] for x in notIncluded):
                phrase += (str(tagged[u]).split(',')[0])[1:]
                phrase += ' '
                curLen += 1
                curLen %= phraseLen
                if (curLen == 0):
                    count[phrase] += 1
                    listphr = phrase.split()
                    phrase = listphr[1] + ' '
                    curLen = 1
print(count.most_common(20))   
###TASK6.3.4###


###TASK6.3.5###
text = ''
#count = collections.Counter()
#notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
#notIncluded = ['NONLEX', 'PART', 'PR', 'NUM=ciph', 'CONJ']
notIncluded = ['NONLEX', 'NUM=ciph'] 
with open('testLab6.csv', 'r', encoding = 'utf-8') as csvIN:
    reader = csv.DictReader(csvIN)
    for row in reader:
        says = str(row['Title'] + ' ' + row['Description'])
        tokens = nltk.word_tokenize(says)
        tagged = nltk.pos_tag(tokens, lang = 'rus')
        for u in range(0, len(tagged)):
            if not any(x in tagged[u] for x in notIncluded):
                text += (str(tagged[u]).split(',')[0])[1:]
                text += ' '
print(len(Counter(text.split())))
###TASK6.3.5###


###TASK6.3.6###
text = ''
#count = collections.Counter()
#notIncluded = ['CC', 'CD', 'IN', 'RP', 'SYM', 'TO', 'DT', '[', ']', '``', ',', '.', "''", "'", 'FW', ':', "'s", "'d", "'ll"]#
#notIncluded = ['NONLEX', 'PART', 'PR', 'NUM=ciph', 'CONJ']
notIncluded = ['NONLEX', 'NUM=ciph'] 
with open('testLab6.csv', 'r', encoding = 'utf-8') as csvIN:
    reader = csv.DictReader(csvIN)
    for row in reader:
        says = str(row['Title'] + ' ' + row['Description'])
        tokens = nltk.word_tokenize(says)
        tagged = nltk.pos_tag(tokens, lang = 'rus')
        for u in range(0, len(tagged)):
            if not any(x in tagged[u] for x in notIncluded):
                text += (str(tagged[u]).split(',')[0])[1:]
                text += ' '
print(len(text.split())) 
###TASK6.3.6###


###TASK6.4.1###
fieldNamesNew = ['Title', 'Price']
with open('Avito.csv', 'r', encoding = 'utf-8') as csvIN, open('pythonLab6Task3.csv', 'w', encoding = 'utf-8') as csvOUT:
    reader1 = csv.reader(csvIN, None)
    writer = csv.writer(csvOUT)
    first_line = next(reader1)
    first_line = ['Title', 'Price']
    #csv_writer = csv.writer(csvOUT)
    sortedList = sorted(reader1, key=lambda row: float(row[4]), reverse=True)
    if first_line:
        writer.writerow(first_line)   
    for row in sortedList:
        writer.writerow(row[2:3] + row[4:5])
###TASK6.4.1###


###TASK6.4.2###
fieldNamesNew = ['Title', 'Price']
with open('Avito.csv', 'r', encoding = 'utf-8') as csvIN, open('pythonLab6Task3.csv', 'w', encoding = 'utf-8') as csvOUT:
    reader1 = csv.reader(csvIN, None)
    writer = csv.writer(csvOUT)
    first_line = next(reader1)
    first_line = ['Title', 'Price']
    sortedList = sorted(reader1, key=lambda row: float(row[4]), reverse=False)
    if first_line:
        writer.writerow(first_line)
    for row in sortedList:
        writer.writerow(row[2:3] + row[4:5])
###TASK6.4.2###
