# Serializers
Looking into different options for moving away from java ser/deser 

This was my first attempt at a jmh benchmark. I was writing some timing tests comparing standard java ser/deser with json serialization and gzipped json and I wanted to make sure I had a sound benchmarking framework under me. Managing the timing and jvm warming and worrying about things like triggering garbage collection worried me. 

Please feel free to download this and run it but be warned a full run takes about 20 minutes using the default jmh configs.

# Directions
I followed the directions here
http://openjdk.java.net/projects/code-tools/jmh/

I used the command line path generating an archetype. Yeah it's old school but they "strongly recommend new users make use of the archetype to setup the correct environment."

# Results
Here's information from my latest run:
```
JMH 1.17.3 (released 21 days ago)
VM version: JDK 1.8.0_45, VM 25.45-b02
VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/bin/java
VM options: <none>
Warmup: 20 iterations, 1 s each
Measurement: 20 iterations, 1 s each
Timeout: 10 min per iteration
Threads: 1 thread, will synchronize iterations
Benchmark mode: Average time, time/op


(omitted warmup and iteration output)

Result "testJavaSerialization":
  7.455 ±(99.9%) 0.032 ms/op [Average]
  (min, avg, max) = (7.268, 7.455, 7.945), stdev = 0.137
  CI (99.9%): [7.423, 7.488] (assumes normal distribution)

Result "testJsonGzipped":
  27.210 ±(99.9%) 0.055 ms/op [Average]
  (min, avg, max) = (26.736, 27.210, 28.752), stdev = 0.232
  CI (99.9%): [27.155, 27.265] (assumes normal distribution)

Result "testJsonSerialization":
  3.198 ±(99.9%) 0.074 ms/op [Average]
  (min, avg, max) = (2.931, 3.198, 4.234), stdev = 0.314
  CI (99.9%): [3.124, 3.272] (assumes normal distribution)

# The Breakdown

Run complete. Total time: 00:20:25

Benchmark                          Mode  Cnt   Score   Error  Units
MyBenchmark.testJavaSerialization  avgt  200   7.455 ± 0.032  ms/op
MyBenchmark.testJsonGzipped        avgt  200  27.210 ± 0.055  ms/op
MyBenchmark.testJsonSerialization  avgt  200   3.198 ± 0.074  ms/op
```

# Takeaway
1. Building a benchmark framework is hard and it was way easier to implement theirs than try to manage the timing, jvm warmups, and getting the math right to get good averages
2. Gzip is super slow, though ubiquitous
