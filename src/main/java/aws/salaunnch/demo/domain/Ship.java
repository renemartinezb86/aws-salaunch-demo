package aws.salaunnch.demo.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ship.
 */
@Entity
@Table(name = "ship")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ship")
public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "ship_name", nullable = false)
    private String shipName;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToMany(mappedBy = "ship")
    private Set<Marine> marines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShipName() {
        return shipName;
    }

    public Ship shipName(String shipName) {
        this.shipName = shipName;
        return this;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public Location getLocation() {
        return location;
    }

    public Ship location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Marine> getMarines() {
        return marines;
    }

    public Ship marines(Set<Marine> marines) {
        this.marines = marines;
        return this;
    }

    public Ship addMarine(Marine marine) {
        this.marines.add(marine);
        marine.setShip(this);
        return this;
    }

    public Ship removeMarine(Marine marine) {
        this.marines.remove(marine);
        marine.setShip(null);
        return this;
    }

    public void setMarines(Set<Marine> marines) {
        this.marines = marines;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ship)) {
            return false;
        }
        return id != null && id.equals(((Ship) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ship{" +
            "id=" + getId() +
            ", shipName='" + getShipName() + "'" +
            "}";
    }
}
