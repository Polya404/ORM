package knubisoft.model;

import knubisoft.TableAnnotation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@TableAnnotation("persons")
public class Person {

    private String name;
    private BigInteger age;
    private BigInteger salary;
    private String position;
    private LocalDate dateOfBirth;

}
