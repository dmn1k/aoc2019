package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputDownloader {
    public static List<String> downloadInput(int day) {
        try (var is = openConnection(day).getInputStream();
             var br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             Stream<String> lines = br.lines()) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URLConnection openConnection(int day) {
        try {
            var urlConnection = new URL("https://adventofcode.com/2019/day/" + day + "/input").openConnection();
            urlConnection.setRequestProperty("cookie", "session=53616c7465645f5f86ede804b8f385cf9d79474ab49c70cfabb9ac64d72cebef3e43f1b0fd20accf9081fe58660b7860");

            return urlConnection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Long> downloadLongList(int day) {
        return downloadInput(day).stream()
                    .flatMap(input -> Arrays.stream(input.split(",")))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
    }
}
