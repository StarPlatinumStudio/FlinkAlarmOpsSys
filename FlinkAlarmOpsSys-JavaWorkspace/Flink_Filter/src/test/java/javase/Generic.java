package javase;

public class Generic {
    //Generics class:
    //类型参数只能代表引用型类型，不能是原始类型（像int,double,char的等）

    public static void main(String args[]){
        Integer[] intArray={1,2,3,4,5};
        Character[] charArray={'A','B','C','D','E'};
        String[] strArray={"Hello","World"};
        printArray(intArray);
        printArray(charArray);
        printArray(strArray);
        Runnable x = new Runnable() {
            @Override
            public void run() {
                System.out.println(this.getClass());
            }
        };
        x.run();
//       Box<Integer> integerBox=new Box<>(1){
//           @Override
//           public void box(){
//               System.out.println(get());
//           }
//       };
//       integerBox.box();

    }

    public static <E> void printArray(E[] inputArray){
        for (E element:inputArray){
            System.out.printf("%s",element);
        }
        System.out.println();
    }
}
abstract class Box<T>{
    public Box(T t){
        this.t=t;
    }
    private T t;
    public void add(T t){
        this.t=t;
    }
    public T get(){
        return t;
    }
    abstract void box();
}