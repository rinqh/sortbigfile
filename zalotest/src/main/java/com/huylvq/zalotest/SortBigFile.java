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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rin
 */
public class SortBigFile {

    private int maxSize = 10;
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
                    lines.clear();
                }
            }

            Collections.sort(lines);
            File file = new File("tmp" + System.currentTimeMillis());
            tmpFiles.add(file);
            writeFile(lines, file);
            lines.clear();
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
        Map<String, BufferedReader> map = new HashMap<>();
        List<BufferedReader> readers = new ArrayList<>();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            System.err.println(tmpFiles.size());
            for (int i = 0; i < tmpFiles.size(); i++) {
                BufferedReader reader = new BufferedReader(new FileReader(tmpFiles.get(i)));
                readers.add(reader);
                String line = reader.readLine();
                if (line != null) {
                    map.put(line, readers.get(i));
                }
            }
            List<String> sorted = new LinkedList<>(map.keySet());
            while (map.size() > 0) {
                Collections.sort(sorted);
                String line = sorted.remove(0);
                writer.write(line);
                writer.write(System.lineSeparator());
                System.err.println(line);
                BufferedReader reader = map.remove(line);
                if (reader == null) {
                    System.err.println("reader null");
                }
                String nextLine = reader.readLine();
                if (nextLine != null) {
                    map.put(nextLine, reader);
                    sorted.add(nextLine);
                }
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

}
