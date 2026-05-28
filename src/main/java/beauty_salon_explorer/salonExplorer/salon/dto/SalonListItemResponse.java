package beauty_salon_explorer.salonExplorer.salon.dto;

import java.util.Set;

public record SalonListItemResponse(
        Long id,
        String name,
        String district,
        Double rating,
        Integer reviewCount,
        String priceRange,
        Set<String> services
) {
}
