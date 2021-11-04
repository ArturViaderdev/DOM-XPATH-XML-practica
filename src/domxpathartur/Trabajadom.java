/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domxpathartur;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Clase que contiene el árbol DOM y los métodos para trabajarlo
 * @author Alu2017363
 */
public class Trabajadom {

    Document documento;
    File f;

    /**
     * Constructor.
     * @param archivo Nombre del archivo XML que se cargará al instanciar la clase
     */
    public Trabajadom(String archivo) {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            f = new File(archivo);
            //Se carga el archivo 
            documento = builder.parse(f);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Trabajadom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Trabajadom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Trabajadom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Transforma un nodo a mensaje de la clase Mensaje
     * @param elmensaje Nodo que contiene el mensaje
     * @return Devuelve el mensaje
     */
    public Mensaje nodetomensajer(Node elmensaje) {
        NodeList datos;
        String emisor, texto, receptor;
        emisor = "";
        receptor = "";
        texto = "";
        LocalDateTime fecha;
        Mensaje nuevomensaje;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //Se transforma el atributo fecha de string a LocalDateTime
        fecha = LocalDateTime.parse(((Element) elmensaje).getAttribute("fechahora"), formatter);
        //Se obtienen los nodos hijos del mensaje
        datos = elmensaje.getChildNodes();
        //Se recorren los nodos
        for (int cont = 0; cont < datos.getLength(); cont++) {
            switch (datos.item(cont).getNodeName()) {
                case "emisor":
                    //Nodo emisor - Se recoge el valor del emisor
                    emisor = datos.item(cont).getTextContent();
                    break;
                case "receptor":
                    //Nodo receptor - Se recoge el valor del receptor
                    receptor = datos.item(cont).getTextContent();
                    break;
                case "texto":
                    //Nodo texto - Se recoge el valor del cuerpo del mensaje
                    texto = datos.item(cont).getTextContent();
            }
        }
        //Se crea un nuevo objeto mensaje con los valores leidos
        nuevomensaje = new Mensaje(emisor, receptor, texto, fecha);
        //Se devuelve el mensaje
        return nuevomensaje;
    }

    /**
     * Obtiene todos los mensajes y los devuelve como mensajes de la clase mensaje
     * @return Lista de mensajes
     */
    public ArrayList<Mensaje> gettodosmensajes() {
        ArrayList<Mensaje> mensajes;
        //Se obtienen los nodos hijos del primer nodo, los mensajes
        NodeList resultado = documento.getFirstChild().getChildNodes();
        mensajes = new ArrayList<>();
        //Se recorren los nodos
        for (int i = 1; i < resultado.getLength(); i++) {
            //Si el nodo es del tipo elemento
            if (resultado.item(i).getNodeType() == Node.ELEMENT_NODE) {
                //Se añade el nodo transformado a mensaje en la lista de mensajes
                mensajes.add(nodetomensajer(resultado.item(i)));
            }
        }
        //Se devuelve la lista de mensajes
        return mensajes;
    }

    /**
     * Obtiene los mensajes de un emisor o de un receptor según el parámetro nnodo
     * @param nnodo Puede contener emisor o receptor y decidirá por que campo se buscará
     * @param valor Nombre del emisor o receptor del que se buscaran sus mensajes
     * @return Lista de mensajes de la clase mensaje con los resultados de la búsqueda
     */
    public ArrayList<Mensaje> getde(String nnodo, String valor) {
        ArrayList<Mensaje> mensajes;
        Node nmensaje;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            //Se obtienen los mensajes de un emisor concreto o un receptor
            XPathExpression exp = xpath.compile("/mensajes/mensaje[./" + nnodo + "='" + valor + "']");
            NodeList resultado = (NodeList) exp.evaluate(documento, XPathConstants.NODESET);
            mensajes = new ArrayList<>();
            for (int i = 0; i < resultado.getLength(); i++) {
                nmensaje = resultado.item(i);
                //Se convierte el mensaje de nodo a Mensaje y se añade a la lista
                mensajes.add(nodetomensajer(nmensaje));
            }
            //Se devuelve la lista
            return mensajes;
        } catch (XPathExpressionException ex) {
            System.out.println(ex.getMessage());     
            return null;
        }
    }

