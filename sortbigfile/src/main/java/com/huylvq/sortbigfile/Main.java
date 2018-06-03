/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.sortbigfile;

import java.io.File;

/**
 *
 * @author Rin
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Invalid input!");
            System.err.println("USAGE: java -jar target/sortbigfile/sortbigfile-1.0-SNAPSHOT.jar inputfile outputfile");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];
        File file = new File(inputFile);
        if (!file.exists() || file.isDirectory()) {
            System.err.println(inputFile + " does not exist! Exitting!");
            return;
        }
        System.out.println("Running...Please wait...");
        //SortBigFile sorter = new SortBigFile(inputFile, outputFile);
        SortBigFileUsingMultithread sorter = new SortBigFileUsingMultithread(inputFile, outputFile);
        sorter.sort();
        System.out.println("Done!");
    }
}
