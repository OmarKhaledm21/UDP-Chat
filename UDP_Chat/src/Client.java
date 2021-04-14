import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/*
Name : Omar Khaled Mohy El-din
 */

public class Client {
    public static void main(String[] args){
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            System.out.println("Client active");

            InetAddress serverIP= InetAddress.getByName("localhost");
            int serverPORT=22000;

            byte[] requestBytes;
            byte[] responseBytes = new byte[4096];

            System.out.println("Type one of the following commands \n'time': to display time.\n'weather': to display weather.\n" +
                    "'IP': to display IP address.\n'close or quit': to close connection!");
            Scanner s = new Scanner(System.in);

            while (true) {
                String input = s.nextLine();
                requestBytes = input.getBytes();
                DatagramPacket clientPACKET = new DatagramPacket(requestBytes, requestBytes.length
                        , serverIP, serverPORT);

                clientSocket.send(clientPACKET);

                if(input.toLowerCase().equals("close")||input.toLowerCase().equals("quit")){
                    System.out.println("Socket is closed");
                    clientSocket.close();
                    break;
                }

                DatagramPacket serverPK = new DatagramPacket(responseBytes, responseBytes.length);
                clientSocket.receive(serverPK);

                String response = new String(serverPK.getData());
                System.out.println("Server: " + response);

                requestBytes=null;
                responseBytes =null;
                requestBytes = new byte[4096];
                responseBytes =new byte[4096];

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
