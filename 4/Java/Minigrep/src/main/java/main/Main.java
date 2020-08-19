package main;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParseResult;
import searchers.FileSearcher;
import searchers.SubstringSearchingFileVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Command(name = "minigrep", version = {"0.1.0", "Grigorovich Artyom", "NSU, FIT", "Copyright(c) 2019"},
        description = {"Find files or substrings in files (only ASCII!)", "Please, use '-Xms350m -Xmx2G' VM options if possible!"})
public class Main {
    private static final int THREADS_AMOUNT = Runtime.getRuntime().availableProcessors();

    @Option(names = {"-n", "--name"}, description = "searches the directory tree for file with given name", paramLabel = "fileName")
    private String fileName;

    @Option(names = {"-d", "--data"}, description = "searches the directory tree or file for given string", paramLabel = "needle")
    private String needle;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help message and exit")
    private boolean helpRequested = false;

    @Option(names = {"-v", "--version"}, versionHelp = true, description = "print version information and exit")
    boolean versionRequested;

    @Parameters(paramLabel = "FILE", arity = "1..*", description = "one or more files/dirs to search in")
    private String[] fileNames;

    public static void main(String[] args) {
        Main main = new Main();
        CommandLine commandLine = new CommandLine(main);
        try {
            var parseResult = commandLine.parseArgs(args);
            if (commandLine.isUsageHelpRequested() || commandLine.isVersionHelpRequested()) {
                if (commandLine.isUsageHelpRequested()) {
                    System.out.println(commandLine.getUsageMessage());
                }
                if (commandLine.isVersionHelpRequested()) {
                    commandLine.printVersionHelp(System.out);
                }
                return;
            }
            main.Execute(parseResult);
        } catch (CommandLine.MissingParameterException ex) {
            System.out.println("Files not specified!");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Some of the files contains non-ASCII characters. Program will be closed.");
        }


    }

    private static boolean isPureAscii(String v) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(v);
    }

    private static String[] selectDistinctNames(String[] names) {
        if (names.length == 1) {
            return names;
        }
        ArrayList<String> distinctFiles = new ArrayList<>();
        for (int i = 0; i < names.length; ++i) {
            if (!distinctFiles.contains(names[i])) {
                distinctFiles.add(names[i]);
            }
        }
        var temp = new String[names.length];
        return distinctFiles.toArray(temp);
    }

    private static ArrayList<File> searchByName(ParseResult parseResult, String[] files, String fileName) {
        ArrayList<File> foundFiles = new ArrayList<>();
        for (int i = 0; i < files.length; ++i) {
            File file = new File(files[i]);
            if (file.getName().contains(fileName)) {
                foundFiles.add(file);
                continue;
            }
            final FileSearcher ff = new FileSearcher(fileName, file, THREADS_AMOUNT);
            final ArrayList<File> f = ff.find();
            foundFiles.addAll(f);
        }
        return foundFiles;
    }

    private static void searchByData(File[] fileArray, String needle) {
        if (!isPureAscii(needle)) {
            System.out.println("Needle contains non-ASCII characters. Program will be closed.");
            return;
        }

        byte[] needleBytes = needle.getBytes(StandardCharsets.UTF_8);
        int needleLength = needleBytes.length;
        for (int i = 0; i < fileArray.length; ++i) {
            File file = fileArray[i];
            var substringSearchingFileVisitor = new SubstringSearchingFileVisitor(needleBytes, needleLength, THREADS_AMOUNT);
            if (file.isFile()) {
                new Thread(substringSearchingFileVisitor.search(new ArrayList<>(List.of(file.toPath())), needle.getBytes(StandardCharsets.UTF_8), needleLength)).start();
            } else {
                try {
                    Files.walkFileTree(file.toPath(), substringSearchingFileVisitor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void Execute(ParseResult parseResult) {
        if (fileName == null && needle == null) {
            System.out.println("Search options are not specified!. Program will be closed.");
            return;
        }
        fileNames = selectDistinctNames(fileNames);

        if (parseResult.hasMatchedOption("-n")) {

            var found = searchByName(parseResult, fileNames, fileName);
            if (found.size() == 0) {
                return;
            }

            if (parseResult.hasMatchedOption("-d")) {
                var temp = new File[1];
                searchByData(found.toArray(temp), needle);
            } else {
                final int length = found.size();
                for (int j = 0; j < length; ++j) {
                    System.out.println(found.get(j).getAbsolutePath());
                }
                System.out.println();
            }
            return;
        }

        if (parseResult.hasMatchedOption("-d")) {
            int amountOfFiles = fileNames.length;
            File[] files = new File[amountOfFiles];
            for (int i = 0; i < amountOfFiles; ++i) {
                files[i] = new File(fileNames[i]);
            }
            searchByData(files, needle);
        }
    }
}


