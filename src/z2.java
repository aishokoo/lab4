import java.util.*;
import java.util.concurrent.*;

public class z2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Random random = new Random();
        int n = 20;
        List<Integer> numbers = new ArrayList<>();

        //Генерація випадкової послідовності чисел
        for (int i = 0; i < n; i++) {
            numbers.add(random.nextInt(100) + 1); // Числа від 1 до 100
        }

        //Виведення початкової послідовності
        System.out.println("Initial sequence: " + numbers);

        //Вимірювання часу роботи асинхронних операцій
        long startTime = System.nanoTime();

        //CompletableFuture для асинхронної обробки
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Вимірюємо час до генерації сум
            long timeBeforeSums = System.nanoTime();
            // Створення списку сум сусідніх елементів
            List<Integer> sums = new ArrayList<>();
            for (int i = 0; i < numbers.size() - 1; i++) {
                sums.add(numbers.get(i) + numbers.get(i + 1));
            }

            //Виведення результату
            System.out.println("Sum of adjacent elements: " + sums);
            long timeAfterSums = System.nanoTime();
            System.out.println("Time after calculating sums: " + (timeAfterSums - timeBeforeSums) + " ns");

            // Асинхронне обчислення мінімальної суми
            CompletableFuture<Integer> minSumFuture = CompletableFuture.supplyAsync(() -> {
                return sums.stream().min(Integer::compare).orElseThrow();
            });

            // Обробка результату мінімальної суми
            minSumFuture.thenAcceptAsync(minSum -> {
                long timeAfterMinSum = System.nanoTime();
                System.out.println("Minimum sum: " + minSum);
                System.out.println("Time after calculating the minimum sum: " + (timeAfterMinSum - timeAfterSums) + " ns");
            });

            //Завершення без повернення значення
            minSumFuture.thenRunAsync(() -> {
                long timeAfterFinalRun = System.nanoTime();
                System.out.println("Time after completing all operations: " + (timeAfterFinalRun - timeAfterSums) + " ns");
            });
        });

        //Очікуємо завершення виконання асинхронних операцій
        future.get();

        //Виведення загального часу роботи
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Перетворення на мілісекунди
        System.out.println("Total time for all asynchronous operations: " + duration + " ms");
    }
}
