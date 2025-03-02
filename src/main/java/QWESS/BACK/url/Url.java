package QWESS.BACK.url;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="QUESSURL")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR", nullable = false)
    private String url;

    @Column(name = "tf", nullable = false)
    private boolean tf;
}
