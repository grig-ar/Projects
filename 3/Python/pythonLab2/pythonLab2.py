import json
import operator
from pprint import pprint


###TASK2.1###
count_words = {}
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(len(data['acts'])):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                count_words.update({name : count_words.get(name, 0) + 1})
pprint(max(count_words.items(), key=operator.itemgetter(1))[0])
###TASK2.1###


###TASK2.2###
count_words = {}
with open('RomeoAndJuliet.json') as f:
    data = json.load(f)
    for i in range(len(data['acts'])):
        for j in range(len(data['acts'][i]['scenes'])):
            for k in range(len(data['acts'][i]['scenes'][j]['action'])):
                name = data["acts"][i]["scenes"][j]["action"][k]["character"]
                says = str(data["acts"][i]["scenes"][j]["action"][k]["says"])
                count_words.update({name : max(count_words.get(name, 0), len(says))})
print(max(count_words.items(), key=operator.itemgetter(1))[0], max(count_words.items(), key=operator.itemgetter(1))[1])
###TASK2.2###


###TASK2.3###
data = json.dumps({
    "glossary": {
        "title": "example glossary",
		"GlossDiv": {
            "title": "S",
			"GlossList": {
                "GlossEntry": {
                    "ID": "SGML",
					"SortAs": "SGML",
					"GlossTerm": "Standard Generalized Markup Language",
					"Acronym": "SGML",
					"Abbrev": "ISO 8879:1986",
					"GlossDef": {
                        "para": "A meta-markup language, used to create markup languages such as DocBook.",
						"GlossSeeAlso": ["GML", "XML"]
                    },
					"GlossSee": "markup"
                }
            }
        }
    }
})
with open('data.json', 'w') as outfile:
    json.dump(data, outfile, ensure_ascii=False)
    outfile.close()
###TASK2.3###