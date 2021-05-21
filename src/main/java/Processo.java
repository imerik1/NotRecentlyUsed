import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Processo {
    private String ID; // a 1
    private Integer R; // referenced
    private Integer M; // modified
    private Integer C; // clock
}