import math
import collections
import pickle
import pprint


class TFIDFCounter:
    
    def countTFIDF(self, textArr, fileName):
        wordsAmount = collections.Counter()
        textsAmount = len(textArr)
        allTextsWords = []
        allTermFrequency = []
        termAppearance = {}
        IDF = self.__loadIDF(fileName)
        for text in textArr:
            parsedText = text.split()
            allTextsWords.append(parsedText)
            textLength = len(parsedText)
            for word in parsedText:   
                wordsAmount[word] += 1
            termFrequency = {}
            for key, value in wordsAmount.items():
                termFrequency[key] = value/textLength
                termAppearance.update({key : termAppearance.get(key, 0) + 1})
            allTermFrequency.append(termFrequency)
            
        if (not bool(IDF)):
            for key, value in termAppearance.items():
                #print(key, value)
                IDF[key] = math.log(textsAmount/int(value),10)
            self.__saveIDFToFile(IDF, fileName)
            
        for i in range(0, textsAmount):
            TFIDF = {}
            text = allTextsWords[i]
            TF = allTermFrequency[i]
            for word in text:
                tf_idf = TF[word] * IDF[word]
                TFIDF[word] = tf_idf
            self.__saveTFIDFToFile(TFIDF, fileName, i)

                
    def __loadIDF(self, fileName):
        try:
            idfIN = open(fileName + '.idf', 'rb')
        except:
            return {}
        return pickle.load(idfIN)


    def __saveIDFToFile(self, file, fileName):
        with open(fileName + '.idf', 'wb') as idfOUT:
            pickle.dump(file, idfOUT, pickle.HIGHEST_PROTOCOL)


    def __saveTFIDFToFile(self, file, fileName, text_id):
        with open(fileName + '.tfidf', 'a', encoding='latin-1') as idfOUT:
            idfOUT.write('###' + str(text_id) + '###\n')
            
            for key, value in file.items():
                idfOUT.write(key + ' = ' + str(value) + '\n')

texts = []
fileName = 'HP'
for i in range(1,8):
    with open(fileName + str(i) + '.txt', 'r', encoding = 'latin-1') as txtIN:
        text = txtIN.read().replace('\n', '').lower()
        texts.append(text)
        
myCount = TFIDFCounter()
myCount.countTFIDF(texts, fileName)
    

    










#with open('HP7.txt', 'r', encoding = 'latin-1') as txtIN, open('HP7_n.txt', 'w', encoding = 'latin-1') as txtOUT:
#    text = txtIN.read()
#    #print(text)
#    for ch in [',', '.', '!', '?', '…', '«', '»', '*', ':', ';', '—', '(', ')']:
#        text = text.replace(ch, '')
#    text = text.replace('-', ' ')
#    txtOUT.write(text)
#'…',
