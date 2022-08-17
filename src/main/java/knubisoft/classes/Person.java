package knubisoft.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Person {

    private String name;
    private BigInteger age;
    private BigInteger salary;
    private String position;
    //private LocalDate dateOfBirth;

}
