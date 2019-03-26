/**
 * Created by JJ. Liu on 2017-12-10.
 */
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Scanner;

public class Server {
    private List<Socket> clients;
    private ServerSocket server;
    private int listenPort;

    public Server(int listenPort) throws IOException {
        clients = new ArrayList<Socket>();
        this.listenPort = listenPort;
        server = new ServerSocket(listenPort);
    }

    public void serverRun() {
        System.out.println("Waiting to accept connections from clients...");
        try {
            while (true) {
                Socket curClient = server.accept();
                if (curClient != null)
                    System.out.println("Have gotten the connection from port:" + listenPort);
                clients.add(curClient);
                new ClientThread(curClient).start();
            }
        } catch (BindException e) {
            System.out.println("Unable to bind to port: " + listenPort);
        } catch (IOException e) {
            System.out.println("Unable to instantiate a ServerSocket on port: " + listenPort);
        }
    }

    protected class ClientThread extends Thread {

        private Socket client;
        private BufferedReader serverReader;
        private PrintWriter serverWriter;
        public boolean connected;

        public ClientThread(Socket client) {
            this.client = client;
            try {
                serverReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                serverWriter = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Unable to build server reader and writer");
            }
        }

        public String getMessage() {
            try {
                String message = null;
                String curReading;
                if ((curReading = serverReader.readLine()) != null) {
                    message = curReading;
                }
                return message;
            } catch (IOException e) {
                System.out.println("Reading content error!!");
                return null;
            }
        }

        public void disconnect() {
            try {
                if (connected == false) {
                    System.out.println("There isn't any connections.");
                    return;
                }
                serverReader.close();
                serverWriter.close();
                client.close();
                connected = false;
                clients.remove(client);
            } catch (IOException e) {
                System.out.println("Disconnecting failed!!");
            }
        }

        @Override
        public void run() {
            String curMessage, resultMessage;
            Iterator<Socket> i = null;
            try {
                while (true) {
                    curMessage = getMessage();
                    if (curMessage.equals("disconnect"))
                        break;
                    for(i = clients.iterator(); i.hasNext(); ) {
                        Socket curClient = (Socket) i.next();
                        PrintWriter tmpClientWriter = new PrintWriter(curClient.getOutputStream(), true);
                        Date now = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        resultMessage = sdf.format(now) + " " + curClient.getInetAddress() + ":" + " says: " + curMessage;
                        tmpClientWriter.println(resultMessage);
                    }
                    System.out.println("Recieved the message: " + curMessage);
                }
            } catch (IOException e) {
                return;
            } catch (NullPointerException e) {
                i.remove();
            }

        }
    }

    public static void main(String[] args) throws IOException {
//		System.out.print("Enter the listen port: ");
//		Scanner portGetter = new Scanner(System.in);
//		int listenPort = Integer.parseInt(portGetter.nextLine());
        Server server = new Server(8888);
        server.serverRun();
    }
}

