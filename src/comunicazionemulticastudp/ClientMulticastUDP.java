package comunicazionemulticastudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Monica Ciuchetti
 */
public class ClientMulticastUDP {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
                //numero di porta server
                int port = 2000;
                //numero di porta del gruppo
                int portGroup = 1900;
                //indirizzo del server
                InetAddress serverAddress;
                //socket UDP
                DatagramSocket dSocket = null;
                //socket multicast UDP
                MulticastSocket mSocket = null;
                //indirizzo gruppo multicast UDP
                InetAddress group;
                
                //Datagramma UDP con la richiesta da inviare al server
                DatagramPacket outPacket;
                //Datagramma UDP di risposta ricevuto dal server
                DatagramPacket inPacket;
                  
                //buffer di lettura
                byte[] inBuffer= new byte[256];
                                        
                //messaggio di richiesta
                String messageOut = "Richiesta comunicazione";
                //messaggio di risposta
                String messageIn;
                
                try {    
                    System.out.println("CLIENT UDP");
                    //1) RICHIESTA AL SERVER
                    //si recupera l'IP del server UDP
                    serverAddress = InetAddress.getLocalHost(); 
                    System.out.println("Indirizzo del server trovato!");
                    
                    //istanza del socket UDP per la prima comunicazione con il server
                    dSocket = new DatagramSocket();
                    
                    //si prepara il datagramma con i dati da inviare
                    outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), serverAddress, port);
                    
                    //si inviano i dati
                    dSocket.send(outPacket);
                    System.out.println("Richiesta al server inviata!");
                   
                    //2) RISPOSTA DEL SERVER
                    //si prepara il datagramma per ricevere dati dal server
                    inPacket = new DatagramPacket(inBuffer,inBuffer.length);
                    dSocket.receive(inPacket); 
                    
                    //lettura del messaggio ricevuto e sua visualizzazione
                    messageOut = new String(inPacket.getData(),0,inPacket.getLength());
                    System.out.println("Lettura dei dati ricevuti dal server");
                    
                    messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
                    System.out.println("Messaggio ricevuto dal server " + serverAddress +
                        ":" + port + "> " + messageIn);
                    
                    //3) RICEZIONE MESSAGGIO DEL GRUPPO
                    //istanza del Multicast socket e unione al gruppo
                    mSocket = new MulticastSocket(1900);
                    group = InetAddress.getByName("239.255.255.250");
                    mSocket.joinGroup(group);
                    
                    //si prepara il datagramma per ricevere dati dal gruppo
                    inPacket = new DatagramPacket(inBuffer,inBuffer.length);
                    mSocket.receive(inPacket); 
                    
                    //lettura del messaggio ricevuto e sua visualizzazione
                    messageIn = new String(inPacket.getData(),0, inPacket.getLength());
                    System.out.println("Lettura dei dati ricevuti dai partecipanti al gruppo");
                    System.out.println("Messaggio ricevuto dal gruppo " + group +
                        ":" + portGroup + "> " + messageIn);
                    
                    //uscita dal gruppo
                    mSocket.leaveGroup(group);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClientMulticastUDP.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Errore di risoluzione");
                } catch (SocketException ex) {
                    Logger.getLogger(ClientMulticastUDP.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Errore di creazione socket");
                } catch (IOException ex) {
                    Logger.getLogger(ClientMulticastUDP.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Errore di I/O");
                }
                finally{
                    if (dSocket != null)
                        dSocket.close();
                    if (mSocket != null)
                        mSocket.close();
                }
        }
    }