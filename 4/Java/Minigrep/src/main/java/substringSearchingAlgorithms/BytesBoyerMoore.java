package substringSearchingAlgorithms;

import java.util.Arrays;

public class BytesBoyerMoore {
    private static final int ALPHABET_SIZE = 128;

    public void indexOf(String fileName, byte[] haystack, int haystackLength, byte[] needle, int needleLength, long offset) {
        if (needleLength == 0) {
            return;
        }
        int[] byteTable = makeByteTable(needle, needleLength);
        int[] offsetTable = makeOffsetTable(needle, needleLength);
        for (int i = needleLength - 1, j; i < haystackLength; ) {
            for (j = needleLength - 1; needle[j] == haystack[i]; --i, --j) {
                if (j == 0) {
                    System.out.println(fileName + ":" + (offset + i));
                    break;
                }
            }
            i += (offsetTable[needleLength - 1 - j] >= byteTable[haystack[i]]) ? offsetTable[needleLength - 1 - j] : byteTable[haystack[i]];
        }
    }

    private static int[] makeByteTable(byte[] needle, int needleLength) {
        int[] table = new int[ALPHABET_SIZE];
        Arrays.fill(table, needleLength);
        for (int i = 0; i < needleLength - 1; ++i) {
            table[needle[i]] -= 1 + i;
        }
        return table;
    }

    private static int[] makeOffsetTable(byte[] needle, int needleLength) {
        int[] table = new int[needleLength];
        int lastPrefixPosition = needleLength;
        for (int i = needleLength; i > 0; --i) {
            if (isPrefix(needle, needleLength, i)) {
                lastPrefixPosition = i;
            }
            table[needleLength - i] = lastPrefixPosition - i + needleLength;
        }
        for (int i = 0; i < needleLength - 1; ++i) {
            int slen = suffixLength(needle, needleLength, i);
            table[slen] = needleLength - 1 - i + slen;
        }
        return table;
    }

    private static boolean isPrefix(byte[] needle, int needleLength, int p) {
        for (int i = p, j = 0; i < needleLength; ++i, ++j) {
            if (needle[i] != needle[j]) {
                return false;
            }
        }
        return true;
    }

    private static int suffixLength(byte[] needle, int needleLength, int p) {
        int len = 0;
        for (int i = p, j = needleLength - 1;
             i >= 0 && needle[i] == needle[j]; --i, --j) {
            len += 1;
        }
        return len;
    }
}
