package thread.control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV1 {

    public static void main(String[] args) {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");
        thread1.start();
        thread2.start();
        log("tast1.result = " + task1.result);
        log("tast2.result = " + task2.result);
        int sumAll = task1.result + task2.result;
        log("tasl1 + tast2 = " + sumAll);
        log("End");
    }

    static class SumTask implements Runnable {


        int startValue ;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("작업 시작");
            sleep(2000);
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            result=sum;
            log("작업 완료 result = " + result);
        }
    }
}
/*this
* 어떤 메서드를 호출하는 것은, 정확히는 특정 스레드가 어떤 메서드를 호출하는것이다.
* 스레드는 메서드의 호출을 관리하기 위해 메서드 단위로 스택 프레임을 만들고 해당 스택 프레임을 쌓아 올린다
* 이떄 인스턴스의 메서드를 호출하면, 어떤 인스턴스의 메서드를 호출했는지 기억하기 위해,
*  해당 인스턴스의 참조값을 스택 프레임 내부에 저장해둔다.이것이 바로 우리가 자주 사용하던 this 이다.
*
* 특정 메서드 안에서 thjis를 포툴하면 바로 스택프레임 안에 있는  this 값을 불러서 사용하게 된다.
* 이로 인해서 thread-1, thread-2는 자신의 인스턴스를 구분해서 사용할 수 있다.*/