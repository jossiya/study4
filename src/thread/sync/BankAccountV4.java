package thread.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV4 implements BankAccount {

    private int balance;

    private final Lock lock = new ReentrantLock();

    public BankAccountV4(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        lock.lock(); //ReentrantLock 이용하여 lock 걸기
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
// private final Lock lock = new ReentrantLock()을 사용하도록 선헌한다.
// synchronized(this) ept;sd[  lock.lock()을 사용해서 락을 건다.
//   lock() -> unlock()까지는 안전한 임계영역이 된다.
// 임계 영역이 끝나면 반드시! 락을 반납해야 한다. 그렇지 않으면 댜기하는 스레드가 락으 ㄹ언지 못한다.
// 따라서 lock.unlock()은 반드시 finally 블럭에 작성해야 한다. 이렇게 하면 검증에 실패해서 중간에 return을
//  호출해도 또는 중간에 예상치 못한 예외가 발생해도 lock.unlock()이 반드시 호출 된다.