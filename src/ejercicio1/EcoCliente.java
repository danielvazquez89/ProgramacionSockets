package ejercicio1;

import java.net.*;
import java.util.Scanner;
import java.io.*;

/**
 * 
 * @author Daniel Vázquez
 *
 */
class ClienteUDP {
	private DatagramSocket socketUDP;

	public ClienteUDP() {
		try {
			socketUDP = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void enviarMsg(String msg, String ip, int puerto) {
		InetAddress hostServidor;
		try {
			hostServidor = InetAddress.getByName(ip);
			byte[] mensaje = msg.getBytes();
			DatagramPacket peticion = new DatagramPacket(mensaje, mensaje.length, hostServidor, puerto);
			socketUDP.send(peticion);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String recibirMsg() {
		DatagramPacket respuesta = null;
		try {
			byte[] bufer = new byte[1000];
			respuesta = new DatagramPacket(bufer, bufer.length);
			socketUDP.receive(respuesta);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(respuesta.getData()).trim();
	}

	public void closeClienteUDP() {
		socketUDP.close();
		System.out.println("-> Cliente Terminado");
	}
}

// Primero en ejecutarse
public class EcoCliente {
	public static void main(String args[]) {
		String nombre = "Juan";
		Scanner sc = new Scanner(System.in);
		String linea;
		ClienteUDP canal = new ClienteUDP();
		System.out.println("Comience a escribir ('Fin') para terminar");
		do {
			do {
				System.out.print("Yo: ");
				linea = sc.nextLine();
			} while (linea.trim().equals(""));
			canal.enviarMsg(nombre + ": " + linea, "localhost", 5555);
			System.out.println(linea.trim());
			if (!linea.trim().equals("Fin")) {
				linea = canal.recibirMsg();
				System.out.println(linea);
				linea = linea.trim().split(":")[1];
			}
		} while (!linea.trim().equals("Fin"));
		sc.close();
		canal.closeClienteUDP();

	}
}
