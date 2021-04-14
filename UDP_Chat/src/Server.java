import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/*
Name : Omar Khaled Mohy El-din

 */

public class Server {
    public static void main(String[] args){
        try {
            DatagramSocket serverSocket = new DatagramSocket(22000);
            System.out.println("Server is up");

            byte[] requestByte = new byte[4096];
            byte[] responseByte;

            Scanner s = new Scanner(System.in);
            while (true){
                //client packet for reciving client request
                DatagramPacket clientPK = new DatagramPacket(requestByte, requestByte.length);
                //Receive client request onto the client packet
                serverSocket.receive(clientPK);

                byte[] data = new byte[clientPK.getLength()];
                System.arraycopy(clientPK.getData(), clientPK.getOffset(), data, 0, clientPK.getLength());

                //Create string from actual data
                String msg = new String (data);
                System.out.println("Client "+clientPK.getSocketAddress()+": " + msg);

                String serverINPUT="";
                if(msg.startsWith("weather")||msg.startsWith("Weather")){
                    serverINPUT="The weather today is cold!";
                    responseByte = serverINPUT.getBytes();
                }else if(msg.startsWith("IP")||msg.startsWith("Ip")||msg.startsWith("ip")){
                    InetAddress CLIENT_IP = clientPK.getAddress();
                    serverINPUT="IP Address is: "+CLIENT_IP.toString();
                    responseByte = serverINPUT.getBytes();
                }else if(msg.startsWith("time")||msg.startsWith("Time")){
                    Date date = new Date();
                    String strDateFormat = "HH:mm:ss a";
                    SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                    serverINPUT=sdf.format(date).toString();
                    responseByte=serverINPUT.getBytes();
                }else if(msg.startsWith("close")||msg.startsWith("quit")){
                    serverSocket.close();
                    break;
                }else{
                    serverINPUT="Wrong Question!";
                    responseByte=serverINPUT.getBytes();
                }

                //get ip and port of client
                InetAddress clientIP = clientPK.getAddress();
                int clientPORT = clientPK.getPort();

                //server packet to respond to client
                DatagramPacket serverPK = new DatagramPacket(responseByte, responseByte.length
                        , clientIP, clientPORT);

                //send server response
                serverSocket.send(serverPK);
                requestByte=null;
                responseByte =null;
                requestByte = new byte[4096];
                responseByte =new byte[4096];
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
