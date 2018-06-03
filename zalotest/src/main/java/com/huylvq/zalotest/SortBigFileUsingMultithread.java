/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.zalotest;

import com.huylvq.zalotest.data.MyQueue;
import com.huylvq.zalotest.runnable.MergeFileRunnable;
import com.huylvq.zalotest.runnable.ReadFileRunnable;
import com.huylvq.zalotest.runnable.WriteFileRunnable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rin
 */
public class SortBigFileUsingMultithread {

    private static final int MEG = 1024 * 1024;
    private static final int DEFAUT_MAX_BLOCK_SIZE = 100 * MEG;
    private String inputFile;
    private String outputFile;
    private List<File> tmpFiles;

    /**
     *
     */
    public SortBigFileUsingMultithread() {
        tmpFiles = new ArrayList<>();
    }

    /**
     *
     * @param inputFile
     * @param outputFile
     */
    public SortBigFileUsingMultithread(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        tmpFiles = new ArrayList<>();
    }

    /**
     *
     */
    public void sort(){
        try {
            System.out.println("Starting " + System.currentTimeMillis());
            splitInput();
            System.out.println("Done split, start merge " + System.currentTimeMillis());
            mergeFile();
            System.out.println("Done " + System.currentTimeMillis());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(SortBigFileUsingMultithread.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void splitInput() throws InterruptedException, ExecutionException {
        int maxBlockSize = DEFAUT_MAX_BLOCK_SIZE;
        //int maxBlockSize = 10 * 1024;
        MyQueue queue = new MyQueue();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<?>> futures = new ArrayList<>();

        // Reader thread
        futures.add(executor.submit(new ReadFileRunnable(queue, inputFile, tmpFiles, maxBlockSize)));
        // Writer threads
        futures.add(executor.submit(new WriteFileRunnable("Write 1", queue)));
        futures.add(executor.submit(new WriteFileRunnable("Write 2", queue)));

        // Wait for all thread done task
        for (Future<?> f : futures) {
            f.get();
        }
        executor.shutdown();
        System.out.println(tmpFiles.size());
    }

    /**
     *
     * 
     */
    private void mergeFile() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        BlockingQueue<String> queue = new LinkedBlockingDeque<>(1000);
        MergeFileRunnable merge = new MergeFileRunnable(tmpFiles, queue);
        Future f = exec.submit(merge);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            String line;
            while (true) {
                line = queue.poll(1, TimeUnit.SECONDS);
                if (line != null) {
                    writer.write(line);
                    writer.newLine();
                }
                if (f.isDone() && queue.isEmpty()) {
                    break;
                }
            }

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            f.cancel(true);
            exec.shutdown();
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
