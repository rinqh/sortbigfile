/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.sortbigfile.data;

import java.io.File;
import java.util.List;

/**
 *
 * @author Rin
 */
public class MyData {

    /**
     *
     */
    public List<String> lines;

    /**
     *
     */
    public File file;

    /**
     *
     * @param lines
     * @param file
     */
    public MyData(List<String> lines, File file) {
        this.lines = lines;
        this.file = file;
    }
    
    
    
}
