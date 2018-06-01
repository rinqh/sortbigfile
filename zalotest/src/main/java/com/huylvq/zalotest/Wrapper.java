/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.zalotest;

/**
 *
 * @author huylvq
 */
public class Wrapper {

    String string;
    int index;          //Use for reader stream index in readers list when merge file

    Wrapper(String line) {
        this.string = line;
        this.index = 0;
    }
}
