package beauty_salon_explorer.salonExplorer.salon;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "salons")
@Getter
@Setter
@NoArgsConstructor
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String address;

    @Column(nullable = false)
    private String district;

    private String phoneNumber;

    private String websiteUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "salon_services",
            joinColumns = @JoinColumn(name = "salon_id")
    )
    @Column(name = "service")
    private Set<String> services = new HashSet<>();

    private String priceRange;

    private Double rating;

    private Integer reviewCount;

    private String source;

    private String externalId;

    private Double latitude;

    private Double longitude;
}