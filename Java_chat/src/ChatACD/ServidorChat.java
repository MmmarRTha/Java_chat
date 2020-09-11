/*
 * Chat servidor
 */
package ChatACD;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martha Nieto
 */
public class ServidorChat implements Runnable {
    static ServerSocket servidor;
    static Socket socketS;
    static int mili = 0;
    static int seg = 0;
    static int min = 0;
    static int hora = 0;
    static boolean estado = true;
    private static JTextArea areaT;
    //declaracion de variables
    ThreadReloj hReloj;
    JTextField txtMsgS, txtNickS;
    JLabel etiNickS, etiChatS, etiS, etiMili, etiSeg, etiMin, etiHora;
    Thread hCro, hiloServidor, hR;
    File historialS;
    private JButton btnES, btnHS, btnBHS;

    public ServidorChat() {
        //creacion de la ventana
        JFrame fS = new JFrame();
        fS.setSize(620, 420); //Establecemos tamaño de ventana ancho y largo
        fS.setTitle("PROGRAMA CHAT EN JAVA (Servidor)"); //se establece titulo de la ventana
        fS.setLocationRelativeTo(null); //establecemos la pantalla en el centro
        fS.setMinimumSize(new Dimension(620, 420)); //establecemos el tamano minimo
        fS.getContentPane().setBackground(Color.CYAN);
        fS.setBackground(Color.cyan); //Establecemos color de la ventana
        fS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Cierra y termina el programa
        fS.setVisible(true);

        //creacion del panel
        JPanel panelS = new JPanel();
        panelS.setLayout(null);
        panelS.setBackground(Color.cyan);
        panelS.setSize(620, 480);
        panelS.setVisible(true);
        fS.add(panelS);

        //etiquetas del cronometro
        JLabel etiHora = new JLabel("00 :");
        etiHora.setBounds(210, 300, 40, 20);
        etiHora.setFont(new Font("Arial", Font.BOLD, 12));
        etiHora.setForeground(Color.white);
        etiHora.setOpaque(true);
        etiHora.setBackground(Color.black);
        panelS.add(etiHora);

        JLabel etiMin = new JLabel("00 :");
        etiMin.setBounds(250, 300, 40, 20);
        etiMin.setFont(new Font("Arial", Font.BOLD, 12));
        etiMin.setForeground(Color.white);
        etiMin.setOpaque(true);
        etiMin.setBackground(Color.black);
        panelS.add(etiMin);

        JLabel etiSeg = new JLabel("00 :");
        etiSeg.setBounds(290, 300, 40, 20);
        etiSeg.setFont(new Font("Arial", Font.BOLD, 12));
        etiSeg.setForeground(Color.white);
        etiSeg.setOpaque(true);
        etiSeg.setBackground(Color.black);
        panelS.add(etiSeg);

        JLabel etiMili = new JLabel("00 :");
        etiMili.setBounds(330, 300, 40, 20);
        etiMili.setFont(new Font("Arial", Font.BOLD, 12));
        etiMili.setForeground(Color.white);
        etiMili.setOpaque(true);
        etiMili.setBackground(Color.black);
        panelS.add(etiMili);

        etiNickS = new JLabel("NICKNAME:");
        etiNickS.setBounds(20, 10, 80, 20);
        panelS.add(etiNickS);

        //se establece el boton de historial              
        btnHS = new JButton("Historial");
        btnHS.setBounds(300, 10, 90, 25);
        panelS.add(btnHS);

        //se establece el boton de borrar historial              
        btnBHS = new JButton("Borrar historial");
        btnBHS.setBounds(400, 10, 120, 25);
        panelS.add(btnBHS);

        //creacion de etiqueta
        etiChatS = new JLabel("CONVERSACION:");
        etiChatS.setBounds(20, 60, 180, 20);
        panelS.add(etiChatS);

        txtNickS = new JTextField();
        txtNickS.setBounds(90, 10, 120, 25);
        panelS.add(txtNickS);

        //creacion de area de texto
        areaT = new JTextArea();
        areaT.setBounds(40, 80, 500, 220);
        areaT.setBorder(new LineBorder(Color.BLACK));
        panelS.add(areaT);

        //creacion de etiqueta
        etiS = new JLabel("MENSAJE:");
        etiS.setBounds(20, 320, 200, 20);
        panelS.add(etiS);

        //creacion de campo de texto
        txtMsgS = new JTextField();
        txtMsgS.setBounds(20, 340, 470, 25);
        panelS.add(txtMsgS);

        //se establece el boton de enviar             
        btnES = new JButton("ENVIAR");
        btnES.setBounds(490, 340, 80, 25);
        panelS.add(btnES);

        //Etiqueta para reloj
        JLabel etiReloj = new JLabel("Reloj");
        etiReloj.setBounds(400, 60, 68, 15);
        etiReloj.setFont(new Font("Arial", Font.BOLD, 10));
        etiReloj.setOpaque(true);
        etiReloj.setForeground(Color.white);
        etiReloj.setBackground(Color.black);
        panelS.add(etiReloj);
        //Se inicializa el hilo del rejol
        hReloj = new ThreadReloj(etiReloj);
        hR = new Thread(hReloj, "Reloj");
        hR.start();

        //se estableec el hilo para que el servidor permanezca a la escucha de llamadas del cliente
        hiloServidor = new Thread(this);
        hiloServidor.start();

        //hilo del cronometro
        estado = true;
        hCro = new Thread("Cronometro") {
            public void run() {
                for (; ; ) {
                    if (estado == true) {
                        System.out.println("Proceso de " + Thread.currentThread().getName() + " en ejecución");
                        try {
                            Thread.sleep(1);
                            if (mili > 1000) {
                                mili = 0;
                                seg++;
                            }
                            if (seg > 60) {
                                mili = 0;
                                seg = 0;
                                min++;
                            }
                            if (min > 60) {
                                mili = 0;
                                seg = 0;
                                min = 0;
                                hora++;
                            }
                            etiMili.setText(" : " + mili);
                            mili++;
                            etiSeg.setText(" : " + seg);
                            etiMin.setText(" : " + min);
                            etiHora.setText(" : " + hora);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        System.out.println("Proceso de " + Thread.currentThread().getName() + " finalizado");
                        break;
                    }
                }
            }
        };
        hCro.start();

        //evento del boton ver historial
        ActionListener escuchaLeeHistorial = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File archivoHistorial = new File("C:/Users/Martha  Nieto/Documents/NetBeansProjects/ACD_Poo3/Historial Chat Servidor.txt");
                try {
                    FileReader entrada = new FileReader(archivoHistorial);
                    BufferedReader bufferL = new BufferedReader(entrada);
                    String texto = "";
                    String linea = "";
                    while (((linea = bufferL.readLine()) != null)) {
                        texto += linea + "\n";
                    }
                    entrada.close();
                    areaT.setText(texto);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "No se encontro el archivo");
                }
            }
        };
        btnHS.addActionListener(escuchaLeeHistorial);

        //evento del boton borrar historial
        ActionListener escuchaBorraHistorial = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File archivoBorrarHistorial = new File("C:/Users/Martha  Nieto/Documents/NetBeansProjects/ACD_Poo3/Historial Chat Servidor.txt");
                if (archivoBorrarHistorial.delete())
                    JOptionPane.showMessageDialog(null, "El historial del SERVIDOR ha sido borrado satisfactoriamente");
                else JOptionPane.showMessageDialog(null, "El historial no puede ser borrado");
            }
        };
        btnBHS.addActionListener(escuchaBorraHistorial);

        //evento del boton enviar
        ActionListener escuchaEnviarS = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String enviarMsg = "";
                    enviarMsg = txtMsgS.getText().trim();
                    //stream de salida de datos mediante el socket
                    DataOutputStream flujoSalida = new DataOutputStream(socketS.getOutputStream());
                    flujoSalida.writeUTF(enviarMsg); //flujo de escritura
                    flujoSalida.flush();
                    Date horaActual = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh.mm aa");
                    areaT.append("\n Yo:\t" + enviarMsg + "      " + sdf.format(horaActual));//muestra el  mensaje enviado
                    historialS = new File("Historial Chat Servidor.txt");
                    PrintStream salida = new PrintStream(historialS);
                    salida.print(areaT.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR! No se pudo enviar el mensaje al cliente");
                    System.out.println(ex.getMessage());
                }
            }
        };
        btnES.addActionListener(escuchaEnviarS);
    }

    //implementacion del hilo del serversocket
    @Override
    public void run() {
        String mensaje = "";
        try { //creacion del Serversocket para poder recibir llamadas de clientes
            servidor = new ServerSocket(9999); //el servidor esta listo para escuchar en el puerto 9999
            socketS = servidor.accept(); //el servidor acepta las conexiones entrantes
            DataInputStream flujoEntrada = new DataInputStream(socketS.getInputStream());
            while (true) {
                mensaje = flujoEntrada.readUTF(); //lectura de flujo entante
                Date horaActual = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh.mm aa");
                areaT.setText(areaT.getText().trim() + "\n Cliente:\t" + mensaje + "      " + sdf.format(horaActual)); //muestra mensaje del cliente
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "ERROR! No se pudo estableces la conexion con el cliente");
            System.out.println(ex.getMessage());
        }
    }
}