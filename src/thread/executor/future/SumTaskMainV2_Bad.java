package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;

public class SumTaskMainV2_Bad {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SumTaskMainV2.SumTask task1 = new SumTaskMainV2.SumTask(1, 50);
        SumTaskMainV2.SumTask task2 = new SumTaskMainV2.SumTask(51, 100);
        ExecutorService es = Executors.newFixedThreadPool(2);
        Integer sum1 = es.submit(task1).get(); // 2초
        Integer sum2 = es.submit(task2).get(); // 2초
        
        //future 을 사용하지 않으면 결과를 받을때까지 blocking이 되어 기다려야 한다.
        //싱글 스레드랑 같아짐


        log("task1.result = " + sum1);
        log("task2.result = " + sum2);

        int sumAll = sum1 + sum2;
        log("task1 + task2 = " + sumAll);
        log("end");
        es.close();
    }

    static class SumTask implements Callable<Integer> {
        int startValue;
        int endValue;

        public SumTask(int startValue, int entValue) {
            this.startValue = startValue;
            this.endValue = entValue;
        }

        @Override
        public Integer call() throws Exception {
            log("작업 시작");
            Thread.sleep(2000);
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            log("작업 완료 result = " + sum);
            return sum;
        }
    }
}
