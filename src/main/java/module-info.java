module ni.edu.uam.practicahibernate {
    // Módulos de JavaFX.
    requires javafx.controls;
    requires javafx.fxml;

    // API de persistencia y proveedor Hibernate.
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Permite cargar la aplicación y los contrxoladores FXML.
    opens ni.edu.uam.practicahibernate to javafx.fxml;
    opens ni.edu.uam.practicahibernate.controller to javafx.fxml;

    // Hibernate accede a los atributos de Libro mediante
    // reflexión. JavaFX también necesitará sus propiedades
    // para mostrar los datos en las tablas.
    opens ni.edu.uam.practicahibernate.model
            to org.hibernate.orm.core, javafx.base;

    exports ni.edu.uam.practicahibernate;
    exports ni.edu.uam.practicahibernate.controller;
}