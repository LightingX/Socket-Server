/**
 * Created by JJ. Liu on 2017-12-10.
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {


    protected Socket client;
    private String hostIP;
    private int hostPort;
    private BufferedReader clientReader;
    private PrintWriter clientWriter;
    public boolean connected;

    public Client(String hostIP, int hostPort) throws IOException {
        this.hostIP = hostIP;
        this.hostPort = hostPort;

        connected = false;
    }

    public void connect() throws Throwable {
        try {
            client = new Socket(hostIP, hostPort);
            System.out.println("Conncet successfully!");
            clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            clientWriter = new PrintWriter(client.getOutputStream(), true);
        } catch (UnknownHostException e) {
            System.out.println("Error setting up socket connection: unknown host at " + hostIP + ":" + hostPort);
            throw e;
        } catch (IOException e) {
            System.out.println("Error setting up socket connection: " + e);
            throw e;
        }
        connected = true;
    }

    public void sendMessage(String messageContent) throws IOException {
        clientWriter.println(messageContent);
        clientWriter.flush();
    }

    public String getMessage() {
        try {
            String message = "";
            String curReading;
            if ((curReading = clientReader.readLine()) != null) {
                message += curReading;
            }
            return message;
        } catch (IOException e) {
            connected = false;
            return null;
        }
    }

    public void disconnect() {
        try {
            if (connected == false) {
                System.out.println("There isn't any connections.");
                return;
            }
            clientReader.close();
            clientWriter.close();
            client.close();
            connected = false;
        } catch (IOException e) {
            System.out.println("Disconnecting failed!!");
        }
    }



    public static void main(String[] args) throws IOException {
        try {
            Scanner in = new Scanner(System.in);
//			System.out.print("Enter the IP of host: ");
//			String hostIP = in.nextLine();
//			System.out.print("Enter the listen port of host: ");
//			int listenPort = Integer.parseInt(in.nextLine());
            Client client = new Client("localhost", 8888);
            client.connect();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(client.connected) {
                        System.out.println(client.getMessage());
                    }
                    System.out.println("Server closed.");
                }
            }).start();
            String curLine;
            do {
                System.out.print("Enter your message: \n");
                curLine = in.nextLine();
                client.sendMessage(curLine);
            } while (!curLine.equals("disconnect"));
            client.disconnect();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
