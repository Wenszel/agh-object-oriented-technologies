package driver;

import app.CrawlerApp;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DuckDuckGoDriver {

    private static final Pattern VQD_ID = Pattern.compile("vqd=([\\d-]+)&", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    public static List<String> searchForImages(String query) throws IOException, InterruptedException {
        var htmlQuery = query.replace(' ', '+');

        var searchSource = downloadUrlSource(htmlQuery);
        return extractPhotoUrls(searchSource, htmlQuery);
    }

    private static String downloadUrlSource(String searchQuery) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var queryRequest = String.format("https://duckduckgo.com/?q=%s", searchQuery);
        URI uri = getScraperUri(queryRequest);
        HttpResponse<String> response = sendRequest(client, uri);
        return response.body();
    }

    private static URI getScraperUri(String queryRequest) {
        var scraperRequest = "http://api.scraperapi.com?api_key=%s&url=%s".formatted(CrawlerApp.SCRAPER_API_KEY, queryRequest);
        return URI.create(scraperRequest);
    }

    private static HttpResponse<String> sendRequest(HttpClient client, URI uri) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();
        return client.send(request,
                HttpResponse.BodyHandlers.ofString());
    }

    private static List<String> extractPhotoUrls(String html, String searchQuery) throws IOException, InterruptedException {
        var matcher = VQD_ID.matcher(html);
        if (matcher.find()) {
            String vqdId = matcher.group(1);

            var queryRequest = String.format("https://duckduckgo.com/i.js?q=%s&o=json&vqd=%s", searchQuery, vqdId);
            URI uri = getScraperUri(queryRequest);
            var client = HttpClient.newHttpClient();
            HttpResponse<String> response = sendRequest(client, uri);

            return parseImageResults(response);
        }
        throw new IOException("Not valid search source site:" + html);
    }

    private static List<String> parseImageResults(HttpResponse<String> response) {
        var jsonObject = new JSONObject(response.body());
        var results = jsonObject.getJSONArray("results");
        return StreamSupport.stream(results.spliterator(), false)
                .filter(JSONObject.class::isInstance)
                .map(result -> ((JSONObject) result).getString("image"))
                .collect(Collectors.toList());
    }
}
