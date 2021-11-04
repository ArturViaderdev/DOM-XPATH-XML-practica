/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domxpathartur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que contiene el arbol dom y los métodos de la aplicación. Debe ser
 * instanciada.
 *
 * @author Alu2017363
 */
public class Metodos {

    //Esta variable contendrá el árbol dom y todos los métodos para trabajar con el
    private Trabajadom arbol;

    /**
     * Constructor de la clase. Carga el XML al declararse.
     */
    public Metodos() {
        //Inicializa la clase Trabajadom en la variable arbol cargando el fichero messages.xml
        arbol = new Trabajadom("messages.xml");
    }

    /**
     * Muestra una lista de mensajes
     *
     * @param mensajes Lista de objetos de la clase Mensaje
     */
    private void muestramensajes(ArrayList<Mensaje> mensajes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime;
        if (mensajes.isEmpty()) {
            System.out.println("No hay mensajes.");
        } else {
            //Se recorre la lista de mensajes
            for (int cont = 0; cont < mensajes.size(); cont++) {
                //Se muestra el mensaje
                System.out.println("Mensaje número " + (cont + 1));
                //Se convierte la fecha a String para mostrarla
                formatDateTime = mensajes.get(cont).getfecha().format(formatter);
                System.out.println("Fecha del mensaje: " + formatDateTime);
                System.out.println("Emisor: " + mensajes.get(cont).getemisor());
                System.out.println("Receptor: " + mensajes.get(cont).getreceptor());
                System.out.println("Texto: " + mensajes.get(cont).getmensaje());
            }
        }
    }

    /**
     * Permite al usuario introducir un número para seleccionar un mensaje
     *
     * @param Número más bajo permitido
     * @param Número más alto permitido
     * @return Devuelve el número que ha introducido el usuario
     */
    private int entradausuario(int base, int cuantos) {
        int opcion;
        boolean entrado = false;
        Scanner entrada;
        entrada = new Scanner(System.in);
        opcion = -1;
        do {

            try {
                opcion = entrada.nextInt();
                if (opcion >= base && opcion <= cuantos) {
                    entrado = true;
                } else {
                    System.out.println("Opción incorrecta. Vuelve a introducir.");
                }
            } catch (Exception ex) {
                System.out.println("Error de entrada. Vuelve a introducir.");
                entrada.next();
            }
        } while (!entrado);
        return opcion;
    }

    /**
     * Le pide al usuario introducir un número que debe ser desde 1 a un máximo
     *
     * @param Máximo valor permitido
     * @return
     */
    private int selecciona(int numero) {
        int cual;
        System.out.println("Selecciona el mensaje a eliminar");
        cual = entradausuario(1, numero);
        return cual;
    }

    /**
     * Interactua con el usuario con el fin de eliminar un mensaje
     */
    public void eliminarunmensaje() {
        int opcion, numero, cual;
        //El usuario puede elegir si desea ver todos los mensajes o los mensajes de un receptor
        System.out.println("Elige una opción antes de eliminar un mensaje.");
        System.out.println("1-Ver todos los mensajes.");
        System.out.println("2-Ver todos los mensajes de un receptor.");
        System.out.println("0-Cancelar.");
        opcion = entradausuario(0, 2);
        switch (opcion) {
            case 1:
                //Mostrar todos mensajes
                //Se muestran todos los mensajes y se guarda el número de mensajes en la variable número
                numero = vertodosmensajes();
                //El usuario selecciona un número que puede ir desde 1 hasta el número de mensajes
                cual = selecciona(numero);
                if (cual != -1) {
                    //Se elimina el mensaje del árbol
                    arbol.eliminamensaje(cual - 1);
                    //Se graba el fichero XML
                    arbol.grabafichero();
                }
                break;
            case 2:
                //Mostrar mensajes de un receptor
                //El programa pide al usuario que introduzca un receptor y se muestran los mensajes de este
                //En la variable nummensajesreceptor se guardan el nombre del receptor y el número de mensajes
                Cadenanumero nummensajesreceptor = vermensajesde("receptor");
                if (nummensajesreceptor.getnumero() > 0) {
                    //El usuario selecciona un número de mensaje
                    cual = selecciona(nummensajesreceptor.getnumero());
                    if (cual != -1) {
                        //Se elimina el mensaje en la posición introducida del receptor introducido
                        arbol.eliminamensajereceptor(cual - 1, nummensajesreceptor.getcadena());
                        //Se graba el fichero XML
                        arbol.grabafichero();
                    }
                }
                break;
            case -1:
                System.out.println("Error entrada de datos.");
                break;
            case 0:
                System.out.println("Operación cancelada.");
                break;
            default:
                System.out.println("Operación cancelada.");

        }

    }

