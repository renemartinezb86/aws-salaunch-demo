package aws.salaunnch.demo.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import aws.salaunnch.demo.domain.enumeration.EntryType;

/**
 * A ShipLog.
 */
@Entity
@Table(name = "ship_log")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shiplog")
public class ShipLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "datetime")
    private Instant datetime;

    @Column(name = "entrytext")
    private String entrytext;

    @Enumerated(EnumType.STRING)
    @Column(name = "entrytype")
    private EntryType entrytype;

    @OneToOne
    @JoinColumn(unique = true)
    private Ship ship;

    @OneToOne
    @JoinColumn(unique = true)
    private Marine marine;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public ShipLog datetime(Instant datetime) {
        this.datetime = datetime;
        return this;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public String getEntrytext() {
        return entrytext;
    }

    public ShipLog entrytext(String entrytext) {
        this.entrytext = entrytext;
        return this;
    }

    public void setEntrytext(String entrytext) {
        this.entrytext = entrytext;
    }

    public EntryType getEntrytype() {
        return entrytype;
    }

    public ShipLog entrytype(EntryType entrytype) {
        this.entrytype = entrytype;
        return this;
    }

    public void setEntrytype(EntryType entrytype) {
        this.entrytype = entrytype;
    }

    public Ship getShip() {
        return ship;
    }

    public ShipLog ship(Ship ship) {
        this.ship = ship;
        return this;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Marine getMarine() {
        return marine;
    }

    public ShipLog marine(Marine marine) {
        this.marine = marine;
        return this;
    }

    public void setMarine(Marine marine) {
        this.marine = marine;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipLog)) {
            return false;
        }
        return id != null && id.equals(((ShipLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShipLog{" +
            "id=" + getId() +
            ", datetime='" + getDatetime() + "'" +
            ", entrytext='" + getEntrytext() + "'" +
            ", entrytype='" + getEntrytype() + "'" +
            "}";
    }
}
