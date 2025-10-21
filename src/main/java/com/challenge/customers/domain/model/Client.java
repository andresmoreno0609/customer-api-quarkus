package com.challenge.customers.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

/**
 * Entidad principal que representa al cliente.
 * Se apoya en Panache para simplificar operaciones de persistencia.
 */
@Entity
@Table(
        name = "client",
        uniqueConstraints = @UniqueConstraint(columnNames = "num_cta"),
        indexes = {
                @Index(name = "idx_client_name", columnList = "name"),
                @Index(name = "idx_client_country", columnList = "country_id"),
                @Index(name = "idx_client_status", columnList = "status_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends PanacheEntityBase {

    /** Identificador autoincremental */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del cliente */
    @Column(nullable = false, length = 255)
    private String name;

    /** Fecha de nacimiento */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /** Relación con el catálogo de géneros */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gender_id", nullable = false)
    private CatalogGender gender;

    /** Número de cuenta único */
    @Column(name = "num_cta", nullable = false, length = 15, unique = true)
    private String numCta;

    /** Relación con el catálogo de estado */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private CatalogStatus status;

    /** Relación con el catálogo de país */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CatalogCountry country;

    /** Fecha en la que se activó el cliente */
    @Column(name = "activated_date")
    private Instant activatedDate;

    /** Fecha en la que se inactivó el cliente */
    @Column(name = "inactivated_date")
    private Instant inactivatedDate;

    /** Fecha de creación y actualización del registro */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Callbacks automáticos para timestamps */
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /** Helper de dominio */
    public boolean isActive() {
        return this.status != null && "ACTIVE".equalsIgnoreCase(this.status.getName());
    }
}
