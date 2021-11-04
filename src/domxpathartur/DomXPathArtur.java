/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domxpathartur;

import java.io.IOException;
import java.util.Scanner;

/**
 * Clase main de la aplicación
 * @author Alu2017363
 */
public class DomXPathArtur {

    /**
     * Inicio de la app
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Inicializo la clase de métodos en una variable. Contiene los métodos y el árbol
        Metodos codigo = new Metodos();
        Scanner entrada = new Scanner(System.in);
        int numero=0;     
        int opcion=-1;
        do {
            try {
                //Menú principal
                System.out.println("0-Salir");
                System.out.println("1-Añadir mensaje");
                System.out.println("2-Ver mensajes de un receptor contreto");
                System.out.println("3-Borrar un mensaje");
                System.out.println("4-Ver todos los mensajes");
                System.out.println("5-Ver mensajes de un emisor.");
                System.out.println("6-Modificar mensaje de un emisor.");
                opcion = entrada.nextInt();
                switch(opcion)
                {
                    case 0:
                        System.out.println("Adios.");
                        break;
                    case 1:
                        //Añadir un mensaje
                        codigo.anadirmensaje();
                        break;
                    case 2:
                        //Ver mensajes de un receptor
                        //Se muestran los mensajes de un receptor seleccionado por el usuario
                        //Y se guardan en la variable datosreceptor el nombre del receptor y el número de mensajes.
                        //El método vermensajesde se reutiliza en eliminar y modificar.
                        Cadenanumero datosreceptor = codigo.vermensajesde("receptor");
                        System.out.println("Hay " + datosreceptor.getnumero() + " mensajes del receptor " + datosreceptor.getcadena() + ".");
                        break;
                    case 3:
                        //Se elimina un mensaje seleccionado por el usuario
                        codigo.eliminarunmensaje();
                        break;
                    case 4:
                        //Se muestran todos los mensajes y se guarda en la variable número el número de mensajes existentes
                        numero = codigo.vertodosmensajes();
                        System.out.println("Hay " + numero + " mensajes.");
                        break;
                    case 5:
                        //Se muestran los mensajes de un emisor seleccionado por el usuario y se guarda en la variable
                        //datosemisor el número de mensajes y el nombre del emisor
                        Cadenanumero datosemisor = codigo.vermensajesde("emisor");
                        System.out.println("Hay " + datosemisor.getnumero() + " mensajes del emisor " + datosemisor.getcadena() + ".");
                        break;
                    case 6:
                        //Modificar un mensaje seleccionado por el usuario
                        codigo.modificarmensaje();
                        break;
                    default:
                        System.out.println("Opción incorrecta.");
                }
                
            } catch (Exception ex) {
                System.out.println("Error entrando opción del menú. El error es:" + ex.getMessage());
                entrada.next();
            }
        }while(opcion!=0);
    }

}
