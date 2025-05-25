package travel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Agencies")
public class Agency extends Entity<Long> implements Serializable {
    private String agencyName;
    private String email;
    private String password;

    public Agency(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Agency(String agencyName, String email, String password) {
        this.agencyName = agencyName;
        this.email = email;
        setPassword(password);
    }

    public Agency() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Agency agency = (Agency) o;
        return Objects.equals(agencyName, agency.agencyName) && Objects.equals(email, agency.email) && Objects.equals(password, agency.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), agencyName, email, password);
    }

    @Column(name = "agency_name")
    public String getAgencyName() {
        return agencyName;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        if (!password.startsWith("$2a$") && !password.startsWith("$2b$") && !password.startsWith("$2y$")) {
            this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        } else {
            this.password = password;
        }
    }
}
