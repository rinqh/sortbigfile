/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.sortbigfile.runnable;

import com.huylvq.sortbigfile.data.MyData;
import com.huylvq.sortbigfile.data.MyQueue;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rin
 */
public class WriteFileRunnable implements Runnable {

    private String name;
    private MyQueue queue;

    /**
     *
     */
    public WriteFileRunnable() {
    }

    /**
     *
     * @param name
     * @param queue
     */
    public WriteFileRunnable(String name, MyQueue queue) {
        this.name = name;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            MyData data = null;
            while (true) {
                data = queue.get();
                if (data != null) {
                    writeFile(data);
                    data.lines.clear();
                    data = null;
                }
                if (!queue.continueProducing && queue.isEmpty()) {
                    break;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(WriteFileRunnable.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    private void writeFile(MyData data) {
//        System.out.println(System.currentTimeMillis() + this.name + " start running!!");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(data.file));
            for (String s : data.lines) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(WriteFileRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(WriteFileRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        System.out.println(System.currentTimeMillis() + this.name + " done!!");
    }

}
