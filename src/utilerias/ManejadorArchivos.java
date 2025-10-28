
package utilerias;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManejadorArchivos {
    private static final String SEPARADOR = ",";
    private static final String DIRECTORIO_DATOS = "datos/";

    public ManejadorArchivos() {
        File directorio = new File("datos/");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

    }

    public void guardarDatos(String nombreArchivo, List<String[]> datos) {
        String rutaArchivo = "datos/" + nombreArchivo + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for(String[] fila : datos) {
                String linea = String.join(",", fila);
                writer.write(linea);
                writer.newLine();
            }

            System.out.println("Datos guardados exitosamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar datos en archivo: " + e.getMessage());
        }

    }

    public List<String[]> cargarDatos(String nombreArchivo) {
        String rutaArchivo = "datos/" + nombreArchivo + ".csv";
        List<String[]> datos = new ArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            while(true) {
                String linea;
                if ((linea = reader.readLine()) == null) {
                    System.out.println("Datos cargados exitosamente desde: " + rutaArchivo);
                    break;
                }

                if (!linea.trim().isEmpty()) {
                    String[] campos = linea.split(",");

                    for(int i = 0; i < campos.length; ++i) {
                        campos[i] = campos[i].trim();
                    }

                    datos.add(campos);
                }
            }
        } catch (FileNotFoundException var10) {
            System.out.println("Archivo no encontrado: " + rutaArchivo + ". Se creará al guardar datos.");
        } catch (IOException e) {
            System.err.println("Error al cargar datos desde archivo: " + e.getMessage());
        }

        return datos;
    }

    public boolean archivoExiste(String nombreArchivo) {
        String rutaArchivo = "datos/" + nombreArchivo + ".csv";
        return (new File(rutaArchivo)).exists();
    }

    public boolean eliminarArchivo(String nombreArchivo) {
        String rutaArchivo = "datos/" + nombreArchivo + ".csv";
        File archivo = new File(rutaArchivo);
        return archivo.delete();
    }

    public boolean crearRespaldo(String nombreArchivo) {
        String rutaOriginal = "datos/" + nombreArchivo + ".csv";
        String rutaRespaldo = "datos/" + nombreArchivo + "_backup.csv";

        try {
            File archivoOriginal = new File(rutaOriginal);
            File archivoRespaldo = new File(rutaRespaldo);
            if (!archivoOriginal.exists()) {
                return false;
            } else {
                String linea;
                try (
                        BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(archivoRespaldo));
                ) {
                    while((linea = reader.readLine()) != null) {
                        writer.write(linea);
                        writer.newLine();
                    }
                }

                System.out.println("Respaldo creado: " + rutaRespaldo);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error al crear respaldo: " + e.getMessage());
            return false;
        }
    }
}
