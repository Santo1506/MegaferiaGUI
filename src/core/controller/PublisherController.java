package core.controller;

import core.model.Manager;
import core.model.Publisher;
import core.repository.PersonRepository;
import core.repository.PublisherRepository;
import core.util.Response;
import core.util.StatusCode;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para editoriales.
 */
public class PublisherController {

    private final PublisherRepository publisherRepository;
    private final PersonRepository personRepository;

    public PublisherController(PublisherRepository publisherRepository, PersonRepository personRepository) {
        this.publisherRepository = publisherRepository;
        this.personRepository = personRepository;
    }

    public Response<Publisher> crearEditorial(String nit, String nombre, String direccion, String idGerenteTexto) {
        if (nit == null || nombre == null || direccion == null || idGerenteTexto == null
                || nit.isEmpty() || nombre.isEmpty() || direccion.isEmpty() || idGerenteTexto.isEmpty()) {
            return new Response<>(StatusCode.ERROR_VALIDACION, "Todos los campos de la editorial son obligatorios.");
        }
        if (!nit.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d")) {
            return new Response<>(StatusCode.ERROR_VALIDACION, "El NIT debe tener el formato XXX.XXX.XXX-X.");
        }
        long idGerente;
        try {
            idGerente = Long.parseLong(idGerenteTexto);
        } catch (NumberFormatException e) {
            return new Response<>(StatusCode.ERROR_VALIDACION, "El ID del gerente debe ser numérico.");
        }
        if (publisherRepository.existeNit(nit)) {
            return new Response<>(StatusCode.ERROR_DUPLICADO, "Ya existe una editorial con ese NIT.");
        }
        Manager gerente = personRepository.buscarGerente(idGerente);
        if (gerente == null) {
            return new Response<>(StatusCode.ERROR_NO_ENCONTRADO, "El gerente seleccionado no existe.");
        }
        if (gerente.getEditorial() != null) {
            return new Response<>(StatusCode.ERROR_VALIDACION, "El gerente ya está asignado a otra editorial.");
        }
        Publisher editorial = new Publisher(nit, nombre, direccion, gerente);
        gerente.setEditorial(editorial);
        publisherRepository.guardar(editorial);
        return new Response<>(StatusCode.SUCCESS, "Editorial creada correctamente.", editorial);
    }

    public Response<List<Publisher>> obtenerEditoriales() {
        List<Publisher> copias = new ArrayList<>();
        for (Publisher editorial : publisherRepository.obtenerOrdenados()) {
            copias.add(editorial.copiar());
        }
        return new Response<>(StatusCode.SUCCESS, "Editoriales listadas.", copias);
    }

    public Publisher buscarPorNit(String nit) {
        return publisherRepository.buscarPorNit(nit);
    }
}
