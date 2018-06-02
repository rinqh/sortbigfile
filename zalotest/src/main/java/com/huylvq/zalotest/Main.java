/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.zalotest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rin
 */
public class Main {

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("Invalid input!");
                return;
            }
            String inputFile = args[0];
            String outputFile = args[1];
//        System.out.println(inputFile);
//        System.out.println(outputFile);
//        File file = new File(".");
//        for (String fileNames : file.list()) {
//            System.out.println(fileNames);
//        }
//        System.out.println("Working Directory = "
//                + System.getProperty("user.dir"));
            File file = new File(inputFile);
            if (!file.exists() || file.isDirectory()) {
                System.err.println(inputFile + " does not exist! Exitting!");
                return;
            }
            SortBigFile sort = new SortBigFile();
            sort.sort(inputFile, outputFile);
        } catch (InterruptedException | ExecutionException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
