import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    int phrL = 2;
    int rpt = 2;
    int isF = 0;
    boolean isParsed = true;
    FileInputStream fstr = null;

    public void parseArgs (String[] ar) {
        switch (ar.length) {
            case 0:
                break;
            case 1:
                if (ar[0].equals("-")) {
                    break;
                } else {
                    try {
                        fstr = new FileInputStream(new File("src\\" + ar[0]));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (fstr != null)
                        isF = 1;
                    break;
                }
            case 2: case 3:
                if (!(ar[0].equals("-n")) && !(ar[0].equals("-m"))) {
                    System.out.println("Format error!\n");
                    isParsed = false;
                    break;
                } else {
                    if(ar[0].equals("-n"))
                        phrL = Integer.parseInt(ar[1]);
                    if(ar[0].equals("-m"))
                        rpt = Integer.parseInt(ar[1]);
                }
                if (ar.length == 3 && !ar[2].equals("-")) {
                    try {
                        fstr = new FileInputStream("src\\" + new File(ar[2]));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (fstr != null)
                        isF = 1;
                }
                break;
            case 4: case 5:
                if(!((ar[0].equals("-n")) && (ar[2].equals("-m")) || (ar[2].equals("-n")) && (ar[0].equals("-m")))) {
                    System.out.println("Format error!\n");
                    isParsed = false;
                    break;
                } else {
                    if (ar[0].equals("-n"))
                        phrL = Integer.parseInt(ar[1]);
                    else
                        phrL = Integer.parseInt(ar[3]);
                    if (ar[0].equals("-m"))
                        rpt = Integer.parseInt(ar[1]);
                    else
                        rpt = Integer.parseInt(ar[3]);
                }
                if (ar.length == 5 && !ar[4].equals("-")) {
                    try {
                        fstr = new FileInputStream("src\\" + new File(ar[4]));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (fstr != null)
                        isF = 1;
                }
                break;
        }
    }

    public String fileToString (InputStream ist) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        String buf = "";
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ist.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            buf = result.toString("UTF-8");
        } catch (Exception ex) {ex.printStackTrace();}
        return buf;
    }

    public String streamToString () {
        StringBuilder text = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        String temp;
        while (sc.hasNext()){
            text.append(sc.nextLine()).append(" ");
        }
        text.deleteCharAt(text.length()-1);
        return text.toString();
    }

    public String[] stringToPhrases(int phraseLength, String stringFromFile) {
        if (phraseLength <= 0) {
            throw new IllegalArgumentException("Nonpositive phrase length: " + phraseLength);
        }
        stringFromFile = stringFromFile.replaceAll("\r\n", " ");
        String[] phr = stringFromFile.split(" ");
        String[] phrases;
        int length = phr.length;
        if (phraseLength > length) {
            throw new IllegalArgumentException("Length of phrase: " + phraseLength + " > amount of given words: " + length);
        }
        ArrayList<String> parsed = new ArrayList<String>();
        if (phraseLength == 1) {
            String temp;
            for (String aPhr : phr) {
                temp = aPhr;
                parsed.add(temp);
            }
            phrases = parsed.toArray(new String[parsed.size()]);
            return phrases;
        }
        else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < (length-phraseLength+1); ++i) {
                for (int j = 0; j < phraseLength; ++j) {
                    builder.append(phr[i+j]).append(" ");
                }
                builder.deleteCharAt(builder.length()-1);
                parsed.add(builder.toString());
                builder.setLength(0);
            }
        }
        phrases = parsed.toArray(new String[parsed.size()]);
        return phrases;
    }

    public Map<String, Integer> phrasesToMap(String[] phrases) {
        Map<String, Integer> counted = new HashMap<>();
        for (int i = 0; i < phrases.length; ++i) {
            Integer frequency = counted.get(phrases[i]);
            counted.put(phrases[i], frequency == null ? 1 : frequency + 1);
        }
        return counted;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public void outputMap(Map<String, Integer> counted, int repeats) {
        if (repeats <= 0) {
            throw new IllegalArgumentException("Nonpositive repeats: " + repeats);
        }
        for (Map.Entry entry : counted.entrySet()) {
            if((int) entry.getValue() >= repeats)
                System.out.println(entry.getKey() + " (" + entry.getValue() + ")");
        }
    }

    public static void main(String[] args) {
        Main myObj = new Main();
        String str;
        String[] parsed;
        Map<String, Integer> counted;
        myObj.parseArgs(args);
        if (!myObj.isParsed)
            return;
        if( myObj.isF == 1)
            str = myObj.fileToString(myObj.fstr);
        else
            str = myObj.streamToString();
        if (myObj.fstr != null) {
            try {
                myObj.fstr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        parsed = myObj.stringToPhrases(myObj.phrL, str);
        counted = myObj.phrasesToMap(parsed);
        counted = sortByValue(counted);
        myObj.outputMap(counted, myObj.rpt);
    }
}
