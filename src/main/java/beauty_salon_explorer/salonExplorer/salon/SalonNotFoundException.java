package beauty_salon_explorer.salonExplorer.salon;

public class SalonNotFoundException extends RuntimeException {

    public SalonNotFoundException(Long id) {
        super("Salon not found with id: " + id);
    }
}