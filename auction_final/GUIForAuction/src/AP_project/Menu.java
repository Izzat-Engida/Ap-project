package AP_project;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


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
                System.exit(0);
            }
            else if(choice==1){
                System.out.println("Enter your Email: ");
                String email = scan.nextLine();
                System.out.println("Enter your password: ");
                String password = scan.nextLine();
                AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");

                boolean success=access.login(email,password);
                int userId = access.userId(email);

                if(success && userId != -1){
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

