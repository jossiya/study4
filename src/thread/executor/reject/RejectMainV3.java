package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectMainV3 {

    //작업을 거절 한 대신에 요청한 스레드에 대신 일을 시킨다.
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        executor.submit(new RunnableTask("task1"));
        executor.submit(new RunnableTask("task2"));
        executor.submit(new RunnableTask("task3"));
        executor.submit(new RunnableTask("task4"));
        executor.close();
    }
}
// 생산자가 너무 빠를때  생산자의 속도를 조절 할 수 있다.