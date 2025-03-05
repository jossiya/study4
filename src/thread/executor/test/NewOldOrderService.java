package thread.executor.test;

import java.util.List;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class NewOldOrderService {
    private final ExecutorService es = Executors.newFixedThreadPool(10);

    public void order(String orderNo) throws InterruptedException {
        List<Callable<Boolean>> tasks = List.of(
                new InventoryWork(orderNo),
                new ShippingWork(orderNo),
                new AccountingWork(orderNo)
        );

        try {
            for (Future<Boolean> result : es.invokeAll(tasks)) {
                try {
                    if (!result.get()) {
                        log("일부 작업이 실패했습니다: " + orderNo);
                    }
                } catch (ExecutionException e) {
                    log("작업 실패: " + orderNo + " - " + e.getMessage());
                }
            }
        } finally {
            log("모든 주문 처리가 성공적으로 완료되었습니다.");
        }
    }

    public void shutdown() {
        es.shutdown();
        try {
            if (!es.awaitTermination(5, TimeUnit.SECONDS)) {
                es.shutdownNow();
            }
        } catch (InterruptedException e) {
            es.shutdownNow();
        }
    }

    static class InventoryWork implements Callable<Boolean> {
        private final String orderNo;

        public InventoryWork(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        public Boolean call() {
            log("재고 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class ShippingWork implements Callable<Boolean> {
        private final String orderNo;

        public ShippingWork(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        public Boolean call() {
            log("배송 시스템 알림: " + orderNo);
            sleep(1000);
            return true;
        }
    }

    static class AccountingWork implements Callable<Boolean> {
        private final String orderNo;

        public AccountingWork(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        public Boolean call() {
            log("회계 시스템 업데이트: " + orderNo);
            sleep(1000);
            return true;
        }
    }
}
