/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domxpathartur;

import java.time.LocalDateTime;

/**
 * Guarda un mensaje con sus datos.
 * No he implementado métodos get ya que los datos se introducen en el constructor
 * @author Alu2017363
 */
public class Mensaje {
    private String emisor;
    private String receptor;
    private String mensaje;
    private LocalDateTime fecha;
    
    /**
     * Constructor con los datos a guardar
     * @param emisor El emisor del mensaje
     * @param receptor El receptor del mensaje
     * @param mensaje El texto del mensaje
     * @param fecha La fecha del mensaje
     */
    public Mensaje(String emisor,String receptor,String mensaje,LocalDateTime fecha)
    {
        this.emisor = emisor;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }
    
    /**
     * Devuelve el emisor del mensaje
     * @return Emisor del mensaje
     */
    public String getemisor()
    {
        return emisor;
    }
    
    /**
     * Devuelve el receptor del mensaje
     * @return Receptor del mensaje
     */
    public String getreceptor()
    {
        return receptor;
    }
    
    /**
     * Devuelve el texto del mensaje
     * @return Texto del mensaje
     */
    public String getmensaje()
    {
        return mensaje;
    }
    
    /**
     * Devuelve la fecha del mensaje
     * @return Fecha del mensaje
     */
    public LocalDateTime getfecha()
    {
        return fecha;
    }   
}
