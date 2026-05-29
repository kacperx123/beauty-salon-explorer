package beauty_salon_explorer.salonExplorer.salon.importer;

import beauty_salon_explorer.salonExplorer.salon.Salon;
import beauty_salon_explorer.salonExplorer.salon.SalonRepository;
import beauty_salon_explorer.salonExplorer.salon.SalonSeedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonSeedExportService {

    private final SalonRepository salonRepository;
    private final ObjectMapper objectMapper;

    public String exportSeedJson() throws Exception {
        List<SalonSeedItem> seedItems = salonRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Salon::getName))
                .map(this::mapToSeedItem)
                .toList();

        Path outputPath = Path.of(
                "src",
                "main",
                "resources",
                "data",
                "salons.seed.json"
        );

        Files.createDirectories(outputPath.getParent());

        ObjectMapper localMapper = objectMapper.rebuild().
                enable(SerializationFeature.INDENT_OUTPUT).
                build();
        objectMapper.writeValue(outputPath.toFile(), seedItems);

        return "Exported salons to seed JSON: " + seedItems.size();
    }

    private SalonSeedItem mapToSeedItem(Salon salon) {
        return new SalonSeedItem(
                salon.getName(),
                salon.getAddress(),
                salon.getDistrict(),
                salon.getPhoneNumber(),
                salon.getWebsiteUrl(),
                salon.getServices(),
                salon.getPriceRange(),
                salon.getRating(),
                salon.getReviewCount(),
                salon.getSource(),
                salon.getExternalId(),
                salon.getLatitude(),
                salon.getLongitude()
        );
    }
}