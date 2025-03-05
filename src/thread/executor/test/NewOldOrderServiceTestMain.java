package thread.executor.test;

import java.util.concurrent.ExecutionException;

public class NewOldOrderServiceTestMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String orderNo = "Order#1234";  // 예시 주문 번호
        NewOldOrderService orderService = new NewOldOrderService();
        orderService.order(orderNo);
    }

}