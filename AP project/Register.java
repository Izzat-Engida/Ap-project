import java.io.Serializable;
import java.util.Date;


class Register implements Serializable {
    private String firstName, lastName, email, password,address;
    private Date birthDate;

    Register(String firstName, String lastName, String email, String password, Date birthDate, String address){
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.address=address;
    }
    String getAddress(){
        return address;
}
    String getFirstName(){
        return firstName;
    }
    String getLastName(){
        return lastName;
    }
    String getEmail(){
        return email;
    }
    String getPassword(){
        return password;
    }
    Date getBirthDate(){
        return birthDate;
    }
}
