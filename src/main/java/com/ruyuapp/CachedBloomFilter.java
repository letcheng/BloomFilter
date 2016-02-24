package com.ruyuapp;

/**
 *
 * 可以进行替换策略的Bloom Filter数据结构实现
 *
 * @author Letcheng on 2016/2/24.
 */
public class CachedBloomFilter<E> extends CountBloomFilter<E> {

    private double t;

    public CachedBloomFilter(int m,int n_max,int k){
        super(m,n_max,k);
    }

    public CachedBloomFilter(double fpp, int n_max) {
        super(fpp, n_max);
        t = (1.0d * super.m / (Math.log(1.0d/fpp)/Math.log((double)2)));
    }

    @Override
    public void add(byte[] bytes) {
        if(n*c > t){ // 执行替换策略，移除m/3个元素
            //相关代码根据具体情况
        }
        super.add(bytes);
    }
}
