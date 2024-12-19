import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DataManager {
    private final List<Object> dataProcessors = new ArrayList<>();

    public void registerDataProcessor(Object processor) {
        dataProcessors.add(processor);
    }

    public List<String> loadData(String source) throws IOException {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        }
        return data;
    }

    public List<String> processData(List<String> data) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Callable<List<String>>> tasks = new ArrayList<>();

        for (Object processor : dataProcessors) {
            for (Method method : processor.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(DataProcessor.class)) {
                    tasks.add(() -> {
                        try {
                            return (List<String>) method.invoke(processor, data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Collections.emptyList();
                        }
                    });
                }
            }
        }

        List<Future<List<String>>> futures = executorService.invokeAll(tasks);

        List<String> result = new ArrayList<>();
        for (Future<List<String>> future : futures) {
            result.addAll(future.get());
        }

        executorService.shutdown();
        return result;
    }

    public void saveData(List<String> data, String destination) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
