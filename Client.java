import java.net.*;
import java.io.*;
import java.lang.Math;
import java.net.Socket.*;
import java.util.Date;
import java.util.Scanner;
import java.util.Date;

public class Client {
    public static void main(String[] args) throws Exception {
        try{
        Socket socket=new Socket("127.0.0.1",8888);
        DataInputStream inStream=new DataInputStream(socket.getInputStream());
        DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String clientMessage1="",clientMessage2="",clientMessage3="", serverMessage="", serverMessage2="";
        while(!clientMessage1.equals("bye")){

            System.out.println("Enter Name :" );
            clientMessage1=br.readLine();
            outStream.writeUTF(clientMessage1);
            outStream.flush();

            System.out.println("Enter Phone Number :" );
            clientMessage2=br.readLine();
            outStream.writeUTF(clientMessage2);
            outStream.flush();

            System.out.println("Enter Date :" );
            clientMessage3=br.readLine();
            outStream.writeUTF(clientMessage3);
            outStream.flush();


            serverMessage=inStream.readUTF();
            System.out.println(serverMessage);
            
            serverMessage2=inStream.readUTF();
            System.out.println(serverMessage2);
        }
        outStream.close();
        outStream.close();
        socket.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
