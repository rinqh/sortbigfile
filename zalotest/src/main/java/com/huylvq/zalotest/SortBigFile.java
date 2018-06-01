/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.zalotest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rin
 */
public class SortBigFile {

    //private int maxSize = 30;
    private int maxSize = 100 * 1024 * 1024;
    private List<File> tmpFiles = new ArrayList<>();

    SortBigFile() {
    }

    void sort(String inputFile, String outputFile) {
        spilitInput(inputFile);
        mergeSort(outputFile);
    }

    void spilitInput(String inputFile) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        tmpFiles.clear();
        BufferedReader br = null;
        List<String> lines = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(inputFile));
            String line = null;
            int currChunkSize = 0;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                currChunkSize += line.length() + 1;
                if (currChunkSize >= maxSize) {
                    currChunkSize = 0;
                    Collections.sort(lines);
                    File file = new File("tmp" + System.currentTimeMillis());
                    tmpFiles.add(file);
                    writeFile(lines, file);
                    //System.out.println(lines);
                    lines.clear();
                }
            }
            if (!lines.isEmpty()) {
                //System.out.println(lines);
                Collections.sort(lines);
                File file = new File("tmp" + System.currentTimeMillis());
                tmpFiles.add(file);
                writeFile(lines, file);
                lines.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    private void writeFile(List<String> lines, File outputFile) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            for (String s : lines) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    public void mergeSort(String outputFile) {
        int currentSize = 0;
        List<String> writes = new ArrayList<>();
        List<Wrapper> wrappers = new ArrayList<>();
        CompareWrapper compare = new CompareWrapper();
        List<BufferedReader> readers = new ArrayList<>();
        BufferedWriter writer = null;
        File f = new File(outputFile);
        if (f.exists()) {
            f.delete();
        }
        try {
            writer = new BufferedWriter(new FileWriter(outputFile, true));
            System.err.println(tmpFiles.size());
            for (int i = 0; i < tmpFiles.size(); i++) {
                BufferedReader reader = new BufferedReader(new FileReader(tmpFiles.get(i)));
                readers.add(reader);
                String line = reader.readLine();
                if (line != null) {
                    Wrapper wrapper = new Wrapper(line);
                    wrapper.index = i;
                    wrappers.add(wrapper);
                }
            }
            while (wrappers.size() > 0) {
                Collections.sort(wrappers, compare);
                Wrapper line = wrappers.remove(0);
                writes.add(line.string);
                currentSize += line.string.length() + 1;
                if (currentSize >= maxSize) {
                    currentSize = 0;
                    //appendFile(writes, writer);
                    appendFileUsingNIO(writes, outputFile);
                    writes.clear();
                }
//                writer.write(line.string);
//                writer.write(System.lineSeparator());
                //System.err.println(line);
                //BufferedReader reader = map.remove(line);
                BufferedReader reader = readers.get(line.index);
//                if (reader == null) {
//                    System.err.println("reader null");
//                }
                String nextLine = reader.readLine();
                if (nextLine != null) {
                    Wrapper newWrapper = new Wrapper(nextLine);
                    newWrapper.index = line.index;
                    wrappers.add(newWrapper);
                }
            }
            if (!writes.isEmpty()) {
                //appendFile(writes, writer);
                appendFileUsingNIO(writes, outputFile);
                writes.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            for (int i = 0; i < readers.size(); i++) {
                try {
                    readers.get(i).close();
                } catch (IOException ex) {
                    Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            for (int i = 0; i < tmpFiles.size(); i++) {
                tmpFiles.get(i).delete();
            }
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

    }

    private void appendFile(List<String> writes, BufferedWriter writer) throws IOException {
        for (String s : writes) {
            writer.write(s);
            writer.newLine();
        }
    }

    private void appendFileUsingNIO(List<String> writes, String outputFile) throws IOException {
        Path filePathObj = Paths.get(outputFile);
        boolean fileExists = Files.exists(filePathObj);
        if (!fileExists) {
            Files.createFile(filePathObj);
        }
        // Appending The New Data To The Existing File

        for (String s : writes) {
            Files.write(filePathObj, s.getBytes(), StandardOpenOption.APPEND);
            Files.write(filePathObj, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            //writer.write(s);
            //writer.newLine();
        }
    }

}
