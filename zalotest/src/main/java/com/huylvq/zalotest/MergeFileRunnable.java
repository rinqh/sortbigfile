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
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rin
 */
public class MergeFileRunnable implements Runnable {

    private final List<File> listFiles;
    private final BlockingQueue queue;
    int maxBlockSize;

    public MergeFileRunnable(List<File> listFiles, BlockingQueue queue, int maxBlockSize) {
        this.listFiles = listFiles;
        this.queue = queue;
        this.maxBlockSize = maxBlockSize;
    }

    @Override
    public void run() {
        //int currentSize = 0;
        List<Wrapper> wrappers = new ArrayList<>();
        CompareWrapper compare = new CompareWrapper();
        //List<String> lines = new ArrayList<>();
        List<BufferedReader> readers = new ArrayList<>();
        try {
            for (int i = 0; i < listFiles.size(); i++) {
                BufferedReader reader = null;
                reader = new BufferedReader(new FileReader(listFiles.get(i)));
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
                queue.put(line.string);
                BufferedReader reader = readers.get(line.index);
                String nextLine = reader.readLine();
                if (nextLine != null) {
                    Wrapper newWrapper = new Wrapper(nextLine);
                    newWrapper.index = line.index;
                    wrappers.add(newWrapper);
                }
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(MergeFileRunnable.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            for (int i = 0; i < readers.size(); i++) {
                try {
                    readers.get(i).close();
                } catch (IOException ex) {
                    Logger.getLogger(MergeFileRunnable.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            for (int i = 0; i < listFiles.size(); i++) {
                listFiles.get(i).delete();
            }
        }
    }

}
