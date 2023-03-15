package comunicazionemulticastudp;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Monica Ciuchetti
 */
public class ServerMulticastUDP {

     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //porta del Server
        int port=2000;
        //oggetto Socket UDP 
        DatagramSocket dSocket = null;
 
        //datagramma UDP ricevuto dal client
        DatagramPacket inPacket;
        //datagramma UDP di risposta da inviare
        DatagramPacket outPacket;
        //Buffer per il contenuto del segmento da ricevere
        byte[] inBuffer;
        
        //Indirizzo del gruppo Multicast UDP
        InetAddress groupAddress;
        //messaggio ricevuto
        String messageIn;
        //messaggio da inviare
        String messageOut;
                       
        try {
            System.out.println("SERVER UDP");
            //1) SERVER IN ASCOLTO 
            //si crea il socket e si associa alla porta specifica
            dSocket = new DatagramSocket(port);
            System.out.println("Apertura porta in corso!");
            
            //si prepara il buffer per il messaggio da ricevere
            inBuffer = new byte[256];
            
            //2) RICEZIONE MESSAGGIO DEL CLIENT
            //si crea un datagramma UDP in cui trasportare il buffer per tutta la sua lunghezza
            inPacket = new DatagramPacket(inBuffer, inBuffer.length);
            //si attende il pacchetto dal client
            dSocket.receive(inPacket);
            
            //si recupera l'indirizzo IP e la porta UDP del client
            InetAddress clientAddress = inPacket.getAddress();
            int clientPort = inPacket.getPort();
            
            //si stampa a video il messaggio ricevuto dal client
            messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println("Messaggio ricevuto dal client " + clientAddress +
                    ":" + clientPort + "> " + messageIn);
            
            //3)RISPOSTA AL CLIENT
            //si prepara il datagramma con i dati da inviare
            messageOut = "Ricevuta richiesta!";
            outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort);
            
            //si inviano i dati
            dSocket.send(outPacket);
            System.out.println("Spedito messaggio al client.");
            
            //4)INVIO MESSAGGIO AL GRUPPO
            //si recupera l'IP gruppo
            groupAddress = InetAddress.getByName("239.255.255.250");
            //si inizializza la porta del gruppo
            int groupPort = 1900;
            
            //si prepara il datagramma con i dati da inviare al gruppo
            messageOut= "Benvenuti a tutti!";
            
            outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), groupAddress, 1900);
            
            //si inviano i dati
            dSocket.send(outPacket);
            System.out.println("Spedito messaggio al gruppo.");

        } catch (BindException ex) {
            Logger.getLogger(ServerMulticastUDP.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Portagià in uso");
        } catch (SocketException ex) {
            Logger.getLogger(ServerMulticastUDP.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Errore di creazione del socket e apertura del server");
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerMulticastUDP.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Errore di risoluzione");
        } catch (IOException ex) {
            Logger.getLogger(ServerMulticastUDP.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Errore di I/O");
        }
        finally{
           if (dSocket != null)
                dSocket.close();
          }
    }
    
}
