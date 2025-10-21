package com.challenge.customers.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gender_id", nullable = false)
    private CatalogGender gender;

    @Column(name = "num_cta", nullable = false, length = 15, unique = true)
    private String numCta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private CatalogStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CatalogCountry country;

    @Column(name = "activated_date")
    private Instant activatedDate;

    @Column(name = "inactivated_date")
    private Instant inactivatedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public boolean isActive() {
        return this.status != null && "ACTIVE".equalsIgnoreCase(this.status.getName());
    }
}
