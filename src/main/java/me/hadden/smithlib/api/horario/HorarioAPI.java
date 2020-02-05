package me.hadden.smithlib.api.horario;

import java.text.*;
import java.util.*;

public class HorarioAPI
{
    public static SimpleDateFormat sdf;
    public static Calendar ca;
    
    static {
        HorarioAPI.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        HorarioAPI.ca = new GregorianCalendar(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
    
    public static String getHorario() {
    	final TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        final Calendar calendar = Calendar.getInstance(tz);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm ");
        return sdf.format(calendar.getTime());
    }
}
