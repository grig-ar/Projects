import xml.etree.ElementTree as eTree


class Word:
    
    def __init__(self, word_id, text, params):
        self.wordID = word_id
        self.text = text
        self.params = params

    def get_word_id(self):
        return self.wordID

    def get_text(self):
        return self.text


class Sentence:
    words = {}

    def __init__(self):
        self.words = {}

    def add_word(self, word_id, word):
        self.words.update({word_id: word})

    def print_sentence(self):
        for key, value in self.words.items():
            print(value.get_text(), end=' ')

    def get_word_by_id(self, word_id):
        return self.words.get(word_id)

    def print_word_by_id(self, word_id):
        print(self.words.get(word_id).get_text())


class Corpus:
    sentences = {}

    def add_sentence(self, sent_id, sent):
        Corpus.sentences.update({sent_id: sent})

    def get_sentence_by_id(self, sent_id):
        return Corpus.sentences.get(sent_id)

    def print_sentence_by_id(self, sent_id):
        Corpus.sentences.get(sent_id).print_sentence()


sent_id = 0
token_id = 0
params = []
corpus = Corpus()
tree = eTree.parse('OpenCorpora.xml')
root = tree.getroot()
for attr in root:
    for sent in attr.iter('sentence'):
        sentence = Sentence()
        sent_id = sent.get('id')
        for stats in sent.iter('token'):
            t_id = stats.get('id')
            name = stats.get('text')
            for sub_attr in stats.iter('g'):
                word_type = sub_attr.get('v')
                params.append(word_type)
            word = Word(t_id, name, params)
            sentence.add_word(t_id, word)
            params.clear()
        corpus.add_sentence(sent_id, sentence)

corpus.print_sentence_by_id('1')
print()
print(corpus.get_sentence_by_id('1').get_word_by_id('2').get_text())
corpus.get_sentence_by_id('1').print_word_by_id('3')
