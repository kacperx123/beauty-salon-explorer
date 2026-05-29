package beauty_salon_explorer.salonExplorer.salon.importer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OsmOverpassClient {

    private static final String OVERPASS_API_URL = "https://overpass-api.de/api/interpreter";

    private final RestClient restClient;

    public OsmResponse fetchWarsawBeautySalons() {
        String query = """
                [out:json][timeout:60];
                area["name"="Warszawa"]["boundary"="administrative"]->.warsaw;
                (
                  nwr["shop"="hairdresser"](area.warsaw);
                  nwr["shop"="beauty"](area.warsaw);
                  nwr["beauty"](area.warsaw);
                );
                out center tags;
                """;

        return executeQuery(query);
    }

    private OsmResponse executeQuery(String query) {
        try {
            return restClient.post()
                    .uri(OVERPASS_API_URL)
                    .body("data=" + query)
                    .retrieve()
                    .body(OsmResponse.class);
        } catch (HttpClientErrorException.TooManyRequests exception) {
            throw new OsmImportException(
                    "OpenStreetMap Overpass API rate limit exceeded. Please try again later.",
                    exception
            );
        } catch (HttpClientErrorException exception) {
            HttpStatusCode statusCode = exception.getStatusCode();

            throw new OsmImportException(
                    "OpenStreetMap Overpass API request failed with status: " + statusCode,
                    exception
            );
        } catch (Exception exception) {
            throw new OsmImportException(
                    "OpenStreetMap Overpass API request failed.",
                    exception
            );
        }
    }
}