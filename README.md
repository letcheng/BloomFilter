## BloomFilter
[![Build Status](https://travis-ci.org/letcheng/BloomFilter.svg?branch=master)](https://travis-ci.org/letcheng/BloomFilter)
[![Release](https://jitpack.io/v/letcheng/BloomFilter.svg)](https://jitpack.io/#letcheng/BloomFilter)

Bloom Filter &amp;&amp; Count Bloom Filter &amp;&amp; Cached Bloom Filter

### Bloom Filter

- 原理 
 
 ![image](https://github.com/letcheng/BloomFilter/raw/master/src/main/resources/bloom-filter.JPG)


- 性能
  + 添加元素: 0.943s, 53022.269353128315 元素/s
  + 测试已经存在的元素: 0.907s, 55126.79162072767 元素/s
  + 测试不存在的元素: 0.519s, 96339.11368015414 元素/s

### Cached Bloom Filter

- 原理 
  
   ![image](https://github.com/letcheng/BloomFilter/raw/master/src/main/resources/cached-bloom-filter.JPG)
  
- 可以进行高效缓存替换的Bloom Filter数据结构，可以应用于爬虫的URL去重中，在以雪球爬行策略过程中，一个爬虫线程在一定时间范围内遇到的链接集中在一个URL集合中的。这时可以采用CachedBloomFilter数据结构。
