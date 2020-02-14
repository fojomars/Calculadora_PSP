/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoraservidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class CalculadoraServidor extends Thread {

    private Socket clientSocket;

    public CalculadoraServidor(Socket socket) {
        clientSocket = socket;
    }

    public void run() {
        try {
            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();

            System.out.println("Preguntando por primer operando");
            String envio = "Envíe el primer operando";
            os.write(envio.getBytes());
            
            System.out.println("Recibiendo primer operando");
            byte[] mensaje = new byte[40];
            is.read(mensaje);
            
            double num1 = Double.parseDouble(new String(mensaje));
            System.out.println("Preguntando por segundo operando");
            envio = "Envíe el segundo operando";
            os.write(envio.getBytes());
            
            System.out.println("Recibiendo segundo operando");
            mensaje = new byte[25];
            is.read(mensaje);
            double num2 = Double.parseDouble(new String(mensaje));
            
            System.out.println("Preguntando por el tipo de operación");
            envio = "Envíe el tipo de operación: 1=suma, 2=resta, 3=mult, 4=div";
            os.write(envio.getBytes());
            
            System.out.println("Recibiendo tipo de operación");
            mensaje = new byte[25];
            is.read(mensaje);
            
            double operacion = Double.parseDouble(new String(mensaje));
            double resultado;
            if (operacion == 1.0) {
                resultado = num1 + num2;
                envio = num1 + " + " + num2 + " = " + resultado;
                os.write(envio.getBytes());
            } else if (operacion == 2.0) {
                resultado = num1 - num2;
                envio = num1 + " - " + num2 + " = " + resultado;
                os.write(envio.getBytes());
            } else if (operacion == 3.0) {
                resultado = num1 * num2;
                envio = num1 + " * " + num2 + " = " + resultado;
                os.write(envio.getBytes());
            } else if (operacion == 4.0) {
                resultado = num1 / num2;
                envio = num1 + " / " + num2 + " = " + resultado;
                os.write(envio.getBytes());
            } else {
                envio = "Error. Operación incorrecta";
                os.write(envio.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Creando socket servidor");
        ServerSocket serverSocket = new ServerSocket();
        System.out.println("Realizando el bind");

        int puerto = Integer.parseInt(JOptionPane.showInputDialog("Escribe el puerto: "));
        InetSocketAddress addr = new InetSocketAddress("192.168.0.1", puerto);
        serverSocket.bind(addr);

        System.out.println("Aceptando conexiones");

        while (serverSocket != null) {
            Socket newSocket = serverSocket.accept();
            System.out.println("Conexión recibida");

            CalculadoraServidor hilo = new CalculadoraServidor(newSocket);
            hilo.start();
        }
        System.out.println("Terminado");
    }
}
