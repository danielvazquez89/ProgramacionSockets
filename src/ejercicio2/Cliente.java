package ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Daniel Vázquez
 *
 */
class ClienteTCP {
	private Socket socketCliente = null;
	private BufferedReader entrada = null;
	private PrintWriter salida = null;

	public ClienteTCP(String ip, int puerto) {
		try {
			socketCliente = new Socket(ip, puerto);
			System.out.println("Conexi�n establecida: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.err.printf("Imposible conectar con ip:%s / puerto:%d", ip, puerto);
			System.exit(-1);
		}
	}

	public void closeClienteTCP() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Cliente Terminado");
	}

	public void enviarMsg(String linea) {
		salida.println(linea);
	}

	public String recibirMsg() {
		String msg = "";
		try {
			msg = entrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
}

public class Cliente {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		String linea;
		ClienteTCP canal = new ClienteTCP("localhost", 5555);
		Tablero miTablero = new Tablero();
		System.out.println(
				"Las cordenadas se escriben con la letra mayúscula, de la A a la J, seguido del número del 1 al 10 (ejemplo: A3)");
		System.out.println("A B C D E F G H I J - 1 2 3 4 5 6 7 8 9 10");
		String mensajeEnemigo;
		boolean fin = false;
		miTablero.imprimirTablero();
		do {
			System.out.println("Me toca");
			System.out.println("Escriba las coordenadas donde quiere atacar (A-J) (1-10): ");
			linea = sc.nextLine();
			canal.enviarMsg(linea);
			mensajeEnemigo = canal.recibirMsg();
			System.out.println("Enemigo dice: " + mensajeEnemigo);
			if (mensajeEnemigo.equals("Tocado")) {
				System.out.println("Le di, victoria");
				fin = true;
			} else if (miTablero.comprobarDado(mensajeEnemigo)) {
				System.out.println("Me ha dado, derrota");
				canal.enviarMsg("Tocado");
				fin = true;
			} else {
				System.out.println("No me ha dado");
			}
		} while (!fin);
		sc.close();
		canal.closeClienteTCP();
	}
}