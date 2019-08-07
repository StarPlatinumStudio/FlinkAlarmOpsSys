package javase;

import java.util.ArrayList;
import java.util.List;

public class Autoboxing {
    public static void main(String args[]){
        /**
         * 自动装箱：
         */
        List<Integer>list=new ArrayList<>();
        list.add(1);//这里的【1】是int类型，编译器把 int 转换为 Integer
        list.add(Integer.valueOf(1));//实际是这样
        /**
         * 拆箱:
         */
        int intNumber=list.get(0);//与自动装箱相反的，在这段代码中，编译器自动把 Integer 对象转换为 int 基本数据类型
        intNumber=list.get(0).intValue();//手动拆箱
        System.out.println(intNumber);
    }
}
