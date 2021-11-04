/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domxpathartur;

/**
 *
 * @author alu2017363
 * Esta clase sirve para devolver juntos un String y un entero.
 * Se utiliza para devolver un receptor seleccionado por el usuario y su número de mensajes
 * y también para el caso de un emisor y su número de mensajes
 * No he implementado métodos set ya que los valores se establecen en el new
 */
public class Cadenanumero {
    private String cadena;
    private int numero;
    
    /**
     * Constructor que recibe los valores a guardar en la clase
     * @param cadena String a guardar
     * @param numero Entero a guardar
     */
    public Cadenanumero(String cadena, int numero)
    {
        this.cadena = cadena;
        this.numero = numero;
    }
    
    /**
     * Devuelve el string
     * @return El string
     */
    public String getcadena()
    {
        return cadena;
    }
    
    /**
     * Devuelve el entero
     * @return El entero
     */
    public int getnumero()
    {
        return numero;
    }
}
