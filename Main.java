import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();
        dataManager.registerDataProcessor(new DataFilter());
        dataManager.registerDataProcessor(new DataDublicates());
        dataManager.registerDataProcessor(new DataUpper());

        List<String> data = dataManager.loadData("input.txt");

        List<String> processedData = dataManager.processData(data);

        dataManager.saveData(processedData, "output.txt");

        processedData.forEach(System.out::println);
    }
}
