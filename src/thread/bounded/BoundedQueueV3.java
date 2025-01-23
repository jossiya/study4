package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BoundedQueueV3 implements BoundedQueue {
    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV3(int max) {
        this.max = max;
    }

    @Override
    public synchronized void put(String data) {
        while (queue.size() == max) {
            log("[put] 큐가 가득 참, 생산자 대기");
            try {
                wait(); //RUNNABLE -> WAITING, 락 반납
                log("[put] 생산자 깨어남");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        queue.offer(data);
        log("[put] 생산자 대이터 저장, notify() 호출");
        notify(); // 대기 스레드, WAIT -> BLOCKED
    }

    @Override
    public synchronized String take() {
        while (queue.isEmpty()) {
            log("[take] 큐에 데이터가 없음, 소비자 대기");
            try {
                wait();
                log("[take] 소비자 깨어남");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String data = queue.poll();
        log("[take] 소비자 데이터 획득, notify() 호출");
        notify(); // 대기 쓰레드, WAIT -> BLOCKED
        return data;
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}

// 생산자 쓰레드는 데이터를 생성하고, 대기중인 소비자 스레드에게 알려주어야 한다. 소비자 스레드는 데이터를 소비자고, 대기중인 생산자 스레드에게 알려주어야 한다.
// 소비자 스레드는 데이터를 소비자고, 대기중인 생산자 스레드에게 알려주어야 한다. 하지만 스레드 대기 집합은 하나이고 이안에 생산자 스레드와 소비자 스레드가 함께 대기한다.
// 그리고 notify()는 원하는 목표를 지정할 수 없었다. 물론 notifyAll()을 사용할 수 있지만, 원하지 않는 모든 스레드가 함께 대기한다. 그리고 notify()는 원하는 목표를 자정할 수 없었다. notifyAll()을
// 사용할 수 있지만, 원하지 않는 모든 쓰레드까지 모두 깨어난다.