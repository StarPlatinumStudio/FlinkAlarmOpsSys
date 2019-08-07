package WordCount_Socket;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WindowWordCount {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream=env
                .socketTextStream("localhost",9999)
                .flatMap(new Splitter());
        dataStream.print();
        env.execute("Window WordCount");
    }

public static class Splitter implements FlatMapFunction<String, String> {
    @Override
    public void flatMap(String sentence,Collector<String> out) throws Exception {
    out.collect("NM$L");
    }
}
}