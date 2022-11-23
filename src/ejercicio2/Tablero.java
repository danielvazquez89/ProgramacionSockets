package ejercicio2;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * @author Daniel Vázquez
 *
 */
public class Tablero {
	private int[][] tablero;
	HashMap<Character, Integer> letras;

	private int numeroBarquitos = 10;

	public Tablero() {
		tablero = new int[10][10];
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				tablero[i][j] = 0;
			}
		}
		for (int i = 0, numero, letra; i < numeroBarquitos; i++) {
			do {
				numero = (ThreadLocalRandom.current().nextInt(0, 9 + 1));
				letra = (ThreadLocalRandom.current().nextInt(0, 9 + 1));
			} while (tablero[letra][numero] == 1);
			tablero[letra][numero] = 1;
		}
		letras = new HashMap<>();
		for (int i = 0, letraContador = 'A'; i < numeroBarquitos; i++, letraContador++) {
			letras.put((char) letraContador, i);
		}
	}

	public void imprimirTablero() {
		System.out.println("---------------------------");
		System.out.println("Mi tablero: \n");
		for (int i = 1; i <= this.tablero.length; i++) {
			System.out.print("   " + i);
		}
		System.out.println();
		for (int i = 0, letraContador = 'A'; i < numeroBarquitos; i++, letraContador++) {
			System.out.print((char) letraContador + "  ");
			for (int j = 0; j < numeroBarquitos; j++) {
				System.out.print(tablero[i][j] + "   ");
			}
			System.out.println();
		}
		System.out.println("---------------------------");
	}

	public boolean comprobarDado(String mensajeEnemigo) {
		boolean dado = false;
		if (validarMensaje(mensajeEnemigo)) {
			if (mensajeEnemigo.length() == 2)
				dado = tablero[letras.get(mensajeEnemigo.charAt(0))][mensajeEnemigo.charAt(1) - '0' - 1] == 1;
			else {
				dado = tablero[letras.get(mensajeEnemigo.charAt(0))][10 - 1] == 1;
			}
		} else
			dado = false;
		return dado;
	}

	private boolean validarMensaje(String mensaje) {
		boolean validaLetra = false;
		boolean validoNumero = false;
		if (mensaje.length() == 2) {
			validaLetra = (Character.isLetter(mensaje.charAt(0)) && Character.isUpperCase(mensaje.charAt(0)))
					&& (mensaje.charAt(0) >= 'A' && mensaje.charAt(0) <= 'J');
			validoNumero = Character.isDigit(mensaje.charAt(1)) && mensaje.charAt(1) > '0';
		} else if (mensaje.length() == 3) {
			validaLetra = (Character.isLetter(mensaje.charAt(0)) && Character.isUpperCase(mensaje.charAt(0)))
					&& (mensaje.charAt(0) >= 'A' && mensaje.charAt(0) <= 'J');
			validoNumero = (Character.isDigit(mensaje.charAt(1)) && Character.isDigit(mensaje.charAt(2)))
					&& mensaje.charAt(2) == '0';
		}
		return validaLetra && validoNumero;
	}

	public int[][] getTablero() {
		return tablero;
	}

	public HashMap<Character, Integer> getLetras() {
		return letras;
	}
}
