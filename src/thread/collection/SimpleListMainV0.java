package thread.collection;

import java.util.ArrayList;
import java.util.List;

public class SimpleListMainV0 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        //스레드1, 스레드2가 동시에 실행 가정
        list.add("A");
        list.add("B");
        System.out.println(list);
    }
}
