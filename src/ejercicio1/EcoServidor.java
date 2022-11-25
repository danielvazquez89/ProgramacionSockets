package ejercicio1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * 
 * @author Daniel Vázquez
 *
 */
class ServidorUDP {
	private DatagramSocket socketUDP;
	private DatagramPacket recibido;

	public ServidorUDP(int puerto) {
		try {
			this.socketUDP = new DatagramSocket(puerto);
			this.recibido = null;
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public String recibirMsg() {
		try {
			byte[] buffer = new byte[1000];
			recibido = new DatagramPacket(buffer, buffer.length);
			socketUDP.receive(recibido);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(recibido.getData()).trim();
	}

	public void enviarMsg(String msg) {

		try {
			DatagramPacket respuesta = new DatagramPacket(msg.getBytes(), msg.length(), recibido.getAddress(),
					recibido.getPort());
			socketUDP.send(respuesta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeServidorUDP() {
		socketUDP.close();
		System.out.println("-> Servidor Terminado");
	}

}

// Espera recibir el mensaje de la otra aplicación (EcoCliente)
public class EcoServidor {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String nombre = "Pedro";
		int puerto = 5555;
		System.out.println("Servidor escucha por el puerto " + puerto);
		ServidorUDP canal = new ServidorUDP(puerto);
		String linea;
		do {
			linea = canal.recibirMsg();
			System.out.println(linea);
			if (linea.split(":")[1].trim().equals("Fin")) {
				linea = "Fin";
			} else {
				do {
					System.out.print("Yo: ");
					linea = sc.nextLine();
				} while (linea.trim().equals(""));
				canal.enviarMsg(nombre + ": " + linea);
			}
		} while (!linea.trim().equals("Fin"));
		sc.close();
		canal.closeServidorUDP();
	}
}
