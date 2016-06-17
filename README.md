# BloomFilter
Bloom Filter &amp;&amp; Count Bloom Filter &amp;&amp; Cached Bloom Filter
[![Build Status](https://travis-ci.org/letcheng/BloomFilter.svg?branch=master)](https://travis-ci.org/letcheng/BloomFilter)

## Bloom Filter

- 一个Bloom Filter的Java实现，采用Java的散列机制

- 与Google Guava的性能有些差距，有时间再优化：

  + Ruyu Bloom Filter添加元素: 0.943s, 53022.269353128315 元素/s
  + Google Bloom Filter添加元素: 0.513s, 97465.88693957115 元素/s
  + Ruyu Bloom Filter测试已经存在的元素: 0.907s, 55126.79162072767 元素/s
  + Google Bloom Filter测试已经存在的元素: 0.494s, 101214.57489878543 元素/s
  + Ruyu Bloom Filter 测试不存在的元素: 0.519s, 96339.11368015414 元素/s
  + Google Bloom Filter 测试不存在的元素: 0.473s, 105708.24524312897 元素/s


## Count Bloom Filter

- 可以进行移除元素的Bloom Filter数据结构实现

## Cached Bloom Filter

- 可以进行高效缓存替换的Bloom Filter数据结构，可以应用于爬虫的URL去重中，在以雪球爬行策略过程中，一个爬虫线程在一定时间范围内遇到的链接集中在一个URL集合中的。这时可以采用CachedBloomFilter数据结构。
