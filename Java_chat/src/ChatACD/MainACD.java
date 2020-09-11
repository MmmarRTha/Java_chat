/*
 * Programa que cuenta con dos clases con interfaz grafica, un ChatCliente y un ChatServidor,
 * el ChatCliente envia informacion al ChatServidor, el ChatServidor esta a la escucha de las conexiones
 * el ChatServidor recibe el texto entrante y lo lee, al mismo tiempo que puede enviar texto al ChatCliente
 * y el ChatCliente recibe la informacion y la lee tambien.
 * contamos con dos botones de historial, uno para ver el historial de la conversacion pasada y el de borrr historial.
 * mostramos en ventana un cronometro y un reloj. Cada mesaje enviado muetra la hora en el que fue recibido el mensaje.
 */
package ChatACD;

/**
 * @author Martha Nieto
 */
public class MainACD {

    public static void main(String[] args) {
        ClienteChat startC = new ClienteChat();
        ServidorChat startS = new ServidorChat();

    }

}
