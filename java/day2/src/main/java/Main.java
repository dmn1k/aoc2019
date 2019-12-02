import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Integer> state = downloadInput().stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Program program1 = Program.create(state,12, 2);
        System.out.println("Result Part 1: " + program1.run());

        for(int noun = 0; noun <= 99; noun++){
            for (int verb = 0; verb <= 99; verb++){
                Program program = Program.create(state, noun, verb);
                Integer result = program.run();
                if(result == 19690720){
                    System.out.println("Result Part 2: " + (100 * noun + verb));
                    return;
                }
            }
        }

        System.out.println("Part 2 failed");
    }

    @AllArgsConstructor
    private static class Program {
        private static Map<Integer, Operation> OPERATIONS = new HashMap<>();
        private ProgramState programState;

        static {
            OPERATIONS.put(1, new Addition());
            OPERATIONS.put(2, new Multiplication());
            OPERATIONS.put(99, new Termination());
        }

        public static Program create(List<Integer> state, Integer noun, Integer verb) {
            List<Integer> stateCopy = new ArrayList<>(state);

            stateCopy.set(1, noun);
            stateCopy.set(2, verb);

            ProgramState programState = new ProgramState(0, stateCopy);
            return new Program(programState);
        }

        public Integer run(){
            while (!programState.isFinished()) {
                Integer currentOpcode = programState.getCurrentOpcode();
                Operation operation = OPERATIONS.get(currentOpcode);
                programState = operation.operate(programState);
            }

            return programState.getEndResult();
        }

    }


    private static List<String> downloadInput() {
        try (var is = openConnection().getInputStream();
             var br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             Stream<String> lines = br.lines()) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URLConnection openConnection() {
        try {
            var urlConnection = new URL("https://adventofcode.com/2019/day/2/input").openConnection();
            urlConnection.setRequestProperty("cookie", "session=53616c7465645f5f86ede804b8f385cf9d79474ab49c70cfabb9ac64d72cebef3e43f1b0fd20accf9081fe58660b7860");

            return urlConnection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}