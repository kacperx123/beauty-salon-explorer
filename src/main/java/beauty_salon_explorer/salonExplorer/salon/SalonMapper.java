package beauty_salon_explorer.salonExplorer.salon;


import beauty_salon_explorer.salonExplorer.salon.dto.SalonDetailsResponse;
import beauty_salon_explorer.salonExplorer.salon.dto.SalonListItemResponse;

public class SalonMapper {

    private SalonMapper() {
    }

    public static SalonListItemResponse toListItemResponse(Salon salon) {
        return new SalonListItemResponse(
                salon.getId(),
                salon.getName(),
                salon.getDistrict(),
                salon.getRating(),
                salon.getReviewCount(),
                salon.getPriceRange(),
                salon.getServices()
        );
    }

    public static SalonDetailsResponse toDetailsResponse(Salon salon) {
        return new SalonDetailsResponse(
                salon.getId(),
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
                salon.getLatitude(),
                salon.getLongitude()
        );
    }
}