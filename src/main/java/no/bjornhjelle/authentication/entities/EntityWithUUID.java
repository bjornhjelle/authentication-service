package no.bjornhjelle.authentication.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@AllArgsConstructor
public abstract class EntityWithUUID {

    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id  @Type(type = "pg-uuid")
    @Column(nullable = false, unique = true)
    protected UUID id;

    public EntityWithUUID() {
        this.id = UUID.randomUUID();
    }

    @CreationTimestamp
    @Column(name="created_at", nullable = false)
    protected OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    protected OffsetDateTime updatedAt;


}
