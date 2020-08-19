import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Scanner;

enum ProgramMode {
    ENCODE, DECODE,
}

enum InputMode {
    CONSOLE, FILE,
}

enum FileType {
    IMAGE, OTHER,
}


public class Main {
    private static final int END_OF_FILE_LENGTH = 59;
    private static final int ENCODED_IMAGE = 73;
    private static final int ENCODED_OTHER_TYPE = 79;
    //private static final int BUF_SIZE = 3 * 1024 * 1024;

    public static void main(String[] args) throws NullPointerException {
        Main main = new Main();
        ProgramMode programMode = main.chooseProgramMode();
        if (programMode == null) {
            return;
        }

        File imageFile = main.getFile(FileType.IMAGE);
        if (imageFile == null) {
            return;
        }

        if (programMode == ProgramMode.DECODE) {
            main.decodeFile(imageFile);
        } else {
            InputMode inputMode = main.chooseInputMode();
            if (inputMode == null) {
                return;
            }
            byte[] byteMessage;
            if (inputMode == InputMode.CONSOLE) {
                String message = main.getMessageFromConsole();
                if (message.length() > 0 && message.length() < imageFile.length() / 3) {
                    //charMessage = message.toCharArray();
                    byteMessage = message.getBytes();
                    main.encodeFile(imageFile, byteMessage, FileType.OTHER);
                }
            } else {
                File file = main.getFile(FileType.OTHER);
                if (file == null) {
                    return;
                }
                FileType fileType = (FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase().equals("png") ||
                        FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase().equals("jpg")) ? FileType.IMAGE : FileType.OTHER;
                if (file.length() > (imageFile.length() / 4)) {
                    System.out.println("File " + file.getName() + " is too big to be encoded");
                    return;
                }
                byteMessage = main.getMessageFromFile(file);
                if (byteMessage == null) {
                    return;
                }
                main.encodeFile(imageFile, byteMessage, fileType);
            }
        }
    }

