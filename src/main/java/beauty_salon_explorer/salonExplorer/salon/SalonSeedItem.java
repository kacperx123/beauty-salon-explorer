package beauty_salon_explorer.salonExplorer.salon;

import java.util.Set;

public record SalonSeedItem(
        String name,
        String address,
        String district,
        String phoneNumber,
        String websiteUrl,
        Set<String> services,
        String priceRange,
        Double rating,
        Integer reviewCount,
        String source,
        String externalId,
        Double latitude,
        Double longitude
) {
}