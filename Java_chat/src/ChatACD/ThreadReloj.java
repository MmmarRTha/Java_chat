/*
 * H1lo del reloj
 */
package ChatACD;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martha Nieto
 */
public class ThreadReloj implements Runnable {
    //se declaran variables del hilo
    String h, m, s, ampm;
    Calendar calendario;
    Thread hReloj;
    JLabel etiReloj;

    //constructor del reloj
    public ThreadReloj(JLabel etiReloj) {
        this.etiReloj = etiReloj;
        hReloj = new Thread(this, "Reloj");
        hReloj.start();
    }

    //comienza el metodo run del hilo reloj
    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hReloj) {
            calcular();
            etiReloj.setText(h + ":" + m + ":" + s + ":" + ampm);
            System.out.println("Proceso de " + Thread.currentThread().getName() + " en ejecución");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadReloj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void calcular() {
        Calendar calendario = new GregorianCalendar();
        Date fechaHoraActual = new Date();

        calendario.setTime(fechaHoraActual);
        ampm = calendario.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        if (ampm.equals("PM")) {
            int hr = calendario.get(Calendar.HOUR_OF_DAY) - 12;
            h = hr > 9 ? "" + hr : "0" + hr;
        } else {
            h = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);
        }
        m = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        s = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
    }
}
