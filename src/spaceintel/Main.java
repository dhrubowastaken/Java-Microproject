package spaceintel;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import spaceintel.db.DatabaseManager;
import spaceintel.model.Observation;
import spaceintel.ui.LoginView;
import spaceintel.ui.SplashScreenView;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * CLASS: Main.java
 * PURPOSE: Application Entry Point & Web Server Dispatcher (Module V).
 * EXPLANATION FOR BEGINNERS:
 * This is where the Java program starts execution (`public static void main`).
 * 
 * SEQUENCE OF EXECUTION:
 * Step 1: Start background HTTP Server on port 8080 (http://localhost:8080/dashboard)
 *         to fulfill Module V (Servlets / HTTP Request-Response / Web Dashboard)
 *         without needing Apache Tomcat!
 * Step 2: Set modern look and feel for Java Swing.
 * Step 3: Launch Screen 1 (SplashScreenView) with mission loading animation.
 * Step 4: SplashScreen initializes the 16-table database and opens Screen 2 (LoginView).
 * ============================================================================
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("MICRO PROJECT 09: AI-BASED SPACE INTELLIGENCE & GEOSPATIAL DATA SYSTEM");
        System.out.println("Organization: ABC National Geospatial Intelligence Centre (Xovian Aerospace)");
        System.out.println("====================================================================");

        // Step 1: Start lightweight embedded HTTP server for Module V (Web Dashboard)
        startEmbeddedWebServer();

        // Step 2: Set high-contrast Aerospace Dark Theme (FlatDarkLaf & global dark palette)
        spaceintel.util.UITheme.setupGlobalTheme();

        // Step 3: Launch GUI on Event Dispatch Thread (Swing best practice)
        SwingUtilities.invokeLater(() -> {
            SplashScreenView splash = new SplashScreenView();
            splash.startLoading(() -> {
                // Step 4: After splash completes, display Login screen
                LoginView login = new LoginView();
                login.setVisible(true);
            });
        });
    }

    /**
     * Starts an embedded HTTP Web Server on port 8080 (Module V).
     * Accessible at: http://localhost:8080/dashboard
     */
    private static void startEmbeddedWebServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/dashboard", new WebDashboardHandler());
            server.setExecutor(null); // default executor
            server.start();
            System.out.println("[Web Server] Module V Live Web Dashboard running at: http://localhost:8080/dashboard");
        } catch (IOException e) {
            System.out.println("[Web Server] Note: Port 8080 unavailable (" + e.getMessage() + "). Desktop Swing UI will run normally.");
        }
    }

    /**
     * HTTP Handler serving an HTML5/JSP-styled Web Dashboard (Module V).
     */
    static class WebDashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            DatabaseManager db = DatabaseManager.getInstance();
            Map<String, String> metrics = db.getDashboardMetrics();
            List<Observation> observations = db.getAllObservations();

            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
            html.append("<title>Xovian Aerospace - Web Intelligence Dashboard</title>");
            html.append("<style>");
            html.append("body { font-family: 'Segoe UI', Tahoma, sans-serif; background: #0B0F19; color: #F8FAFC; margin: 0; padding: 25px; }");
            html.append("h1 { color: #06B6D4; margin-bottom: 5px; }");
            html.append(".sub { color: #94A3B8; margin-bottom: 25px; }");
            html.append(".grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-bottom: 25px; }");
            html.append(".card { background: #151D2E; border: 1px solid #334155; padding: 18px; border-radius: 8px; }");
            html.append(".card h3 { margin: 0 0 8px 0; font-size: 13px; color: #94A3B8; text-transform: uppercase; }");
            html.append(".card p { margin: 0; font-size: 28px; font-weight: bold; color: #06B6D4; }");
            html.append("table { width: 100%; border-collapse: collapse; background: #151D2E; border-radius: 8px; overflow: hidden; }");
            html.append("th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #334155; }");
            html.append("th { background: #1E293B; color: #06B6D4; }");
            html.append("tr:hover { background: #1E293B; }");
            html.append(".badge { padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; }");
            html.append(".verified { background: #065F46; color: #34D399; }");
            html.append(".anom { background: #7F1D1D; color: #F87171; }");
            html.append("</style></head><body>");

            html.append("<h1>XOVIAN AEROSPACE - WEB INTELLIGENCE CONSOLE</h1>");
            html.append("<div class='sub'>ABC National Geospatial Intelligence Centre | Java Enterprise Module V Web Dashboard</div>");

            html.append("<div class='grid'>");
            html.append("<div class='card'><h3>Active Platforms</h3><p>").append(metrics.getOrDefault("METRIC-SOURCES", "10")).append("</p></div>");
            html.append("<div class='card'><h3>Mean AI Confidence</h3><p>").append(metrics.getOrDefault("METRIC-CONFIDENCE", "92.4")).append("%</p></div>");
            html.append("<div class='card'><h3>Active Anomalies</h3><p style='color:#F59E0B;'>").append(metrics.getOrDefault("METRIC-ANOMALIES", "3")).append("</p></div>");
            html.append("</div>");

            html.append("<div class='card'>");
            html.append("<h2 style='margin-top:0; color:#E2E8F0;'>Live Satellite & Sensor Telemetry Feed</h2>");
            html.append("<table>");
            html.append("<thead><tr><th>Obs ID</th><th>Observation Type</th><th>Value</th><th>Source</th><th>Region</th><th>Status</th></tr></thead><tbody>");

            for (Observation o : observations) {
                html.append("<tr>");
                html.append("<td>").append(o.getObservationId()).append("</td>");
                html.append("<td>").append(o.getObservationType()).append("</td>");
                html.append("<td>").append(o.getMeasuredValue()).append(" ").append(o.getUnit()).append("</td>");
                html.append("<td>").append(o.getSourceId()).append("</td>");
                html.append("<td>").append(o.getRegionId()).append("</td>");
                String badgeClass = "VERIFIED".equalsIgnoreCase(o.getStatus()) ? "verified" : "anom";
                html.append("<td><span class='badge ").append(badgeClass).append("'>").append(o.getStatus()).append("</span></td>");
                html.append("</tr>");
            }

            html.append("</tbody></table></div>");
            html.append("<p style='text-align:center; color:#64748B; margin-top:25px;'>Micro Project 09 &bull; Powered by Java SE 21 &bull; SQLite / MySQL Dual Database Layer</p>");
            html.append("</body></html>");

            byte[] responseBytes = html.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}
