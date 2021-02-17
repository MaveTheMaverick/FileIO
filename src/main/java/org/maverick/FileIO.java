package org.maverick;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class FileIO {
    public static final String SEPARATOR = File.separator;
    private static final String[] illegalChars = "\\/:*?\"<>|".split("");

    public static String[] readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        //System.out.println(Arrays.toString(lines));

        return Files.readAllLines(path).toArray(new String[0]);
    }
    public static String[] readFile(Path filePath) {
        String[] lines = new String[0];
        try {
            lines = Files.readAllLines(filePath).toArray(new String[0]);
            //System.out.println(Arrays.toString(lines));
        } catch (IOException e) {
            // exception handling
        }
        return lines;
    }
    public static boolean writeFile(String absolutePath, byte[] fileContent) {
        Path path = Paths.get(absolutePath);
        try {
            Files.write(path, fileContent, StandardOpenOption.CREATE);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static boolean writeFile(String absolutePath, String fileContent) {
        Path path = Paths.get(absolutePath);
        try {
            Files.write(path, fileContent.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static boolean jsonWrite(String absolutePath, String[] write) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(write));
        return writeFile(absolutePath, gson.toJson(write).getBytes());
    }
    public static String[] jsonRead(String absolutePath) {

        Path path = Paths.get(absolutePath);
        Gson gson = new Gson();
        String[] lines = new String[0];
        try {
            lines = Files.readAllLines(path).toArray(new String[0]);
        } catch (IOException e) {
            // exception handling
        }
        return gson.fromJson(lines[0], String[].class);
    }

    /**
     * Returns a String[] of all the items in a directory
     *
     * @param absolutePath
     *        The file path of the directory to be tested
     * @return String[]
     *
     */
    public static String[] directoryTest(String absolutePath) {

        // create a file object that is actually a directory location
        File aDirectory = new File(absolutePath);

        // get a listing of all files in the directory
        String[] filesInDir = aDirectory.list();

        // sort the list of files
        if (filesInDir != null) {
            Arrays.sort(filesInDir);
//            for (String s : filesInDir) {
//                System.out.println("file: " + s);
//            }
        }

        return filesInDir;
    }
    public static boolean fileExists(String absolutePath) {
        File file = new File(absolutePath);
        return file.exists();
    }

    /**
     * Creates nested directories if they don't already exist starting at the base directory of the program.
     * @param folderNames Names of folders, with the root folder to be created as the first argument
     * @return A string representing the complete file path of the last nested directory created
     */
    public static String createNested(String... folderNames) {
        String folderPath = System.getProperty("user.dir");
        StringBuilder indent = new StringBuilder("\\---");
        for (String folderName : folderNames) {
            folderPath += SEPARATOR + folderName;
            File file = new File(folderPath);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println(indent + "+ " + folderName);
                    indent.append("----");
                } else {
                    System.out.println("createNested failed: " + folderName);
                }
            } else {
//                System.out.println(indent + "> " + folderName[i]);
                indent.append("----");
            }
        }
        return folderPath;
    }

    /**
     * Creates a path for nested directories starting at the base directory of the program.
     * @param folderNames Names of nested folders in the root directory. The last string may be a file.
     * @return A string representing the complete file path of the last nested directory/file
     * @throws FileNotFoundException Throws the FileNotFoundException if the file is not found
     */
    public static String getNested(String... folderNames) throws FileNotFoundException {
        String folderPath = System.getProperty("user.dir");
        for (String folderName : folderNames) {
            folderPath += SEPARATOR + folderName;
            File file = new File(folderPath);
            if (!file.exists()) {
                System.err.println("[ERROR] " + folderPath + " does not exist");
                throw new FileNotFoundException();
            }
        }
        return folderPath;

    }
    public static boolean cleanDirectory(String directory) {
        File directoryFile = new File(directory);
        try {
            FileUtils.cleanDirectory(directoryFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException ignored) {}
        return true;
    }

    /**
     * Scans files in a directory to create a unique id
     * @param absolutePath path of the directory
     * @return int unique id
     */
    public static int uniqueId(String absolutePath) {

        String[] filesInDir = directoryTest(absolutePath);

        int fileID;
        int i;
        for (i = 0; i < filesInDir.length; i++) {
            fileID = Integer.parseInt(filesInDir[i].split("-")[0]);
            System.out.println(fileID);
            if (fileID != i)
                break;
        }
        return i;
    }

    public static boolean containsIllegalChars(String string) {
        boolean illegal = false;
        for (String illegalChar : illegalChars) {
            if (string.toLowerCase().contains(illegalChar)) {
                illegal = true;
                break;
            }
        }
        return illegal;
    }
}
