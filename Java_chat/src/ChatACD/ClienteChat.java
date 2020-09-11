/*
 * Chat cliente
 */
package ChatACD;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martha Nieto
 */
public class ClienteChat implements Runnable {
    static Socket puenteC;
    static int mili = 0;
    static int seg = 0;
    static int min = 0;
    static int hora = 0;
    static boolean estado = true;
    ThreadReloj Relojito;
    DataInputStream flujoIn;
    DataOutputStream flujoOut;
    JTextField txtMsg, txtNickC;
    JLabel etiNickC, etiChat, etiC, etiMili, etiSeg, etiMin, etiHora;
    JTextArea areaChat;
    Thread hCro, hiloCliente, hR;
    Date horaActual;
    SimpleDateFormat sdf;
    File historialC;
    private JButton btnE, btnH, btnBH;

    public ClienteChat() {
        JFrame fC = new JFrame();
        fC.setSize(620, 420); //Establecemos tamaño de ventana ancho y largo
        fC.setTitle("PROGRAMA CHAT EN JAVA (Cliente)"); //se establece titulo de la ventana
        fC.setLocationRelativeTo(null); //establecemos la pantalla en el centro
        fC.setMinimumSize(new Dimension(620, 420)); //establecemos el tamano minimo
        fC.getContentPane().setBackground(Color.CYAN);
        fC.setBackground(Color.cyan); //Establecemos color de la ventana
        fC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Cierra y termina el programa
        fC.setVisible(true);

        JPanel panelC = new JPanel();
        panelC.setLayout(null);
        panelC.setBackground(Color.cyan);
        panelC.setSize(620, 480);
        panelC.setVisible(true);
        fC.add(panelC);

        //etiquetas del cronometro
        etiHora = new JLabel("00 :");
        etiHora.setBounds(210, 300, 40, 20);
        etiHora.setFont(new Font("Arial", Font.BOLD, 12));
        etiHora.setForeground(Color.white);
        etiHora.setOpaque(true);
        etiHora.setBackground(Color.black);
        panelC.add(etiHora);

        etiMin = new JLabel("00 :");
        etiMin.setBounds(250, 300, 40, 20);
        etiMin.setFont(new Font("Arial", Font.BOLD, 12));
        etiMin.setForeground(Color.white);
        etiMin.setOpaque(true);
        etiMin.setBackground(Color.black);
        panelC.add(etiMin);

        etiSeg = new JLabel("00 :");
        etiSeg.setBounds(290, 300, 40, 20);
        etiSeg.setFont(new Font("Arial", Font.BOLD, 12));
        etiSeg.setForeground(Color.white);
        etiSeg.setOpaque(true);
        etiSeg.setBackground(Color.black);
        panelC.add(etiSeg);

        etiMili = new JLabel("00 :");
        etiMili.setBounds(330, 300, 40, 20);
        etiMili.setFont(new Font("Arial", Font.BOLD, 12));
        etiMili.setForeground(Color.white);
        etiMili.setOpaque(true);
        etiMili.setBackground(Color.black);
        panelC.add(etiMili);

        etiNickC = new JLabel("NICKNAME:");
        etiNickC.setBounds(20, 10, 80, 20);
        panelC.add(etiNickC);

        //se establece el boton de historial
        btnH = new JButton("Historial");
        btnH.setBounds(300, 10, 90, 25);
        panelC.add(btnH);

        //se establece el boton de borrar historial              
        btnBH = new JButton("Borrar historial");
        btnBH.setBounds(400, 10, 120, 25);
        panelC.add(btnBH);

        etiChat = new JLabel("CONVERSACION:");
        etiChat.setBounds(20, 60, 180, 20);
        panelC.add(etiChat);

        txtNickC = new JTextField();
        txtNickC.setBounds(90, 10, 120, 25);
        panelC.add(txtNickC);

        areaChat = new JTextArea();
        areaChat.setBounds(40, 80, 500, 220);
        areaChat.setBorder(new LineBorder(Color.BLACK));
        panelC.add(areaChat);

        etiC = new JLabel("MENSAJE:");
        etiC.setBounds(20, 320, 200, 20);
        panelC.add(etiC);

        txtMsg = new JTextField();
        txtMsg.setBounds(20, 340, 470, 25);
        panelC.add(txtMsg);

        //se establece el boton               
        btnE = new JButton("ENVIAR");
        btnE.setBounds(490, 340, 80, 25);
        panelC.add(btnE);

        //Etiqueta para reloj
        JLabel etiReloj = new JLabel("Reloj");
        etiReloj.setBounds(400, 60, 68, 15);
        etiReloj.setFont(new Font("Arial", Font.BOLD, 10));
        etiReloj.setOpaque(true);
        etiReloj.setForeground(Color.white);
        etiReloj.setBackground(Color.black);
        panelC.add(etiReloj);
        //Se inicializa el hilo del rejol
        Relojito = new ThreadReloj(etiReloj);
        hR = new Thread(Relojito, "Reloj");
        hR.start();

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
                            Logger.getLogger(ClienteChat.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        System.out.println("Proceso de " + Thread.currentThread().getName() + " finalizado");
                        break;
                    }
                }
            }
        };
        hCro.start();
        //se estableec el hilo para que el cliente este recibiendo y enviando mensajes al mismo tiempo
        hiloCliente = new Thread(this);
        hiloCliente.start();

        //evento del boton ver historial
        ActionListener escuchaLeeHistorial = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File archivoHistorial = new File("C:/Users/Martha  Nieto/Documents/NetBeansProjects/ACD_Poo3/Historial Chat Cliente.txt");
                try {
                    FileReader entrada = new FileReader(archivoHistorial);
                    BufferedReader bufferL = new BufferedReader(entrada);
                    String texto = "";
                    String linea = "";
                    while (((linea = bufferL.readLine()) != null)) {
                        texto += linea + "\n";
                    }
                    entrada.close();
                    areaChat.setText(texto);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "No se encontro el archivo");
                }
            }
        };
        btnH.addActionListener(escuchaLeeHistorial);

        //evento del boton borrar historial
        ActionListener escuchaBorraHistorial = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File archivoBorrarHistorial = new File("C:/Users/Martha  Nieto/Documents/NetBeansProjects/ACD_Poo3/Historial Chat Cliente.txt");
                if (archivoBorrarHistorial.delete())
                    JOptionPane.showMessageDialog(null, "El historial CLIENTE ha sido borrado satisfactoriamente");
                else JOptionPane.showMessageDialog(null, "El historial no puede ser borrado");
            }
        };
        btnBH.addActionListener(escuchaBorraHistorial);
        //evento del boton enviar
        ActionListener escuchaEnviar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String messageOut = "";
                    messageOut = txtMsg.getText().trim();
                    flujoOut.writeUTF(messageOut);
                    flujoOut.flush();
                    Date horaActual = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh.mm aa");
                    areaChat.append("\n Yo:\t" + messageOut + "        " + sdf.format(horaActual));//muestra el  mensaje enviado
                    historialC = new File("Historial Chat Cliente.txt");
                    PrintStream salida = new PrintStream(historialC);
                    salida.print(areaChat.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR! No se pudo enviar el mensaje al servidor");
                    System.out.println(ex.getMessage());
                }
            }
        };
        btnE.addActionListener(escuchaEnviar);
    }

    //se implementa el proceso del hilo del cliente socket
    @Override
    public void run() {
        try {
            //creacion del socket para poder enviar informacion
            puenteC = new Socket("192.168.0.4", 9999);
            //creamos el stream de entrada y salida
            flujoIn = new DataInputStream(puenteC.getInputStream());
            flujoOut = new DataOutputStream(puenteC.getOutputStream());
            String messageIn = "";

            while (true) { //se establece la lectura del flujo entrante
                messageIn = flujoIn.readUTF();
                Date horaActual = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh.mm aa");
                areaChat.setText(areaChat.getText().trim() + "\n Servidor:\t" + messageIn + "        " + sdf.format(horaActual));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "ERROR! No se pudo realizar la conexion con el servidor");
            System.out.println(ex.getMessage());
        }
    }

}