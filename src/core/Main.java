package core;

import com.formdev.flatlaf.FlatDarkLaf;
import core.controller.BookController;
import core.controller.CompraController;
import core.controller.PersonController;
import core.controller.PublisherController;
import core.controller.StandController;
import core.repository.BookRepository;
import core.repository.PersonRepository;
import core.repository.PublisherRepository;
import core.repository.StandRepository;
import javax.swing.UIManager;

/**
 * Clase Main encargada de iniciar la aplicación y conectar MVC.
 */
public class Main {

    public static void main(String[] args) {
        System.setProperty("flatlaf.useNativeLibrary", "false");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo iniciar el tema visual");
        }

        StandRepository standRepository = new StandRepository();
        PersonRepository personRepository = new PersonRepository();
        PublisherRepository publisherRepository = new PublisherRepository();
        BookRepository bookRepository = new BookRepository();

        StandController standController = new StandController(standRepository);
        PersonController personController = new PersonController(personRepository);
        PublisherController publisherController = new PublisherController(publisherRepository, personRepository);
        BookController bookController = new BookController(bookRepository, personRepository, publisherRepository);
        CompraController compraController = new CompraController(standRepository, publisherRepository);

        MegaferiaFrame frame = new MegaferiaFrame(standController, personController, publisherController, bookController, compraController,
                standRepository, personRepository, publisherRepository, bookRepository);
        frame.setVisible(true);
    }
}
