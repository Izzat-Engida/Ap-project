import java.util.Scanner;

public class Usermenu {
    public void menu(){
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
                    // Call the method to check the items to bid on
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
