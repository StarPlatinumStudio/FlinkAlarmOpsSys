package Source;

import Source.Custom.SourceFromSocketClient;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class tumbMain {
    public static void main(String[] args)throws Exception {

        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream=env.addSource(new SourceFromSocketClient(9001));
        DataStream<Long> counts=dataStream
                .flatMap(new FlatMapFunction<String, Long>() {
                    @Override
                    public void flatMap(String s, Collector<Long> collector) throws Exception {
                        for (String word:s.split("\\s")){
                            collector.collect(1L);
                        }
                    }
                })
                .keyBy(new KeySelector<Long, Object>() {
                    @Override
                    public Object getKey(Long aLong) throws Exception {
                        return aLong;
                    }
                })
                .timeWindow(Time.seconds(1))
                .reduce(new ReduceFunction<Long>() {
                    @Override
                    public Long reduce(Long aLong, Long t1) throws Exception {
                        return aLong+t1;
                    }
                });
        counts.print();
        env.execute("job");
    }
}
