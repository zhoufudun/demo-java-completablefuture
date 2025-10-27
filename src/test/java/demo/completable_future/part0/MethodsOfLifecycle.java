package demo.completable_future.part0;

import demo.completable_future.common.Demo;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class MethodsOfLifecycle extends Demo {

    @Test
    public void test() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        CompletableFuture<String> future = new CompletableFuture<>(); // creating an incomplete future

        executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(1);
            future.complete("value"); // completing the incomplete future
            return null;
        });

        while (!future.isDone()) { // checking the future for completion
            TimeUnit.SECONDS.sleep(2);
        }

        String result = future.get(); // reading value of the completed future
        logger.info("result: {}", result);

        executorService.shutdown();
    }

    @Test
    public void testAsyncSupply() throws InterruptedException, ExecutionException {

        ThreadPoolExecutor executorService =
                new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        for(int i=0;i<2;i++){
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    submitTask(executorService, finalI);
                }
            }).start();
        }
    }

    private static CompletableFuture<String> submitTask(ThreadPoolExecutor executorService,int num) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "success"+num;
        }, executorService);
    }

}
