import xml.etree.ElementTree as etree


###TASK1### 
tree = etree.parse('pythonLab1.xml')
f = open('pythonLab1Output.xml', 'a', encoding = 'utf-8')
root = tree.getroot()
for elem in root.iter('source'):
    f.write(elem.text)
f.close()
###TASK1###


###TASK2### 
tree = etree.parse('pythonLab1.xml')
params = []
f = open('pythonLab1Output2.xml', 'a', encoding = 'utf-8')
root = tree.getroot()
for attribute in root:
    for stats in attribute.iter('token'):
        name = stats.get('text')
        for sub_att in stats.iter('g'):
            word_type = sub_att.get('v');
            params.append(word_type)
        if (('NOUN' in params) and ('plur' in params)):
            f.write(name + '\n')
            params.clear()
        else:
            params.clear()
f.close()
###TASK2###


###TASK3### 
tree = etree.parse('pythonLab1.xml')
conj = 0
verb = 0;
root = tree.getroot()

for attribute in root:
    for stats in attribute.iter('token'):
        name = stats.get('text')
        if (name.lower() == 'может'):
            for sub_att in stats.iter('g'):
                word_type = sub_att.get('v');
                if (word_type == 'VERB'):
                    verb += 1
                if (word_type == 'CONJ'):
                    conj += 1
print('Verb: ' + str(verb) + '\n' + 'Conj: ' + str(conj))
###TASK3###


###TASK4### 
import sys, xml.etree.ElementTree as ET

root = ET.parse("pythonTest.xml").getroot()
for child in root.iter('sentence'):
    if (child.get('id') == '1'):
        for tokens in child.iter('tokens'):
            for token in tokens.iter('token'):
                token_id = token.get('id')
                token_name = token.get('text')
                if (token_id == '2'):
                    tokens.remove(token);
                    new_elem = ET.Element('token')
                    new_elem.attrib = {'id' : '2', 'text' : '”нивер'}
                    child[1].insert(1, new_elem)
            for token2 in tokens.iter('token'):
                print('Tag: %s \nKeys: %s \nItems: %s \nText: %s \n' % (token2.tag, token2.keys(), token2.items(), token2.text))    
###TASK4### 


from xml.dom import minidom

def indent(elem, level=0):
    i = "\n" + level*"  "
    if len(elem):
        if not elem.text or not elem.text.strip():
            elem.text = i + "  "
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
        for elem in elem:
            indent(elem, level+1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
    else:
        if level and (not elem.tail or not elem.tail.strip()):
            elem.tail = i

def prettify(elem):
    """Return a pretty-printed XML string for the Element.
    """
    rough_string = ET.tostring(elem, 'utf-8')
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent='\t')
