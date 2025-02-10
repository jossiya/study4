package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

public class BoundedQueueV5 implements BoundedQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();
    private final Condition consumerCond = lock.newCondition();

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;

    public BoundedQueueV5(int max) {
        this.max = max;
    }

    @Override
    public void put(String data) {
        lock.lock();
        try {
            while (queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                try {
                    producerCond.await();
                    log("[put] 생산자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.offer(data);
            log("[put] 생산자 데이터 저장, consumerCond.signal() 호출");
            consumerCond.signal();
        } finally{
            lock.unlock();
        }
    }

    @Override
    public String take() {
        lock.lock();
        try{
            while (queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                try {
                    consumerCond.await();
                    log("[take] 소비자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String data = queue.poll();
            log("[take] 소비자 데이터 획득, producerCond.signal() 호출");
            producerCond.signal();
            return data;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}

// Object.notify()
// 대기 중인 스레드 중 임의의 하나를 선택해서 꺄운다. 스레드가 꺠어나는 순서는 정의되어 있지 않으며, JVM 구현에 따라 다르다. 보통은 먼저 들어온 스레드가 먼저 수행되지만 구현에 따라 다르다.
// 보텅은 먼저 들어온 스레드가 먼저 수행되지만 구현에 따라 다를 수 있다.
// synchronized 블록 내에서 모니터 락을 가지고 있는 스레드가 호출해야 한다.

// Condition.signal()
// 대기 중인 스레드 중 하나를 깨우며, 일반적으로 FIFO 순서로 깨운다. 이 부분은 자바 버전과 구현에 따라 딸라질 수 있지만, 보통 Condition의 구현은 Queue 구조를 사용하기 떄문에 FIFO 순서로 꺠운다.
// ReentrantLock을 가지고 있는 쓰레드가 호출해야 한다.