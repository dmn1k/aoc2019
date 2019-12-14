package day14;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Reaction {
    private Chemical target;
    private List<Chemical> ingredients;
}
