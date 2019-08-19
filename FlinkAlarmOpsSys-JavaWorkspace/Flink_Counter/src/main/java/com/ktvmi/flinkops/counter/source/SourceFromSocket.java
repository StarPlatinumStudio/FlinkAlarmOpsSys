package com.ktvmi.flinkops.counter.source;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

public class SourceFromSocket extends RichFlatMapFunction<String,String> {
    @Override
    public void flatMap(String in, Collector<String> stringCollector) throws Exception {
                stringCollector.collect(in);
    }
}
