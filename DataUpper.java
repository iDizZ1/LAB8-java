import java.util.List;
import java.util.stream.Collectors;


public class DataUpper {
    @DataProcessor
    public List<String> transformData(List<String> data) {
        return data.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}