package thread.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV5 implements BankAccount {

    private int balance;

    private final Lock lock = new ReentrantLock();

    public BankAccountV5(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        if (!lock.tryLock()) {
            log("[진입 실패] 이미 처리중인 작업이 있습니다.");
            return false;
        }
        try{
            log("[검증 시작] 출금액: " + amount + ", 잔액: " + balance);
            if (balance < amount) {
                log("[검증 실패] 출금액: " + amount + ", 잔액: " + balance);
                return false;
            }
            // 잔고가 출금액 보다 많으면, 진행
            log("[검증 완료] 출금액: " + amount + ", 잔액: " + balance);
            sleep(1000); // 출금에 걸리는 시간으로 가정
            balance = balance - amount;
            log("[출금 완료] 출금액: " + amount + ", 잔액: " + balance);
        }finally {
            lock.unlock(); //ReentrantLock 이용하여 lock 해제
        }


        log("거래 종료");
        return true;
    }

    @Override
    public synchronized int getBalance() {
        lock.lock(); //ReentrantLock 이용하여 lock 걸기
        try{
            return balance;
        }finally {
            lock.unlock();
        }
    }
}

// t1: 먼저 락을 획득하고 임계영역을 수행한다.
// t2: 락이 없다는 것으 확인하고 lock.tryLock() 에서 즉시 빠져나온다. 이때 false가 반환된다.
// t2: "[진입 실패] 이미 처리중인 작업이 있습니다."를 출력하고 false를 반환하면서 메서드를 종료한다.
// t1: 임계 영역의 수행을 완료하고 거래를 종료한다. 마지막으로 학을 만납한다.