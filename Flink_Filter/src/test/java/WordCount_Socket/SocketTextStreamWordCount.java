package WordCount_Socket;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
public class SocketTextStreamWordCount {
    public static void main(String[] args) throws Exception{
        if (args.length!=2){
            System.err.println("USAGE:\n缺少启动参数:<hostname><port>\n尝试带参数启动：original-word-count-1.0-SNAPSHOT.jar 127.0.0.1 9000\n");
        return;
        }
        String hostname=args[0];
        Integer port=Integer.parseInt(args[1]);
        //设置流处理环境：set up the streaming execution environment
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        //获取数据
        DataStreamSource<String> streamSource=env.socketTextStream(hostname,port);

        //Flink Data transformation
        //计数
        SingleOutputStreamOperator<Tuple2<String,Integer>> sum=streamSource.flatMap(new LineSplitter())
                .keyBy(0)
                .sum(1);
        sum.print();
        env.execute("Java WordCount from SocketTextStream Example");
    }
    public static final class LineSplitter implements FlatMapFunction<String,Tuple2<String,Integer>>{
        @Override
        public void flatMap(String s,Collector<Tuple2<String,Integer>> collector){
            String[] tokens=s.toLowerCase().split("\\W+");
            for(String token:tokens){
                if(token.length()>0){
                    collector.collect(new Tuple2<String, Integer>(token,1));
                }
            }
        }
    }
}
