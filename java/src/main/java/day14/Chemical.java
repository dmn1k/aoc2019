package day14;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(of = "name")
@ToString
@AllArgsConstructor
@Getter
public class Chemical {
    private long quantity;
    private String name;

    public static Chemical parse(String input) {
        String[] split = input.trim().split("\\s");
        return new Chemical(Integer.parseInt(split[0]), split[1]);
    }
}
