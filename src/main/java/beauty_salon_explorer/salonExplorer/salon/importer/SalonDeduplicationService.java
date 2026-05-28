package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalonDeduplicationService {

    public List<Salon> deduplicate(Collection<Salon> salons) {

        Map<String, Salon> uniqueSalons = new LinkedHashMap<>();

        for (Salon salon : salons) {

            String key = buildKey(salon);

            Salon existingSalon = uniqueSalons.get(key);

            if (existingSalon == null) {
                uniqueSalons.put(key, salon);
                continue;
            }

            if (countFilledFields(salon) > countFilledFields(existingSalon)) {
                uniqueSalons.put(key, salon);
            }
        }

        return uniqueSalons.values()
                .stream()
                .toList();
    }

    private String buildKey(Salon salon) {

        String normalizedName = normalize(salon.getName());
        String normalizedAddress = normalize(salon.getAddress());

        return normalizedName + "|" + normalizedAddress;
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "")
                .replaceAll("[^a-z0-9ąćęłńóśźż]", "");
    }

    private int countFilledFields(Salon salon) {

        int score = 0;

        if (salon.getPhoneNumber() != null) score++;
        if (salon.getWebsiteUrl() != null) score++;
        if (salon.getLatitude() != null) score++;
        if (salon.getLongitude() != null) score++;
        if (salon.getServices() != null && !salon.getServices().isEmpty()) score++;

        return score;
    }
}