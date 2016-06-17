package com.ruyuapp;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * 可以进行替换策略的Bloom Filter数据结构实现
 *
 * @author Letcheng on 2016/2/24.
 */
public class CachedBloomFilter<E> extends CountBloomFilter<E> {

    private double t;
    private Queue<E> elements = new LinkedBlockingQueue<E>(); // 对应CBF存储的元素

    public CachedBloomFilter(int m,int n_max,int k){
        super(m,n_max,k);
        t = m/14.5;
    }

    public CachedBloomFilter(double fpp, int n_max) {
        super(fpp, n_max);
        t = m/14.5;
    }

    @Override
    public void add(E element) {
        elements.add(element);
        super.add(element);
        if(c_m > t){ // 执行移除策略
            int tmp = n/3;
            for(int i=0;i<tmp;i++){
                super.remove(elements.poll());
            }
        }
    }
}
