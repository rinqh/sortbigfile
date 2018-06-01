/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.zalotest;

import java.util.Comparator;

/**
 *
 * @author huylvq
 */
public class CompareWrapper implements Comparator<Wrapper>{

    @Override
    public int compare(Wrapper t, Wrapper t1) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return t.string.compareToIgnoreCase(t1.string);
    }
    
}
