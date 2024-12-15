import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class z1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //Початок вимірювання часу виконання асинхронних операцій
        long startTime = System.currentTimeMillis();

        CompletableFuture<Void> future = CompletableFuture
                //Генерація початкового масиву
                .supplyAsync(() -> {
                    long time = System.currentTimeMillis();
                    //Числа в діапазоні [1, 3] так, щоб їх сума була <= 20
                    int[] array = new Random().ints(10, 1, 3).toArray(); // Генерація масиву
                    System.out.println("Generated array: " + Arrays.toString(array));
                    System.out.println("Time for generation: " + (System.currentTimeMillis() - time) + "ms");
                    return array;
                })
                // Збільшення кожного елементу масиву на 5
                .thenApplyAsync(array -> {
                    long time = System.currentTimeMillis();
                    int[] updatedArray = Arrays.stream(array).map(n -> n + 5).toArray(); // Оновлення масиву
                    System.out.println("Updated array: " + Arrays.toString(updatedArray));
                    System.out.println("Time for update: " + (System.currentTimeMillis() - time) + "ms");
                    return updatedArray;
                })
                // Обчислення факторіалу від суми елементів обох масивів
                .thenApplyAsync(updatedArray -> {
                    long time = System.currentTimeMillis();
                    //Обчислення сум елементів обох масивів
                    int sumInitial = Arrays.stream(updatedArray).map(n -> n - 5).sum();
                    int sumUpdated = Arrays.stream(updatedArray).sum();
                    int totalSum = sumInitial + sumUpdated;
                    System.out.println("Sum of initial array: " + sumInitial);
                    System.out.println("Sum of updated array: " + sumUpdated);
                    System.out.println("Total sum: " + totalSum);
                    try {
                        //Обчислення факторіалу від загальної суми
                        BigInteger factorial = factorial(totalSum);
                        System.out.println("Factorial: " + formatLargeNumber(factorial)); // Форматування великого числа
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error in factorial calculation: " + e.getMessage());
                    }
                    System.out.println("Time for factorial calculation: " + (System.currentTimeMillis() - time) + "ms");
                    return null;
                })
                //Завершення виконання всіх завдань
                .thenRunAsync(() -> {
                    System.out.println("All tasks completed in " + (System.currentTimeMillis() - startTime) + "ms");
                });

        // Очікуємо завершення всіх асинхронних завдань
        future.get();
    }

    // Метод для обчислення факторіалу
    private static BigInteger factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        //Обчислення факторіалу з використанням BigInteger для великих чисел
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    // Метод для форматування великих чисел
    private static String formatLargeNumber(BigInteger number) {
        //Форматування чисел, перевірка для різних ступенів величини
        if (number.compareTo(BigInteger.valueOf(1_000_000_000_000_000_000L)) >= 0) {
            return number.divide(BigInteger.valueOf(1_000_000_000_000_000_000L)) + " sextillion";
        } else if (number.compareTo(BigInteger.valueOf(1_000_000_000_000_000L)) >= 0) {
            return number.divide(BigInteger.valueOf(1_000_000_000_000_000L)) + " quintillion";
        } else if (number.compareTo(BigInteger.valueOf(1_000_000_000_000L)) >= 0) {
            return number.divide(BigInteger.valueOf(1_000_000_000_000L)) + " quadrillion";
        } else if (number.compareTo(BigInteger.valueOf(1_000_000_000L)) >= 0) {
            return number.divide(BigInteger.valueOf(1_000_000_000L)) + " billion";
        } else if (number.compareTo(BigInteger.valueOf(1_000_000L)) >= 0) {
            return number.divide(BigInteger.valueOf(1_000_000L)) + " million";
        } else if (number.compareTo(BigInteger.valueOf(1_000L)) >= 0) {
            return number.divide(BigInteger.valueOf(1_000L)) + " thousand";
        } else {
            return number.toString(); // Якщо число менше за 1000, просто повертаємо його в рядковому вигляді
        }
    }
}
