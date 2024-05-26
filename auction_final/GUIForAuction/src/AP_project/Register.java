package AP_project;

import java.io.Serializable;
import java.util.Date;


public class Register implements Serializable {
    private String firstName, lastName, email, password,address;
    private Date birthDate;
    private int userId;

    public int getUserId() {  return userId; }
    public void setUserId(int userId) {   this.userId = userId;}

    public Register(String firstName, String lastName, String email, String password, Date birthDate, String address){
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.address=address;

    }

    public String getFirstName() { return firstName; }
    public String getLastName() {  return lastName; }
    public String getEmail() {  return email; }
    public String getPassword() {  return password; }
    public String getAddress() { return address; }
    public Date getBirthDate() {  return birthDate; }
}