    /**
     * Se muestran todos los mensajes
     *
     * @return
     */
    public int vertodosmensajes() {
        ArrayList<Mensaje> lista;
        int numero = 0;
        //Se obtienen todos los mensajes del árbol
        lista = arbol.gettodosmensajes();
        //Se muestran los mensajes
        muestramensajes(lista);
        numero = lista.size();
        //Se devuelve el número de mensajes
        return numero;
    }

    /**
     * Interacción con el usuario para modificar un mensaje
     */
    public void modificarmensaje() {
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        int cual;
        String nuevo;
        //Se solicita el usuario que introduzca un emisor
        //Se muestran los mensajes del emisor y se guardan en la variable datosemisor el número de mensajes
        //y el nombre del emisor
        Cadenanumero datosemisor = vermensajesde("emisor");
        if (datosemisor.getnumero() > 0) {
            //Se solicita al usuario que seleccione un número de mensaje
            cual = selecciona(datosemisor.getnumero());
            if (cual != -1) {
                try {
                    //Se solicita al usuario que introduzca el nuevo texto del mensaje
                    System.out.println("Introduce el nuevo mensaje.");
                    nuevo = lector.readLine();
                    //Se modifica el texto de ese mensaje. En la modificación también se pondrá la fecha actual
                    arbol.modificamensajeemisor(cual - 1, datosemisor.getcadena(), nuevo);
                    //Se graba el fichero
                    arbol.grabafichero();
                } catch (IOException ex) {
                    Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    /**
     * Solicita al usuario que introduzca el nombre de un emisor o de un
     * receptor, depende del parámetro que. Se mostrarán los mensajes de ese
     * emisor o ese receptor y se devolverán el nombre del emisor o receptor y
     * el número de mensajes
     *
     * @param que Podrá ser emisor o receptor. Indica por que campo se quieren
     * buscar los mensajes
     * @return El número de mensajes y el nombre del emisor o receptor según el
     * caso
     */
    public Cadenanumero vermensajesde(String que) {
        Cadenanumero resultado;
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        String dato = "";
        ArrayList<Mensaje> lista;
        int numero = 0;
        System.out.println("Introduce el nombre del " + que + " del mensaje.");
        try {
            dato = lector.readLine();
            if (dato != null && !dato.isEmpty()) {

                lista = arbol.getde(que, dato);
                muestramensajes(lista);
                numero = lista.size();

            } else {
                System.out.println("Error de entrada.");
            }
        } catch (IOException ex) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
        }
        resultado = new Cadenanumero(dato, numero);
        return resultado;
    }

    /**
     * Añade un mensaje solicitando los datos al usuario.
     */
    public void anadirmensaje() {
        String emisor, receptor, mensaje;
        LocalDateTime fecha;
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Introduce el nombre del emisor del mensaje.");
        try {
            emisor = lector.readLine();
            System.out.println("Introduce el nombre del receptor del mensaje.");
            receptor = lector.readLine();
            System.out.println("Introduce el texto del mensaje.");
            mensaje = lector.readLine();
            fecha = LocalDateTime.now();
            //Se crea un objeto mensaje
            Mensaje nuevomensaje = new Mensaje(emisor, receptor, mensaje, fecha);
            //Se añade al arbol el objeto mensaje que se transformará
            arbol.anademensaje(nuevomensaje);
            //Se graba el fichero XML
            arbol.grabafichero();
        } catch (IOException ex) {
            System.out.println("Error de entrada:" + ex);
        }
    }
}
