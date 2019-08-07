package javase;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple6;

public class TupleDemo {

public static void main(String args[]){
    Tuple2<Integer,String> tuple2=new Tuple2<>(
            1,"First"
    );
    System.out.println(tuple2.f1+tuple2.f0);
Tuple6<Integer,String,Integer,String,Tuple2<Integer,Integer>,Integer> tuple6=new Tuple6<>();
     tuple6.f0=1;
}
}
