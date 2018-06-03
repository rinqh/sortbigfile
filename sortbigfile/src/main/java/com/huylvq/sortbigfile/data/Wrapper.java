/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.sortbigfile.data;

/**
 *
 * @author huylvq
 */
public class Wrapper {

    /**
     *
     */
    public String string;

    /**
     *
     */
    public int index;          //Use for reader stream index in readers list when merge file

    /**
     *
     * @param line
     */
    public Wrapper(String line) {
        this.string = line;
        this.index = 0;
    }
}
