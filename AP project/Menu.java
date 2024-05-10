import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;


public class Menu {

    private  Menu(){

    }
    public static void main(String[] args) throws NotBoundException, RemoteException, MalformedURLException {
        Scanner scan=new Scanner(System.in);
        int choice;
        while(true){
            System.out.println("1. Login in to the system");
            System.out.println("2. If new Register");
            System.out.println("3. Exit");
            System.out.print("choice: ");
            if(!scan.hasNextInt()){
                System.out.println("Invalid input. Please enter a number.");
                scan.next();
                continue;
            }
            choice=scan.nextInt();
            scan.nextLine();
            if(choice==3){
                break;
            }
            else if(choice==1){
                System.out.println("Enter your username: ");
                String username = scan.nextLine();
                System.out.println("Enter your password: ");
                String password = scan.nextLine();
                AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");


                boolean success=access.login(username,password);
                int userId = access.userId(username);
                if(success && userId != -1){
                    // We can make the login method of AuctionServerImp to return empty str or the user Id of the user if found
                    // but here instead there is another method for retrieving the userId with specified userName
                    System.out.println("login in success");
                    Usermenu menu=new Usermenu();
                    menu.menu(userId);
                }
                else{
                    System.out.println("login failed");
                }
            }
            else if(choice==2){
                System.out.println("Enter your first name: ");
                String firstName = scan.nextLine();
                System.out.println("Enter your last name: ");
                String lastName = scan.nextLine();
                System.out.println("Enter your email: ");
                String email = scan.nextLine();
                System.out.println("Enter your password: ");
                String password = scan.nextLine();
                System.out.println("Enter birth date: ");
                int date = scan.nextInt();
                System.out.println("Enter birth month: ");
                int month = scan.nextInt();
                System.out.println("Enter birth year: ");
                int year = scan.nextInt();
                scan.nextLine(); // consume newline left-over
                System.out.println("Enter your address");
                String address=scan.nextLine();
                Calendar c=Calendar.getInstance();
                c.set(year,month-1,date);
                Date birthDate=c.getTime();
                Register register = new Register(firstName, lastName, email, password, birthDate,address);
                AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
                boolean success= access.register(register);
                if(success){
                    System.out.println("Registered");
                }
                else{
                    System.out.println("Registration failed");
                }
            }
            else{
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }
}

class Register implements Serializable {
    private String firstName, lastName, email, password,address;
    private Date birthDate;

    Register(String firstName, String lastName, String email, String password, Date birthDate,String address){
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
