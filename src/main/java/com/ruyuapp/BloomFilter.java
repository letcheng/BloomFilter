package com.ruyuapp;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Collection;

/**
 *
 * Bloom Filter数据结构实现
 *
 * @author Letcheng on 2016/2/23.
 */
public class BloomFilter<E>{

    protected BitSet bitset;
    protected int m; //Bloom Filter的位数
    protected double c; //每个元素的位数
    protected int n_max;  //Bloom Filter最大的元素个数
    protected int n; // Bloom Filter实际元素的个数
    protected int k; // hash函数的个数

    public static final Charset charset = Charset.forName("UTF-8"); // encoding used for storing hash values as strings

    //在大多数情况下MD5准确率较好，也可以选择SHA1
    public static final String hashName = "MD5";
    public static final MessageDigest digestFunction;


    static {
        MessageDigest tmp;
        try {
            tmp = MessageDigest.getInstance(hashName);
        } catch (NoSuchAlgorithmException e) {
            tmp = null;
        }
        digestFunction = tmp;
    }

    /**
      * @param c
      * @param n_max
      * @param k
      */
    public BloomFilter(double c, int n_max, int k) {
      this.n_max = n_max;
      this.k = k;
      this.c = c;
      this.m = (int)Math.ceil(c * n_max);
      n = 0;
      this.bitset = new BitSet(m);
    }

    public BloomFilter(int m,int n_max,int k){
        this.n_max = n_max;
        this.k = k;
        this.m = m;
        this.c = 1.0d * m / n_max;
    }

    /**
     *
     * 根据m和n_max计算k的最优值
     * 根据论文的推导：k = lg2*(m/n_max)
     * @param m
     * @param n_max
     */
    public BloomFilter(int m, int n_max) {
        this(m / (double) n_max,
                n_max,
                (int) Math.round((m / (double) n_max) * Math.log(2.0))); //k = log2*(m/n_max)
    }


    /**
     * 最常用的构造方法
     * @param fpp
     * @param n_max
     */
    public BloomFilter(double fpp, int n_max) {
        this(Math.ceil(-(Math.log(fpp) / Math.log(2))) / Math.log(2), // c = k / ln(2)
                n_max,
                (int) Math.ceil(-(Math.log(fpp) / Math.log(2)))); // k = ceil(-lg_2(fpp))
    }

    /**
     *
     * 根据Hash的个数，生成散列值
     * @param data
     * @param hashes
     * @return
     */
    public static int[] createHashes(byte[] data, int hashes) {
        int[] result = new int[hashes];

        int k = 0;
        byte salt = 0;
        while (k < hashes) {
            byte[] digest;
            synchronized (digestFunction) {
                digestFunction.update(salt);
                salt++;
                digest = digestFunction.digest(data);                
            }

            for (int i = 0; i < digest.length/4 && k < hashes; i++) {
                int h = 0;
                for (int j = (i*4); j < (i*4)+4; j++) {
                    h <<= 8;
                    h |= ((int) digest[j]) & 0xFF;
                }
                result[k] = h;
                k++;
            }
        }
        return result;
    }

    /**
     * 计算在插入最大元素的情况下的误判率
     * @return
     */
    public double maxFpp() {
        return getFpp(n_max);
    }

    /**
     * 根据当前的元素计算误判率
     * @param n
     * @return
     */
    public double getFpp(double n) {
        // (1 - e^(-k * n / m)) ^ k
        return Math.pow((1 - Math.exp(-k * (double) n
                / (double) m)), k);

    }

    /**
     * 计算当前元素个数的误判率
     * @return
     */
    public double getFpp() {
        return getFpp(n);
    }


    public int getK() {
        return k;
    }

    /**
     * 重置Bloom Filter
     */
    public void clear() {
        bitset.clear();
        n = 0;
    }

    /**
     *
     * 添加对象到Bloom Filter中，会调用对象的toString()方法作为Hash方法的输入
     * @param element
     */
    public void add(E element) {
       add(element.toString().getBytes(charset));
    }

    /**
     * 添加字节数组到Bloom Filter中
     * @param bytes
     */
    public void add(byte[] bytes) {
       int[] hashes = createHashes(bytes, k);
       for (int hash : hashes)
           bitset.set(Math.abs(hash % m), true); //使用K个Hash函数映射到1位
       n++;//添加了一个元素
    }

    /**
     * 添加一个对象集合到Bloom Filter中
     * @param c
     */
    public void addAll(Collection<? extends E> c) {
        for (E element : c)
            add(element);
    }

    /**
     *
     * 获取某个对象是否已经插入到Bloom Filter中，可以使用getFpp()方法计算结果正确的概率
     *
     * @param element
     * @return
     */
    public boolean contains(E element) {
        return contains(element.toString().getBytes(charset));
    }

    /**
     * 判定某个字节数组是否已经插入到Bloom Filter中，可以使用getFpp()方法计算结果正确的概率
     * @param bytes
     * @return
     */
    public boolean contains(byte[] bytes) {
        int[] hashes = createHashes(bytes, k);
        for (int hash : hashes) {
            if (!bitset.get(Math.abs(hash % m))) { //如果有一位未设置，则该元素未插入，但是返回true，并不代表这个元素一定插入过，即存在误判率的概念。
                return false;
            }
        }
        return true;
    }

    /**
     * 如果有一个元素未被插入到Bloom Filter中，则返回false
     * @param c elements to check.
     * @returnr.
     */
    public boolean containsAll(Collection<? extends E> c) {
        for (E element : c)
            if (!contains(element))
                return false;
        return true;
    }

    /**
     * 获取Bloom Filter中某一位的值
     * @param bit
     * @return
     */
    public boolean getBit(int bit) {
        return bitset.get(bit);
    }

    /**
     * 设置Bloom Filter每一位的值
     * @param bit
     * @param value true代表该位已经被设置，false代表未进行设置
     */
    public void setBit(int bit, boolean value) {
        bitset.set(bit, value);
    }

    public BitSet getBitSet() {
        return bitset;
    }

    /**
     * 获取当前的位数
     * @return
     */
    public int size() {
        return this.m;
    }

    /**
     * 获取当前的插入的元素的个数
     * @return
     */
    public int count() {
        return this.n;
    }

    /**
     * 获取Bloom Filter可以插入的最大元素
     * @return
     */
    public int getNMax() {
        return n_max;
    }

    /**
     *
     * 当Bloom Filter满的时候，每个元素占的位数，通过构造方法进行设置
     * @return
     */
    public double getC() {
        return this.c;
    }

    /**
     * 获取当前情况下，Bloom Filter实际上每个元素占的位数
     * @return
     */
    public double getBitsPerElement() {
        return this.m / (double)n;
    }
}