/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.sortbigfile.data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Rin
 */
public class MyQueue {

    /**
     *
     */
    public BlockingQueue<MyData> queue = new LinkedBlockingDeque<>(5);

    /**
     *
     */
    public Boolean continueProducing = Boolean.TRUE;

    /**
     *
     * @param data
     * @throws InterruptedException
     */
    public void put(MyData data) throws InterruptedException {
        this.queue.put(data);
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    public MyData take() throws InterruptedException {
        return this.queue.take();
    }
    
    /**
     *
     * @return
     * @throws InterruptedException
     */
    public MyData get() throws InterruptedException {
        return this.queue.poll(1, TimeUnit.SECONDS);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
