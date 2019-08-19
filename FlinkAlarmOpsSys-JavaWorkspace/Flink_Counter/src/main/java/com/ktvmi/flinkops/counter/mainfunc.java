package com.ktvmi.flinkops.counter;

import com.ktvmi.flinkops.counter.source.SourceFromSocketClient;
import com.ktvmi.flinkops.counter.entity.WordWithCount;
import com.ktvmi.flinkops.counter.sink.HttpSink;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class mainfunc {
    public static void main(String[] args) throws Exception {
        //9001 http://127.0.0.1
        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStream<String> text = env.addSource(new SourceFromSocketClient(Integer.parseInt(args[0])));

        // parse the data, group it, window it, and aggregate the counts
        DataStream<WordWithCount> windowCounts = text

                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new WordWithCount(word, 1));
                        }
                    }
                })

                .keyBy("word")
                .timeWindow(Time.seconds(5))

                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.getWord(), a.getCount() + b.getCount());
                    }
                });

        // print the results with a single thread, rather than in parallel
        windowCounts.addSink(new HttpSink(1001,5,args[1]));

        env.execute("Socket Window WordCount");
    }

}
