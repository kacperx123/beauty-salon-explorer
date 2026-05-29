package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class OsmSalonMapper {

    private static final String DEFAULT_DISTRICT = "Warsaw";

    public Optional<Salon> mapToSalon(OsmElement element, String district) {
        Map<String, String> tags = element.tags();

        if (tags == null || isBlank(tags.get("name"))) {
            return Optional.empty();
        }

        String address = buildAddress(tags);

        if (isBlank(address)) {
            return Optional.empty();
        }

        Salon salon = new Salon();

        salon.setName(tags.get("name"));
        salon.setAddress(address);
        salon.setDistrict(resolveDistrict(tags));
        salon.setPhoneNumber(firstNonBlank(tags.get("phone"), tags.get("contact:phone")));
        salon.setWebsiteUrl(firstNonBlank(tags.get("website"), tags.get("contact:website")));
        salon.setServices(resolveServices(tags));
        salon.setPriceRange(null);
        salon.setRating(null);
        salon.setReviewCount(null);
        salon.setSource("OpenStreetMap");
        salon.setExternalId(element.type() + "-" + element.id());
        salon.setLatitude(resolveLatitude(element));
        salon.setLongitude(resolveLongitude(element));

        return Optional.of(salon);
    }

    private String buildAddress(Map<String, String> tags) {
        String street = tags.get("addr:street");
        String houseNumber = tags.get("addr:housenumber");
        String city = firstNonBlank(tags.get("addr:city"), "Warszawa");

        if (isBlank(street) && isBlank(houseNumber)) {
            return null;
        }

        StringBuilder address = new StringBuilder();

        if (!isBlank(street)) {
            address.append(street);
        }

        if (!isBlank(houseNumber)) {
            if (!address.isEmpty()) {
                address.append(" ");
            }
            address.append(houseNumber);
        }

        if (!isBlank(city)) {
            address.append(", ").append(city);
        }

        return address.toString();
    }

    private String resolveDistrict(Map<String, String> tags) {
        return firstNonBlank(
                tags.get("addr:district"),
                tags.get("is_in:suburb"),
                tags.get("addr:suburb"),
                DEFAULT_DISTRICT
        );
    }

    private Set<String> resolveServices(Map<String, String> tags) {
        Set<String> services = new HashSet<>();

        String shop = tags.get("shop");
        String beauty = tags.get("beauty");

        if ("hairdresser".equalsIgnoreCase(shop)) {
            services.add("hairdresser");
        }

        if ("beauty".equalsIgnoreCase(shop)) {
            services.add("beauty");
        }

        if (!isBlank(beauty)) {
            services.add(beauty.toLowerCase());
        }

        if (services.isEmpty()) {
            services.add("beauty");
        }

        return services;
    }

    private Double resolveLatitude(OsmElement element) {
        if (element.lat() != null) {
            return element.lat();
        }

        if (element.center() != null) {
            return element.center().lat();
        }

        return null;
    }

    private Double resolveLongitude(OsmElement element) {
        if (element.lon() != null) {
            return element.lon();
        }

        if (element.center() != null) {
            return element.center().lon();
        }

        return null;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}