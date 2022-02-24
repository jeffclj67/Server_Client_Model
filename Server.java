import java.net.*;
import java.io.*;
import java.lang.Math;
import java.net.Socket.*;
import java.util.Date;
import java.util.Scanner;
import java.util.Date;

public class Server {

    public static void main(String[] args) throws Exception {
        try{
            ServerSocket server=new ServerSocket(8888);
            int counter=0;
            System.out.println("Server Started ....\n");
            while(true){
                counter++;
                Socket serverClient=server.accept();  //server accept the client connection request
                System.out.println(">" + "Machine:" + counter + " online!");
                ServerClientThread sct = new ServerClientThread(serverClient,counter); //send  the request to a separate thread
                sct.start();
                System.out.println("IP address: "+ serverClient.getLocalAddress());
                System.out.println("Port number: "+ serverClient.getPort()+"\n");
            
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
class ServerClientThread extends Thread {
        
    Socket serverClient;
    int clientNo;
    int squre;
    double result;
    ServerClientThread(Socket inSocket,int counter){
        serverClient = inSocket;
        clientNo=counter;
    }

    public void run(){
        try{
            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            String serverMessage="",serverMessage2="",clientMessage1="",clientMessage2="",clientMessage3="";
            
            //to create a file in the server
            try {
                //create a new log file if it's not existing
                File myObj = new File("guest_log.txt");
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                    FileWriter wr = new FileWriter("guest_log.txt", true);

                    //create headers for each columns, properly spaced out
                    wr.write(String.format("%-14s", "Name") 
                    + String.format("%-14s", "Number") 
                    + String.format("%-14s", "Date")
                    + String.format("%-14s", "Machine")
                    + String.format("%-14s", "Entry_time")
                    + "\n");
                    wr.close();
                } else {
                    System.out.println("File already exists.\n");
                }
              }catch (IOException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        
            //enable the multi-thread connection
            while(!clientMessage1.equals("bye")){
                //accept name, number and date value from client
                 clientMessage1=inStream.readUTF();
                 clientMessage2=inStream.readUTF();
                 clientMessage3=inStream.readUTF(); 
                if(!clientMessage1.equals("read")){

                    //normal entry mode
                    //when user doesn't use the "read" keyword
                    System.out.println("New Entry from Machine-" +clientNo + "\n");
                    System.out.println("Name: " + clientMessage1);
                    System.out.println("Number: " + clientMessage2);
                    System.out.println("Date: " + clientMessage3);

                    serverMessage="\n"+"Reservation confirmed";
                    outStream.writeUTF(serverMessage);
                    outStream.flush();
                    
                    serverMessage2 = new Date().toString();
                    outStream.writeUTF("system time: " + serverMessage2 + "\n");
                    outStream.flush();

                    //pad the strings with proper whitespace
                    String pad1 = String.format("%-14s", clientMessage1);
                    String pad2 = String.format("%-14s", clientMessage2);
                    String pad3 = String.format("%-14s", clientMessage3);
                    String pad4 = String.format("%-14s", "Machine- "+clientNo);
                    

                    try {
                        FileWriter myWriter = new FileWriter("guest_log.txt", true);
                        //write the guest information to the log file
                        myWriter.write(pad1 + pad2 + pad3 + pad4 + serverMessage2 + "\n");
                        myWriter.close();
                        System.out.println("Successfully Entered.\n");
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                }else{
                    //when user wants to read the log file from client
                    //server reads the file first, 
                    //then send the text string to the client
                    System.out.println("Machine " + clientNo + "has accessed the log: \n");
                    File myObj = new File("guest_log.txt");
                    Scanner myReader = new Scanner(myObj);
                    String all = "";
                    while(myReader.hasNextLine()){
                        String data = myReader.nextLine();
                        all = all.concat(data);
                        all = all.concat("\n");
                    }
                    System.out.println(all);
                    outStream.writeUTF(all);
                    outStream.flush();
                }
            }
            inStream.close();
            outStream.close();
            serverClient.close();
        }catch(Exception ex){
            System.out.println(ex);
        }finally{
            System.out.println("Client -" + clientNo + " exit!! \n");
        }
    }
}