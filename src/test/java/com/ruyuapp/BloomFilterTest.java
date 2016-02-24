package com.ruyuapp;

import com.google.common.hash.Funnels;
import com.ruyuapp.BloomFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Letcheng on 2016/2/23.
 */
public class BloomFilterTest {

    private int total = 50000; //测试元素的总数

    private List<String> existingElements = null;
    private List<String> nonExistingElements = null;

    private void printStat(long start, long end) {
        double diff = (end - start) / 1000.0;
        System.out.println(diff + "s, " + (total / diff) + " 元素/s");
    }

    @Before
    public void prepare(){

        final Random r = new Random();
        existingElements = new ArrayList(total);
        for (int i = 0; i < total; i++) {
            byte[] b = new byte[200];
            r.nextBytes(b);
            existingElements.add(new String(b));
        }

        nonExistingElements = new ArrayList(total);
        for (int i = 0; i < total; i++) {
            byte[] b = new byte[200];
            r.nextBytes(b);
            nonExistingElements.add(new String(b));
        }

    }

    @Test
    public void test(){

        double fpp = 0.001d;

        BloomFilter<String> ruyu_bf = new BloomFilter(fpp, total);
        com.google.common.hash.BloomFilter<String> google_bf = com.google.common.hash.BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), total, fpp);

        // 添加元素
        System.out.print("Ruyu Bloom Filter添加元素: ");
        long start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            ruyu_bf.add(existingElements.get(i));
        }
        long end = System.currentTimeMillis();
        printStat(start, end);

        System.out.print("Google Bloom Filter添加元素: ");
        start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            google_bf.put(existingElements.get(i));
        }
        end = System.currentTimeMillis();
        printStat(start, end);

        System.out.print("Ruyu Bloom Filter测试已经存在的元素: ");
        start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            ruyu_bf.contains(existingElements.get(i));
        }
        end = System.currentTimeMillis();
        printStat(start, end);

        System.out.print("Google Bloom Filter测试已经存在的元素: ");
        start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            ruyu_bf.contains(existingElements.get(i));
        }
        end = System.currentTimeMillis();
        printStat(start, end);


        System.out.print("Ruyu Bloom Filter 测试不存在的元素: ");
        start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            ruyu_bf.contains(nonExistingElements.get(i));
        }
        end = System.currentTimeMillis();
        printStat(start, end);

        System.out.print("Google Bloom Filter 测试不存在的元素: ");
        start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            ruyu_bf.contains(nonExistingElements.get(i));
        }
        end = System.currentTimeMillis();
        printStat(start, end);

    }

    @Test
    public void test2(){
        CachedBloomFilter<String> cbf = new CachedBloomFilter<String>(0.01,100);
    }

}
