/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.zalotest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rin
 */
public class ReadFileRunnable implements Runnable {

    private MyQueue queue;
    private String input;
    private List<File> listFile;
    private int maxSizeBlock;

    public ReadFileRunnable(MyQueue queue, String input) {
        this.queue = queue;
        this.input = input;
    }

    public ReadFileRunnable(MyQueue queue, String input, List<File> listFile) {
        this.queue = queue;
        this.input = input;
        this.listFile = listFile;
    }

    public ReadFileRunnable(MyQueue queue, String input, List<File> listFile, int maxSizeBlock) {
        this.queue = queue;
        this.input = input;
        this.listFile = listFile;
        this.maxSizeBlock = maxSizeBlock;
    }

    @Override
    public void run() {
        try {
//            boolean test = true;
            int currentSize = 0;
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(input));
            List<String> lines = new ArrayList<>();
            String line = null;
            while ((line = br.readLine()) != null) {
//                if(test){
//                    System.out.println(System.currentTimeMillis() + " Reading!!!");
//                    test = false;
//                }
                lines.add(line);
                currentSize += line.length() + 1;
                if (currentSize >= maxSizeBlock) {
                    currentSize = 0;
                    Collections.sort(lines);
                    File file = new File("tmp" + System.currentTimeMillis());
                    listFile.add(file);
//                    System.out.println(System.currentTimeMillis() + " Sending queue!!!");
                    queue.put(new MyData(lines, file));
                    lines = new ArrayList<>();
//                    test = true;
                }
            }
            if (!lines.isEmpty()) {
                Collections.sort(lines);
                File file = new File("tmp" + System.currentTimeMillis());
                listFile.add(file);
                queue.put(new MyData(lines, file));
            }
            queue.continueProducing = Boolean.FALSE;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ReadFileRunnable.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            queue.continueProducing = Boolean.FALSE;
        }
    }

}
