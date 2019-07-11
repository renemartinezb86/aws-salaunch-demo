package aws.salaunnch.demo.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Continent.
 */
@Entity
@Table(name = "continent")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "continent")
public class Continent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "continent_name")
    private String continentName;

    @OneToOne
    @JoinColumn(unique = true)
    private Region region;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContinentName() {
        return continentName;
    }

    public Continent continentName(String continentName) {
        this.continentName = continentName;
        return this;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public Region getRegion() {
        return region;
    }

    public Continent region(Region region) {
        this.region = region;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Continent)) {
            return false;
        }
        return id != null && id.equals(((Continent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Continent{" +
            "id=" + getId() +
            ", continentName='" + getContinentName() + "'" +
            "}";
    }
}
