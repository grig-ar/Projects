with open('HP1.txt', 'r', encoding = 'latin-1') as txtIN, open('HP1_n.txt', 'w', encoding = 'latin-1') as txtOUT:
    text = txtIN.read()
    #print(text)
    for ch in [',', '.', '!', '?', '…', '«', '»', '*', ':', ';', '—', '(', ')']:
        text = text.replace(ch, '')
    text = text.replace('-', ' ')
    txtOUT.write(text)
#'…',
