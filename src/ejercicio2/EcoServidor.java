package ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Daniel Vázquez
 *
 */
class ServidorTCP {
	private Socket socketCliente;
	private ServerSocket socketServidor;
	private BufferedReader entrada;
	private PrintWriter salida;

	public ServidorTCP(int puerto) {
		this.socketCliente = null;
		this.socketServidor = null;
		this.entrada = null;
		this.salida = null;
		try {
			socketServidor = new ServerSocket(puerto);
			System.out.println("Esperando conexi�n...");
			socketCliente = socketServidor.accept();
			System.out.println("Conexi�n acceptada: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.out.println("No puede escuchar en el puerto: " + puerto);
			System.exit(-1);
		}
	}

	public void closeServidorTCP() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
			socketServidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");
	}

	public String recibirMsg() {
		String linea = "";
		try {
			linea = entrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linea;
	}

	public void enviarMsg(String linea) {
		salida.println(linea);
	}

}

public class EcoServidor {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		ServidorTCP canal = new ServidorTCP(5555);
		String linea;
		Tablero miTablero = new Tablero();
		System.out.println(
				"Las cordenadas se escriben con la letra mayúscula, de la A a la J, seguido del número del 1 al 10 (ejemplo: A3)");
		System.out.println("A B C D E F G H I J - 1 2 3 4 5 6 7 8 9 10");
		String mensajeEnemigo = "";
		miTablero.imprimirTablero();
		boolean fin = false;
		System.out.println("Empieza el cliente");
		do {
			mensajeEnemigo = canal.recibirMsg();
			System.out.println("Enemigo dice: " + mensajeEnemigo);
			if (mensajeEnemigo.equals("Tocado")) {
				System.out.println("Dado, victoria");
				fin = true;
			} else if (miTablero.comprobarDado(mensajeEnemigo)) {
				System.out.println("Me ha dado, derrota");
				canal.enviarMsg("Tocado");
				fin = true;
			} else {
				System.out.println("No me ha dado, me toca");
				System.out.println("Escriba las coordenadas donde quiere atacar (A-J) (1-10): ");
				linea = sc.nextLine();
				canal.enviarMsg(linea);
			}
		} while (!fin);
		sc.close();
		canal.closeServidorTCP();
	}
}