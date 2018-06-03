/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.zalotest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rin
 */
public class SortBigFileUsingMultithreadTest {

    String input = "xyz\n"
            + "afg\n"
            + "buy\n"
            + "acc\n"
            + "nhanh vai\n"
            + "haha\n"
            + "lol\n"
            + "omegalul\n"
            + "kappa\n"
            + "monkas\n"
            + "s4head\n"
            + "hi hi";
    String inputFile = "jUnitInput.txt";
    String outputFile = "jUnitOutput.txt";

    String expectedResult = "acc\n"
            + "afg\n"
            + "buy\n"
            + "haha\n"
            + "hi hi\n"
            + "kappa\n"
            + "lol\n"
            + "monkas\n"
            + "nhanh vai\n"
            + "omegalul\n"
            + "s4head\n"
            + "xyz\n";

    public SortBigFileUsingMultithreadTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(inputFile));
            writer.write(input);
        } catch (IOException ex) {
            Logger.getLogger(SortBigFileUsingMultithreadTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(SortBigFileUsingMultithreadTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    @After
    public void tearDown() {

        File fileInput = new File(inputFile);
        if (fileInput.exists()) {
            fileInput.delete();
        }
        File fileOutput = new File(outputFile);
        if (fileOutput.exists()) {
            fileOutput.delete();
        }
    }

    /**
     * Test of sort method, of class SortBigFileUsingMultithread.
     */
    @Test
    public void testSort() {
        System.out.println("Multithread sort");
        SortBigFileUsingMultithread instance = new SortBigFileUsingMultithread(inputFile, outputFile);
        instance.sort();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(outputFile));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
//            System.out.println("Actually result: " + sb.toString());
//            System.out.println("Expected result: " +expectedResult);
            assertEquals("Right!", sb.toString(), expectedResult);
        } catch (IOException ex) {
            Logger.getLogger(SortBigFileUsingMultithreadTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(SortBigFileUsingMultithreadTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