    private static class ImageFileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = FilenameUtils.getExtension(f.getAbsolutePath());
            return extension.toLowerCase().equals("png");
        }

        @Override
        public String getDescription() {
            return "PNG images";
        }
    }


    private File getFile(FileType fileType) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "\\res"));

        if (fileType == FileType.IMAGE) {
            fileChooser.setFileFilter(new ImageFileFilter());
        }

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    private ProgramMode chooseProgramMode() {
        Scanner in = new Scanner(System.in);
        System.out.println("Choose program mode (E)ncode/(D)ecode:");
        String mode = in.nextLine();
        switch (mode.toLowerCase()) {
            case "encode":
            case "e":
                return ProgramMode.ENCODE;
            case "decode":
            case "d":
                return ProgramMode.DECODE;
            default:
                System.out.println("Incorrect mode!");
                return null;
        }
    }

    private InputMode chooseInputMode() {
        Scanner in = new Scanner(System.in);
        System.out.println("Choose input mode (C)onsole/(F)ile:");
        String mode = in.nextLine();
        switch (mode.toLowerCase()) {
            case "console":
            case "c":
                return InputMode.CONSOLE;
            case "file":
            case "f":
                return InputMode.FILE;
            default:
                System.out.println("Incorrect mode!");
                return null;
        }
    }

    private String getMessageFromConsole() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter message to encode:");
        StringBuilder stringBuilder = new StringBuilder();
        in.skip("((?<!\\R)\\s)*");

        while (in.hasNextLine()) {
            String nextLine = in.nextLine();

            if (nextLine.length() == 0) {
                if (stringBuilder.length() == 0) {
                    return stringBuilder.toString();
                }

                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                break;
            }

            stringBuilder.append(nextLine).append('\n');
        }

        return stringBuilder.toString();
    }

    private byte[] getMessageFromFile(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try (InputStream in = new FileInputStream(file);
//             Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
//             Reader buffer = new BufferedReader(reader);
//        ) {
//            int r;
//            int count = 0;
//            char[] buf = new char[(int) file.length()];
//            while ((r = buffer.read()) != -1) {
//                char ch = (char) r;
//                buf[count] = ch;
//                ++count;
//            }
//            return buf;
//            //BufferedImage bufferedImage = ImageIO.read(file);
//            //WritableRaster raster = bufferedImage.getRaster();
//            //DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
//            //byte[] buf = Files.readAllBytes(file.toPath());
//            //String text = new String(buf, StandardCharsets.);
//            //return Files.readAllBytes(file.toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    private void encodeFile(File file, byte[] message, FileType fileType) {
        try {
            if (fileType == FileType.IMAGE) {
                byte[] encoded = Base64.getEncoder().encodeToString(message).getBytes();
                message = new byte[encoded.length];
                System.arraycopy(encoded, 0, message, 0, encoded.length);
            }
            int count = 0;
            int len = message.length;
            int parsedLen = message.length;
            int x = 0;
            LinkedList<Integer> stack = new LinkedList<>();
            if (fileType == FileType.IMAGE) {
                stack.push(ENCODED_IMAGE);
            } else {
                stack.push(ENCODED_OTHER_TYPE);
            }
            stack.push(END_OF_FILE_LENGTH);
            while (parsedLen > 0) {
                stack.push(parsedLen % 10);
                parsedLen /= 10;
            }

            BufferedImage oldImage = ImageIO.read(file);
            BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2 = (Graphics2D) newImage.getGraphics();
            g2.drawImage(oldImage, null, null);
            g2.dispose();

            while (!stack.isEmpty()) {
                newImage.setRGB(x, 0, getEncodedColor(new Color(newImage.getRGB(x, 0)), stack.pop()).getRGB());
                ++x;
            }

            for (int y = 1; y < newImage.getHeight(); ++y) {
                for (x = 0; x < newImage.getWidth(); ++x) {
                    if (count >= len) {
                        break;
                    }
                    byte letter = message[count];
                    newImage.setRGB(x, y, getEncodedColor(new Color(newImage.getRGB(x, y)), letter).getRGB());
                    ++count;
                }
            }
            ImageIO.write(newImage, "png", new File("c:\\Users\\Артем\\IdeaProjects\\Java4Course\\Steganography\\res\\testEncoding.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Encoded");
    }

    private static Color getEncodedColor(Color oldColor, int letterVal) {
        //int firstBits = (letterVal >>> 5) << 5; // first 3 significant bits
        int firstBits = (letterVal & 0xe0) >> 5; // first 3 significant bits
        int secondBits = (letterVal & 0x18) >> 3; // second 2 significant bits
        int thirdBits = letterVal & ((int) (1L << 3) - 1); // least 3 significant bits

        int red = oldColor.getRed() & 248;
        int green = oldColor.getGreen() & 252;
        int blue = oldColor.getBlue() & 248;

        red |= firstBits;
        green |= secondBits;
        blue |= thirdBits;

        //oldVal = oldVal & ((int) (1L << amount) - 1); //get 'amount' least significant bits
        return new Color(red, green, blue);
    }

    private static int getDecodedValue(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        int redLSB = red & ((int) (1L << 3) - 1);
        int greenLSB = green & ((int) (1L << 2) - 1);
        int blueLSB = blue & ((int) (1L << 3) - 1);

        return redLSB << 5 | greenLSB << 3 | blueLSB;
    }

//    private static String encodeFileToBase64Binary(File file) {
//        String encodedfile = null;
//        try {
//            FileInputStream fileInputStreamReader = new FileInputStream(file);
//            byte[] bytes = new byte[(int) file.length()];
//            fileInputStreamReader.read(bytes);
//            encodedfile = Base64.getEncoder().encodeToString(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return encodedfile;
//    }

    private void decodeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("c:\\Users\\Артем\\IdeaProjects\\Java4Course\\Steganography\\res\\decodedFile"));
             FileOutputStream fileOutputStream = new FileOutputStream("c:\\Users\\Артем\\IdeaProjects\\Java4Course\\Steganography\\res\\decodedFile")
        ) {
            FileType fileType;
            int count = 0;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedImage image = ImageIO.read(file);
            int len;
            int x = 0;

            while (true) {
                len = getDecodedValue(new Color(image.getRGB(x, 0)));
                if (len == END_OF_FILE_LENGTH) {
                    ++x;
                    break;
                }
                stringBuilder.append(len);
                ++x;
            }

            len = Integer.parseInt(stringBuilder.toString());

            fileType = (getDecodedValue(new Color(image.getRGB(x, 0))) == ENCODED_IMAGE) ? FileType.IMAGE : FileType.OTHER;
            ++x;

            byte[] buffer = new byte[len];
            int bufferPosition = 0;

            stringBuilder.setLength(0);
            for (int y = 1; y < image.getHeight(); ++y) {
                for (x = 0; x < image.getWidth(); ++x) {
                    if (count >= len) {
                        break;
                    }

                    int letter = getDecodedValue(new Color(image.getRGB(x, y)));

                    if (fileType == FileType.IMAGE) {
                        buffer[bufferPosition] = (byte) letter;
                        ++bufferPosition;
                    } else {
                        stringBuilder.append((char) letter);
                    }

                    ++count;
                }
            }

            if (fileType == FileType.IMAGE) {
                byte[] decoded = Base64.getDecoder().decode(buffer);
                fileOutputStream.write(decoded);
            } else {
                writer.write(stringBuilder.toString());
            }

            System.out.println("Decoded");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
