package Comunicacion;

import com.sun.nio.sctp.IllegalReceiveException;

import javax.sound.sampled.Line;

public record Mensaje(String tipo, String origen, String token, long lamport, String msgId, String data) {

    private static final int CAMPOS = 4;

    private String serializar(){
        return String.join(";", limpiar(tipo), limpiar(origen), Long.toString(lamport), limpiar(msgId), limpiar(data));
    }

    public static Mensaje parsear(String linea){
        if(linea == null || linea.isBlank()){
            throw new IllegalArgumentException("mensaje vacio");
        }
        String[] p = linea.split(";", CAMPOS);
        if (p.length != CAMPOS){
            throw new IllegalArgumentException("Se esperaban: " + CAMPOS + " campos y llegaron: " + p.length);
        }
        long reloj;
        try {
            reloj = Long.parseLong(p[3]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("El reloj no es un numero: " + p[3]);
        }
        if (reloj < 0 || p[0].isEmpty() || p[1].isEmpty()){
            throw new IllegalArgumentException("Tipo, origen o reloj son invalidos");
        }
        return new Mensaje(p[0], p[1], p[2], reloj, p[4], p[5]);
    }

    private static String limpiar(String s){
        return s == null ? "" : s.replace(";", ",");
    }

}
