package knubisoft.model;

import knubisoft.TableAnnotation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Setter
@TableAnnotation("person2")
public class Person2 {

    private String name;
    private BigInteger age;

}
