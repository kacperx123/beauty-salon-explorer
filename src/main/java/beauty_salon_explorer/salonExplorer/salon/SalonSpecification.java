package beauty_salon_explorer.salonExplorer.salon;

import org.springframework.data.jpa.domain.Specification;

public class SalonSpecification {

    private SalonSpecification() {
    }

    public static Specification<Salon> hasDistrict(String district) {
        return (root, query, criteriaBuilder) -> {
            if (district == null || district.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("district")),
                    district.toLowerCase()
            );
        };
    }

    public static Specification<Salon> hasService(String service) {
        return (root, query, criteriaBuilder) -> {
            if (service == null || service.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.isMember(
                    service.toLowerCase(),
                    root.get("services")
            );
        };
    }
}