package ni.edu.uam.practicahibernate.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import ni.edu.uam.practicahibernate.dao.CategoriaDAO;
import ni.edu.uam.practicahibernate.dao.LibroDAO;
import ni.edu.uam.practicahibernate.model.Categoria;
import ni.edu.uam.practicahibernate.model.Libro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LibroController {

    // Campos del formulario.
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnio;
    @FXML private TextField txtPrecio;

    // Nueva lista desplegable de categorías.
    @FXML private ComboBox<Categoria> cmbCategoria;

    // Botones.
    @FXML private Button btnGuardar;
    @FXML private Button btnCargar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    // Tabla y columnas.
    @FXML private TableView<Libro> tblLibros;

    @FXML private TableColumn<Libro, Long> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, BigDecimal> colPrecio;
    @FXML private TableColumn<Libro, Categoria> colCategoria;

    // Cada DAO se encarga de su respectiva entidad.
    private final LibroDAO libroDAO = new LibroDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    // ID del libro actualmente seleccionado.
    private Long idSeleccionado = null;

    @FXML
    public void initialize() {

        // Asociamos las columnas con los getters de Libro.
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo"));

        colAutor.setCellValueFactory(
                new PropertyValueFactory<>("autor"));

        colAnio.setCellValueFactory(
                new PropertyValueFactory<>("anioPublicacion"));

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio"));

        // Asociamos la columna con la propiedad categoria.
        // PropertyValueFactory obtiene el objeto Categoria
        // mediante getCategoria() de la entidad Libro.
        // JavaFX utiliza su toString() para mostrar el nombre.
        colCategoria.setCellValueFactory(
                new PropertyValueFactory<>("categoria"));

        // Detectamos cuando se selecciona un libro.
        tblLibros.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, actual) -> {

                    if (actual != null) {
                        seleccionarLibro(actual);
                    }
                });

        actualizarBotones();

        // Cargamos las categorías cuando inicia la vista.
        // Si falla PostgreSQL, mostramos el error.
        try {
            cargarCategorias();
        } catch (RuntimeException ex) {
            mostrarError(
                    "No se pudieron cargar las categorías.", ex
            );
        }
    }

    // Consulta las categorías y las muestra en el ComboBox.
    private void cargarCategorias() {

        List<Categoria> categorias = categoriaDAO.listar();

        cmbCategoria.setItems(
                FXCollections.observableArrayList(categorias)
        );
    }

    // CREATE: registra un nuevo libro.
    @FXML
    private void guardarLibro() {

        Libro libro = obtenerFormulario();

        if (libro == null) {
            return;
        }

        try {
            libroDAO.guardar(libro);

        } catch (RuntimeException ex) {
            mostrarError("No se pudo guardar el libro.", ex);
            return;
        }

        limpiarCampos();
        cargarLibros();
    }

    // READ: muestra los libros registrados.
    @FXML
    private void cargarLibros() {

        try {
            // Obtenemos los libros con sus categorías.
            List<Libro> libros = libroDAO.listar();

            // Actualizamos también las categorías disponibles.
            cargarCategorias();

            // Limpiamos la selección anterior.
            limpiarCampos();

            tblLibros.setItems(
                    FXCollections.observableArrayList(libros)
            );

        } catch (RuntimeException ex) {
            mostrarError("No se pudieron cargar los libros.", ex);
        }
    }

    // UPDATE: modifica el libro seleccionado.
    @FXML
    private void modificarLibro() {

        if (idSeleccionado == null) {
            return;
        }

        Libro libro = obtenerFormulario();

        if (libro == null) {
            return;
        }

        // Utilizamos el ID del registro seleccionado.
        libro.setId(idSeleccionado);

        try {
            libroDAO.actualizar(libro);

        } catch (RuntimeException ex) {
            mostrarError("No se pudo modificar el libro.", ex);
            return;
        }

        limpiarCampos();
        cargarLibros();
    }

    // DELETE: elimina el libro seleccionado.
    @FXML
    private void eliminarLibro() {

        if (idSeleccionado == null) {
            return;
        }

        Alert confirmacion =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar libro");
        confirmacion.setContentText(
                "¿Está seguro de eliminar el libro seleccionado?"
        );

        Optional<ButtonType> resultado =
                confirmacion.showAndWait();

        // Si el usuario cancela, no hacemos cambios.
        if (resultado.isEmpty()
                || resultado.get() != ButtonType.OK) {
            return;
        }

        try {
            boolean eliminado =
                    libroDAO.eliminar(idSeleccionado);

            if (!eliminado) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "El libro ya no existe en la base de datos."
                );
            }

        } catch (RuntimeException ex) {
            mostrarError("No se pudo eliminar el libro.", ex);
            return;
        }

        limpiarCampos();
        cargarLibros();
    }

    // Limpia los campos y la categoría seleccionada.
    @FXML
    private void limpiarCampos() {

        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();

        cmbCategoria.setValue(null);

        idSeleccionado = null;
        tblLibros.getSelectionModel().clearSelection();

        actualizarBotones();
    }

    // Muestra los datos del libro seleccionado.
    private void seleccionarLibro(Libro libro) {

        idSeleccionado = libro.getId();

        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());

        txtAnio.setText(
                String.valueOf(libro.getAnioPublicacion())
        );

        txtPrecio.setText(
                libro.getPrecio().toPlainString()
        );

        // Seleccionamos la categoría equivalente
        // dentro de las opciones del ComboBox.
        cmbCategoria.setValue(
                buscarCategoria(libro.getCategoria())
        );

        actualizarBotones();
    }

    // Localiza la categoría utilizando su identificador.
    // No comparamos referencias porque las entidades
    // pueden provenir de distintas consultas JPA.
    private Categoria buscarCategoria(Categoria categoria) {

        if (categoria == null) {
            return null;
        }

        for (Categoria actual : cmbCategoria.getItems()) {

            if (actual.getId().equals(categoria.getId())) {
                return actual;
            }
        }

        return null;
    }

    // Habilita modificar y eliminar solo cuando
    // existe un libro seleccionado.
    private void actualizarBotones() {

        boolean sinSeleccion = idSeleccionado == null;

        btnModificar.setDisable(sinSeleccion);
        btnEliminar.setDisable(sinSeleccion);
    }

    // Valida los campos y construye un objeto Libro.
    private Libro obtenerFormulario() {

        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "El título y el autor son obligatorios."
            );
            return null;
        }

        if (titulo.length() > 150 || autor.length() > 150) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "El título y el autor admiten hasta 150 caracteres."
            );
            return null;
        }

        // Convertimos y validamos el año.
        int anio;

        try {
            anio = Integer.parseInt(
                    txtAnio.getText().trim()
            );
        } catch (NumberFormatException ex) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "El año debe ser un número entero."
            );
            return null;
        }

        if (anio < 1450 || anio > 2100) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "El año debe estar entre 1450 y 2100."
            );
            return null;
        }

        // Convertimos y validamos el precio.
        BigDecimal precio;

        try {
            precio = new BigDecimal(
                    txtPrecio.getText().trim()
            );
        } catch (NumberFormatException ex) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "El precio debe ser un número válido."
            );
            return null;
        }

        if (precio.signum() < 0) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "El precio no puede ser negativo."
            );
            return null;
        }

        if (precio.scale() > 2
                || precio.compareTo(
                new BigDecimal("9999999999.99")) > 0) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "El precio admite hasta 10 dígitos enteros y 2 decimales."
            );
            return null;
        }

        // La categoría es obligatoria porque la columna
        // categoria_id tiene la restricción NOT NULL.
        Categoria categoria = cmbCategoria.getValue();

        if (categoria == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Debe seleccionar una categoría."
            );
            return null;
        }

        // Construimos la entidad con los datos validados.
        Libro libro = new Libro();

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnioPublicacion(anio);
        libro.setPrecio(precio);
        libro.setCategoria(categoria);

        return libro;
    }

    // Registra el error completo en consola
    // y presenta una alerta al usuario.
    private void mostrarError(
            String mensaje, RuntimeException ex) {

        ex.printStackTrace();

        mostrarAlerta(
                Alert.AlertType.ERROR,
                mensaje + " " + ex.getMessage()
        );
    }

    // Método reutilizable para mostrar alertas.
    private void mostrarAlerta(
            Alert.AlertType tipo, String mensaje) {

        Alert alerta = new Alert(tipo, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}