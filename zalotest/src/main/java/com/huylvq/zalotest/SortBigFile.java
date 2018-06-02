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
public class SortBigFile {

    //private int maxSize = 30;
    private int maxSize = 100 * 1024 * 1024;
    private List<File> tmpFiles = new ArrayList<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> future;

    SortBigFile() {
    }

    void sort(String inputFile, String outputFile) throws InterruptedException, ExecutionException, IOException {
//        spilitInput(inputFile);

        //mergeSort(outputFile);
        splitInput(inputFile);
        mergeFile(outputFile);
    }

    void spilitInput(String inputFile) {
        boolean test = true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        tmpFiles.clear();
        BufferedReader br = null;
        List<String> lines = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(inputFile));
            String line = null;
            int currChunkSize = 0;
            while ((line = br.readLine()) != null) {
                if (test) {
                    System.out.println(System.currentTimeMillis() + " Reading!!!");
                    test = false;
                }
                lines.add(line);
                currChunkSize += line.length() + 1;
                if (currChunkSize >= maxSize) {
                    currChunkSize = 0;
                    Collections.sort(lines);
                    File file = new File("tmp" + System.currentTimeMillis());
                    tmpFiles.add(file);
                    System.out.println(System.currentTimeMillis() + " Start writing!!!");
                    writeFile(lines, file);
                    System.out.println(System.currentTimeMillis() + " Done writing!!!");
                    //System.out.println(lines);
                    lines.clear();
                    test = true;
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
        int index = 0;
        List<List<String>> writes = new ArrayList<>();
        writes.add(new ArrayList<>());
        writes.add(new ArrayList<>());
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
                writes.get(index).add(line.string);
                currentSize += line.string.length() + 1;
                if (currentSize >= maxSize) {
                    currentSize = 0;
                    appendFileUsingThread(writes.get(index), writer);
                    //appendFile(writes.get(index), writer);
                    //appendFileUsingNIO(writes, outputFile);
                    index = index == 1 ? 0 : 1;
                    writes.get(index).clear();
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
                appendFileUsingThread(writes.get(index), writer);
                if (future != null && !future.isDone()) {
                    future.get();
                }
                writes.get(index).clear();
                writes.clear();
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            executor.shutdown();
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

    private void appendFileUsingThread(List<String> writes, BufferedWriter writer) throws IOException, InterruptedException, ExecutionException {
        if (future != null && !future.isDone()) {
            future.get();
        }

        future = executor.submit(new Runnable() {
            @Override
            public void run() {
                //System.out.println("Thread run!!!");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                for (String s : writes) {
                    try {
                        writer.write(s);
                        writer.newLine();
                    } catch (IOException ex) {
                        Logger.getLogger(SortBigFile.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                //System.out.println("Thread done!!!");
            }
        });

//        for (String s : writes) {
//            writer.write(s);
//            writer.newLine();
//        }
    }

    void splitInput(String inputFile) throws InterruptedException, ExecutionException {
        int maxBlockSize = 100 * 1024 * 1024;
        MyQueue queue = new MyQueue();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        List<Future<?>> futures = new ArrayList<>();

        Future producerStatus = threadPool.submit(new ReadFileRunnable(queue, inputFile, tmpFiles, maxBlockSize));
        Future consumer1Status = threadPool.submit(new WriteFileRunnable("Write 1", queue));
        Future consumer2Status = threadPool.submit(new WriteFileRunnable("Write 2", queue));
        futures.add(producerStatus);
        futures.add(consumer1Status);
        futures.add(consumer2Status);

        for (Future<?> f : futures) {
            //System.err.println("future");
            f.get(); // get will block until the future is done
        }
        threadPool.shutdown();
        System.out.println(tmpFiles.size());
    }

    private void mergeFile(String outputFile) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        BlockingQueue<String> queue = new LinkedBlockingDeque<>(1000);
        MergeFileRunnable merge = new MergeFileRunnable(tmpFiles, queue, maxSize);
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
