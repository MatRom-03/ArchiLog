package subscribers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class Abonne {
    private final int numero;
    private final String name;
    private final Date dateOfBirth;
    private final int age;

    /**
     * Generate a new Subscriber
     * @param numero the subscriber number
     * @param name the subscriber name
     * @param dateOfBirth the subscriber date of birth
     */
    public Abonne(int numero, String name, Date dateOfBirth) {
        this.numero = numero;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.age = calculAge(this);
    }

    /**
     * Return the subscriber number
     * @return numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Return the subscriber age
     * @return age
     */
    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Abonne{" +
                "numero=" + numero +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * Calculate the subscriber age
     * @param ab the subscriber
     * @return age
     */
    private static int calculAge(Abonne ab) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = ab.dateOfBirth.toLocalDate();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
}
