package searchers;

import substringSearchingAlgorithms.BytesBoyerMoore;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;


public class SubstringSearchingFileVisitor extends SimpleFileVisitor<Path> {

    private static final int BUF_SIZE = 3 * 1024 * 1024;
    private static final int MAX_BLOCK_SIZE = 100 * 1024 * 1024;

    private final byte[] needle;
    private final int needleLength;
    private final int threadsAmount;
    private ArrayList<Path> files = new ArrayList<>();
    private long currentBlockSize;

    public SubstringSearchingFileVisitor(byte[] needle, int needleLength, int threadsAmount) {
        this.needle = needle;
        this.needleLength = needleLength;
        this.threadsAmount = threadsAmount;
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        files.add(file);
        currentBlockSize += attrs.size();
        if (currentBlockSize >= MAX_BLOCK_SIZE) {
            new Thread(search(new ArrayList<>(files), needle, needleLength)).start();
            files.clear();
            currentBlockSize = 0;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException io) {
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        if (e == null) {
            if (files.size() != 0) {
                var thread = new Thread(search(new ArrayList<>(files), needle, needleLength));
                thread.start();
                files.clear();
                currentBlockSize = 0;
            }
            return FileVisitResult.CONTINUE;
        } else {
            throw e;
        }
    }

    private Runnable doWorkInOneThread(Path fileName, byte[] needle, int needleLength) {
        return () -> {
            var bytesBoyerMoore = new BytesBoyerMoore();
            try {
                byte[] fileBytes = Files.readAllBytes(fileName);
                bytesBoyerMoore.indexOf(fileName.toAbsolutePath().toString(), fileBytes, fileBytes.length, needle, needleLength, 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Some of the files contains non-ASCII characters. Program will be closed.");
            }
        };
    }

    private Runnable doWork( String fileName, final long offset, final long length, byte[] needle, int needleLength) {
        return () -> {
            try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
                var boyerMoore = new BytesBoyerMoore();
                long count = 0;
                long bytesRead = 0;
                byte[] buf = new byte[BUF_SIZE];
                int bufLength;
                byte[] tail = new byte[0];
                int tailLength = tail.length;
                var tempLength = length;
                long amountToRead;
                long initialOffest = 0;
                if (offset > 0) {
                    raf.seek(offset - needleLength + 1);
                    initialOffest = offset - needleLength + 1;
                    tail = new byte[needleLength - 1];
                    tailLength = tail.length;
                    count = raf.read(tail, 0, needleLength - 1);
                    bytesRead += count;
                }
                while (bytesRead < length && count >= 0) {
                    if (tempLength > BUF_SIZE) {
                        amountToRead = BUF_SIZE;
                        tempLength -= BUF_SIZE;
                    } else {
                        amountToRead = tempLength;
                    }
                    count = raf.read(buf, 0, (int) amountToRead);

                    bufLength = (int) amountToRead;
                    byte[] bufTemp = new byte[(int) amountToRead + tailLength];
                    System.arraycopy(tail, 0, bufTemp, 0, tailLength);
                    System.arraycopy(buf, 0, bufTemp, tailLength, bufLength);
                    bufLength = bufTemp.length;
                    boyerMoore.indexOf(fileName, bufTemp, bufLength, needle, needleLength, initialOffest + bytesRead - needleLength + 1);
                    tail = Arrays.copyOfRange(bufTemp, bufLength - (needleLength - 1), bufLength);
                    tailLength = tail.length;
                    bytesRead += count;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Some of the files contains non-ASCII characters. Program will be closed.");
            } catch (Exception e) {
                System.out.println("From Thread no:" + Thread.currentThread().getId() + "===>" + e.getMessage());
            }
        };
    }

    public Runnable search(ArrayList<Path> files, byte[] needle, int needleLength) {
        return () -> {

            int filesAmount = files.size();

            for (int i = 0; i < filesAmount; ++i) {
                File inputFile = files.get(i).toFile();
                long fileLength = inputFile.length();
                if (needleLength > fileLength) {
                    continue;
                }
                long sizePerThread = fileLength / threadsAmount;
                if (fileLength < MAX_BLOCK_SIZE) {
                    new Thread(doWorkInOneThread(files.get(i), needle, needleLength)).start();
                    continue;
                }
                for (int t = 0; t < threadsAmount; t++) {
                    new Thread(doWork(inputFile.getAbsolutePath(), (long) t * sizePerThread, sizePerThread, needle, needleLength)).start();
                }
            }

        };
    }
}

