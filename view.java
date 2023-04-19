package UserLoginAndRegister;
import java.util.Scanner;
public class view {
	public static void main(String args[]) {
		Scanner scanner= new Scanner(System.in);
		System.out.println("Welcome!!!\n1.Login\n2.Register\n3.Forgot Password\n4.Exit\nEnter: ");
		int choice = scanner.nextInt();
		if (choice==1) loginAuth.main(null);
		else if (choice==2) registerAuth.main(null);
		else if (choice==3) forgotPassword.main(null);
		else System.out.println("Invalid Choice!!!");
		scanner.close();
	}
}

//Mysql table details
//Database: loginauth
//table: unpw
//Attributes : username and password (both binary not null),secq1,secq2,secq3.