    /**
     * Modifica un mensaje de un emisor concreto
     * @param cual Número de mensaje del emisor a modificar
     * @param emisor Nombre del emisor
     * @param mensaje Texto nuevo del mensaje
     */
    public void modificamensajeemisor(int cual, String emisor, String mensaje) {
        boolean sal,encontrado;
        sal =false;
        encontrado = false;
        int cont;
        try {
            LocalDateTime fecha;
            NodeList datos;
            Node mensajes = documento.getFirstChild();
            XPath xpath = XPathFactory.newInstance().newXPath();
            //Se obtienen los mensajes de un emisor concreto
            XPathExpression exp = xpath.compile("/mensajes/mensaje[./emisor='" + emisor + "']");
            NodeList resultado = (NodeList) exp.evaluate(documento, XPathConstants.NODESET);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //Se convierte la fecha actual a string
            fecha = LocalDateTime.now();
            String formatDateTime = fecha.format(formatter);
            //Se cambia la fecha del mensaje por la actual
            ((Element) resultado.item(cual)).setAttribute("fechahora", formatDateTime);
            //Se obtienen los nodos hijos del mensaje
            datos = resultado.item(cual).getChildNodes();
            cont=0;
            //Se busca el nodo texto para cambiarlo
            do
            {
             if(cont<datos.getLength())
             {
                 //Se encuentra el nodo texto
                 if(datos.item(cont).getNodeName().equals("texto"))
                 {
                     encontrado=true;
                     sal=true;
                 }
                 else
                 {
                     cont++;
                 }
             }
             else
             {
                 sal=true;
             }
            }while(!sal);
            if(encontrado)
            {
                //Una vez encontrado el nodo texto se cambia el texto del mensaje
                datos.item(cont).setTextContent(mensaje);
                System.out.println("Modificación realizada.");
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(Trabajadom.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Elimina un mensaje de un receptor concreto
     * @param cual Número de mensaje del receptor a eliminar
     * @param receptor Nombre del receptor
     */
    public void eliminamensajereceptor(int cual, String receptor) {
        try {
            Node mensajes = documento.getFirstChild();
          
            XPath xpath = XPathFactory.newInstance().newXPath();
            //Obtenemos los mensajes de un receptor concreto
            XPathExpression exp = xpath.compile("/mensajes/mensaje[./receptor='" + receptor + "']");
            NodeList resultado = (NodeList) exp.evaluate(documento, XPathConstants.NODESET);
            //Eliminamos el mensaje cuyo número viene indicado en la variable cual
            mensajes.removeChild(resultado.item(cual));
            System.out.println("Mensaje eliminado");
        } catch (XPathExpressionException ex) {
            Logger.getLogger(Trabajadom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Elimina un mensaje
     * @param cual Número de mensaje de la lista completa a eliminar
     */
    public void eliminamensaje(int cual) {
        boolean sal, encontrado;
        //Se obtienen los nodos hijos del primer nodo - Los mensajes
        Node mensajes = documento.getFirstChild();
        NodeList lista = mensajes.getChildNodes();
        int cont = 0;
        int cuantos = -1;
        sal = false;
        encontrado = false;
        //Se recorre la lista de mensajes
        do {
            if (cont < lista.getLength()) {
                //Cuando el elemento de la lista es un nodo es que es un mensaje
                if (lista.item(cont).getNodeType() == Node.ELEMENT_NODE) {
                    //Se incrementa el contador de mensajes
                    cuantos++;
                    //Si se encuentra el mensaje que se busca para eliminar
                    if (cuantos == cual) {
                        encontrado = true;
                        sal = true;
                    } else {
                        cont++;
                    }
                } else {
                    cont++;
                }
            } else {
                sal = true;
            }

        } while (!sal);
        if (encontrado) {
            //Se elimina el mensaje
            mensajes.removeChild(lista.item(cont));
        }
        System.out.println("Mensaje eliminado.");
    }

    /**
     * Graba el fichero XML
     */
    public void grabafichero() {
        try {     
            OutputFormat format = new OutputFormat(documento);    
            format.setIndenting(true);    
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(f), format);    
            serializer.serialize(documento);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Trabajadom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Trabajadom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Añade un mensaje
     * @param elmensaje Mensaje a añadir de la clase Mensaje
     */
    public void anademensaje(Mensaje elmensaje) {
        //Se obtiene el primer hijo - La etiqueta mensajes
        Node raiz = documento.getFirstChild();
        //Se crea un nodo mensaje
        Node correo = documento.createElement("mensaje");
        //Se obtiene la variable elemento del nodo
        Element elemento = (Element) correo;
        //Se transforma la fecha a String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = elmensaje.getfecha().format(formatter);
        //Se introduce la fecha en el elemento
        elemento.setAttribute("fechahora", formatDateTime);
        //Se crea un nodo emisor
        Node nemisor = documento.createElement("emisor");
        //Se introduce el nombre del emisor
        Node textonemisor = documento.createTextNode(elmensaje.getemisor());
        //Se añade el texto del nombre al nodo
        nemisor.appendChild(textonemisor);
        //Se añade el nodo emisor al mensaje
        correo.appendChild(nemisor);
        //Se repite el procedimiento con el receptor y el texto
        Node nreceptor = documento.createElement("receptor");
        Node textonreceptor = documento.createTextNode(elmensaje.getreceptor());
        nreceptor.appendChild(textonreceptor);
        correo.appendChild(nreceptor);
        Node ntexto = documento.createElement("texto");
        Node ntextot = documento.createTextNode(elmensaje.getmensaje());
        ntexto.appendChild(ntextot);
        correo.appendChild(ntexto);
        //Se añade el mensaje a la raiz mensajes
        raiz.appendChild(correo);
    }
}
