module ni.edu.uam.practicahibernate {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Base de datos
    requires java.sql;
    requires org.postgresql.jdbc;

    // Jakarta Persistence
    requires jakarta.persistence;

    // Hibernate
    requires org.hibernate.orm.core;

    // Exportar paquete principal
    exports ni.edu.uam.practicahibernate ;

    // Permitir acceso a los controladores JavaFX
    opens ni.edu.uam.practicahibernate to javafx.fxml;

    // Permitir que Hibernate acceda a las entidades
    opens ni.edu.uam.practicahibernate.model
            to org.hibernate.orm.core, javafx.base;
}