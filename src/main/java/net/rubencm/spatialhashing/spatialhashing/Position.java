package net.rubencm.spatialhashing.spatialhashing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Position {
    @NonNull private int x;
    @NonNull private int y;
}
