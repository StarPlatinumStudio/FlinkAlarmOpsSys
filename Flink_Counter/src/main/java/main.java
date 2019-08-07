import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class main {
    public static void main(String[] args) throws Exception{
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();



            //DataStream<String> stream1=env.addSource(new SourceFromSocketClient(9001));
           // DataStream<String> stream2=env.addSource(new SourceFromSocketClient(9002));

       DataStream<WordWithCount> stream=env
               .socketTextStream("127.0.0.1",9001,"\n")
                //.addSource(new SourceFromSocketClient(9001))
                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    public void flatMap(String s, Collector<WordWithCount> collector) throws Exception {
                        for (String word:s.split("\\s")){
                    collector.collect(new WordWithCount(s,1L));
                        }
                    }
                })
                .keyBy("word")
               .timeWindow(Time.seconds(5),Time.seconds(1))
               .reduce(new ReduceFunction<WordWithCount>() {
                   @Override
                   public WordWithCount reduce(WordWithCount a,WordWithCount b) throws Exception {
                       return new WordWithCount(a.word,a.count+b.count);
                   }
               });
stream.print();
        env.execute("StreamExecutionEnvironment Execute");


    }
//    public static class Splitter implements FlatMapFunction<String, String> {
//            public void flatMap(String in, Collector<String> out) throws Exception {
//                out.collect(in);}}

    // Data type for words with count
    public static class WordWithCount {

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
