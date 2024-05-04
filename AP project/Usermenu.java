import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Usermenu {
    public void menu() throws MalformedURLException, NotBoundException, RemoteException {
        Scanner scan=new Scanner(System.in);
        int choice;
        while (true){
            System.out.println("1. Check the items to bid on");
            System.out.println("2. List an item to be bid on");
            System.out.println("3. check the items you bid on");
            System.out.println("4. see your history");
            System.out.println("5. manage your money");
            System.out.println("6. delete your account ");
            System.out.println("7. logout");
            System.out.print("choice: ");
            if(!scan.hasNextInt()){
                System.out.println("Invalid input. Please enter a number.");
                scan.next(); // discard the non-integer input
                continue;
            }
            choice=scan.nextInt();
            scan.nextLine(); // consume newline left-over
            switch(choice){
                case 1:
                    Item item=new Item();
                    item.itemMenu();
                    break;
                case 2:
                    // Call the method to list an item to be bid on
                    break;
                case 3:
                    // Call the method to check the items the user bid on
                    break;
                case 4:
                    // Call the method to see the user's history
                    break;
                case 5:
                    break;
                case 6:
                    System.out.println("Enter your Email: ");
                    String email=scan.nextLine();
                    System.out.println("Enter your password: ");
                    String password=scan.nextLine();
                    AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
                    if(access.login(email,password)){
                  access.removeAccount(email,password);
                        System.out.println("sorry to see you leave");
                  break;
                    }
                    else{
                        System.out.println("sorry, you can't delete your account\n you have inputted wrong information");
                    }

                    break;
                case 7:
                    System.out.println("Logging out...");
                    return; // exit the method
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }
}
