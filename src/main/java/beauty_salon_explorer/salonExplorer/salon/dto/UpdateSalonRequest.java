package beauty_salon_explorer.salonExplorer.salon.dto;

import java.util.Set;

public record UpdateSalonRequest(
        String name,
        String address,
        String district,
        String phoneNumber,
        String websiteUrl,
        Set<String> services,
        String priceRange,
        Double rating,
        Integer reviewCount
) {
}
