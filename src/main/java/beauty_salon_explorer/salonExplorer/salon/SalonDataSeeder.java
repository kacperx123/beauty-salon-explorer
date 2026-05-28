package beauty_salon_explorer.salonExplorer.salon;


import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;


import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SalonDataSeeder implements CommandLineRunner {

    private final SalonRepository salonRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String @NonNull ... args) throws Exception {

        if (salonRepository.count() > 0) {
            return;
        }

        InputStream inputStream = new ClassPathResource(
                "data/salons.seed.json"
        ).getInputStream();

        List<SalonSeedItem> items = objectMapper.readValue(
                inputStream,
                new TypeReference<>() {
                }
        );

        List<Salon> salons = items.stream()
                .map(this::mapToSalon)
                .toList();

        salonRepository.saveAll(salons);

        System.out.println("Seeded salons: " + salons.size());
    }

    private Salon mapToSalon(SalonSeedItem item) {

        Salon salon = new Salon();

        salon.setName(item.name());
        salon.setAddress(item.address());
        salon.setDistrict(item.district());
        salon.setPhoneNumber(item.phoneNumber());
        salon.setWebsiteUrl(item.websiteUrl());
        salon.setServices(item.services());
        salon.setPriceRange(item.priceRange());
        salon.setRating(item.rating());
        salon.setReviewCount(item.reviewCount());
        salon.setSource(item.source());
        salon.setExternalId(item.externalId());
        salon.setLatitude(item.latitude());
        salon.setLongitude(item.longitude());

        return salon;
    }
}