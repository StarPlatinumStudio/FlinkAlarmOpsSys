import Source.Custom.SourceFromSocketClient;
import Source.Custom.SourceFromSocketClient;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
public class SocketWindowCount {

    public static void main(String[] args)throws Exception {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> dataStream=env.addSource(new SourceFromSocketClient(9001));
        DataStream<WordWithCount> counts=dataStream
                .flatMap(new FlatMapFunction<String,WordWithCount>() {
                    @Override
                    public void flatMap(String s, Collector<WordWithCount> collector) throws Exception {
                        for (String word:s.split("\\s")){
                            collector.collect(new WordWithCount(word,1L));
                        }
                    }
                })
                .keyBy("word")
//                .keyBy(new KeySelector<WordWithCount, Object>() {
//                    @Override
//                    public Object getKey(WordWithCount wordWithCount) throws Exception {
//                        return wordWithCount;
//                    }
//                })
                .timeWindow(Time.seconds(5))
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a,WordWithCount b) throws Exception {
                        return new WordWithCount(a.word,a.count+b.count);
                    }
                });
        counts.print();
        env.execute("job");
    }
    /**
     * Data type for words with count.
     */
  private static class WordWithCount {

        public String word;
        public long count;

        public WordWithCount() {}

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}