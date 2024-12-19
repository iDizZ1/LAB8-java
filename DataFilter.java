import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataFilter {
    @DataProcessor
    public List<String> filterData(List<String> data) {
        return data.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
    }
}

