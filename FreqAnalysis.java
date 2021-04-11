import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FreqAnalysis {
    //String array to store the addresses of multiple file locations
    private static String[] fileLocation;
    //Array to store frequency of all 26 letters of all files
    private static int[] totalCharFrequency = new int[26];
    private static int totalCharacter = 0;

    public FreqAnalysis(int numOfFiles){
        fileLocation = new String[numOfFiles];
    }

    //Returns the total character frequency of all files
    public int[] returnTotalFrequency(){
        return totalCharFrequency;
    }

    //Clears total frequency count
    public void clearTotalFrequency(){
        totalCharFrequency = null;
        totalCharFrequency = new int[26];
    }

    //Returns the location of a file
    public String getLocation(int location){
        return fileLocation[location];
    }

    //Combines file frequencies
    public void addFrequency(int[] frequency){
        for(int i = 0; i < 26; i++){
            totalCharFrequency[i]+=frequency[i];
        }
    }

    //Adds total characters from all files
    public void setTotalCharacters(int sum, boolean clear){
        if(!clear)
            totalCharacter += sum;
        else
            totalCharacter = 0;
    }

    //Returns total characters from all files
    public int getTotalCharacter(){
        return totalCharacter;
    }

    //Prints out the frequency of letters in the files
    public String printFrequency(int[] frequency, int totalCharacter){
        String str = "|";

        for(int i = 0; i < 26; i++){
            //Creates the string that holds all the values of the characters
            str = str + ((char) (i + 65)) + ": " + frequency[i] + ", " + (frequency[i] * 100 / totalCharacter) + "%|";
        }

        return str;
    }

    //Stores the location of the files
    public void loadAddress(Scanner scan, int range){
        //Stores the next line into file location array
        String address = scan.next();
        address = address + scan.nextLine();
        fileLocation[range] = address;
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        boolean quit = false;

        //Main loop that controls user interaction
        while(!quit){
            //Stores number of files to be search through , range used to store which file was entered incorrectly
            int numOfFiles, range = 0;
            FreqAnalysis file;

            try{
                //Asks the user the amount of files that will be searched through, used to determine size of our file array
                System.out.print("How many files would you like to scan?: ");
                numOfFiles = scan.nextInt();

                file = new FreqAnalysis(numOfFiles);

                //Stores the address entered by the user into the array
                for(int i = 0; i < numOfFiles; i++){
                    System.out.print("File Address " + (i + 1) + ": ");
                    file.loadAddress(scan, i);
                }
                System.out.println();

                //Control for character counting loop
                boolean finish = false;

                while(!finish){
                    try{
                        //Goes through the array and gets each file to calculate character frequency
                        for(range = 0; range < numOfFiles; range++) {
                            String location = file.getLocation(range), str;
                            //Read object points to file location
                            BufferedReader read = new BufferedReader(new FileReader(location));
                            //Storage of character frequencies
                            int[] frequency = new int[26];
                            int totalCharacters = 0;

                            while((str = read.readLine()) != null){
                                //Making all characters upper case so we can use ascii dec value - 65 to set location on array
                                str = str.toUpperCase();
                                //Goes through the entire string char by char
                                for(int i = 0; i < str.length(); i++){
                                    int dec = str.charAt(i);
                                    //Checks if ascii of character is A-Z and stores if it is
                                    if(dec > 64 && dec < 91){
                                        frequency[((int) str.charAt(i)) - 65]++;
                                        totalCharacters++;
                                    }
                                }
                            }

                            //Combines the frequencies/character count from all files
                            file.addFrequency(frequency);
                            file.setTotalCharacters(totalCharacters, false);

                            //Prints the file's character frequency
                            System.out.println("File " + (range + 1) + ": Format: |[A-Z]: Number of Characters, Percentage|");
                            System.out.println(file.printFrequency(frequency, totalCharacters));
                            System.out.println("*------");
                        }

                        //Prints total character frequency of all files
                        System.out.println("All files' character frequency: Format: |[A-Z]: Number of Characters, Percentage|");
                        System.out.println(file.printFrequency(file.returnTotalFrequency(), file.getTotalCharacter()));

                        //Ends the loop
                        finish = true;
                    }catch(Exception e){
                        //Clears our total frequency array and asks the user to renter the incorrect path address
                        file.clearTotalFrequency();
                        file.setTotalCharacters(0, true);
                        System.out.println("Invalid file " + (range + 1) + ", try again.");
                        System.out.print("File Address: ");
                        file.loadAddress(scan, range);
                        System.out.println();
                    }
                }

                //Checks if the user would like to exit
                System.out.print("\nWould you like to exit? Y/N: ");
                if(scan.nextLine().equalsIgnoreCase("Y")){
                    quit = true;
                }
            }catch(InputMismatchException e){
                System.out.println("Invaid input. Please try again. ");
                scan.nextLine();
            }
        }
        scan.close();
    }
}
