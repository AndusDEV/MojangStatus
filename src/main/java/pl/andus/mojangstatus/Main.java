package pl.andus.mojangstatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kbrewster.mojangapi.MojangAPI;
import me.kbrewster.mojangapi.profile.Profile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.UUID;


public class Main {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        int i = 0;
        while (i < 5) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("MojangStatus v1.1.1");
            System.out.println("Co chcesz zobaczyć?");
            System.out.println("(Status Mojang, Gracz, Serwer Minecraft)");
            System.out.println("(Możesz też napisać to krócej: SM, Gr, MC)");

            String wpisane = scanner.nextLine();

            if (wpisane.equals("Status Mojang")||wpisane.equals("SM")) {
                System.out.println("Status Serwerów Mojang: ");
                System.out.println(" ");
                MojangAPI.getStatus().forEach((url, state) ->
                        System.out.println(url + " jest aktualnie w stanie: " + state + "!")
                );
            } else if (wpisane.equals("Gracz")||wpisane.equals("Gr")) {
                System.out.println("Jak chcesz znaleźć gracza?");
                System.out.println("(UUID, Nick)");

                String UUIDorName = scanner.nextLine();

                if (UUIDorName.equals("Nick")) {
                    System.out.println("Podaj nazwę gracza:");

                    String graczName = scanner.nextLine();

                    System.out.println("------Gracz " + graczName + "-----");
                    Profile nickProfile = MojangAPI.getProfile(graczName);
                    System.out.println("Nick: " + nickProfile.getName());
                    System.out.println("ID: " + nickProfile.getId());
                    System.out.println("URL do skórki: " + nickProfile.getTextures().getTextures().getSkin().getUrl());
                    System.out.println("-----------------------------------\n");

                } else if (UUIDorName.equals("UUID")) {
                    System.out.println("Podaj UUID gracza:");

                    String graczUUID = scanner.nextLine();

                    System.out.println("------UUID " + graczUUID + "-----");
                    Profile uuidProfile = MojangAPI.getProfile(UUID.fromString(graczUUID));
                    System.out.println("Nick: " + uuidProfile.getName());
                    System.out.println("ID: " + uuidProfile.getId());
                    System.out.println("URL do skórki: " + uuidProfile.getTextures().getTextures().getSkin().getUrl());
                    System.out.println("-----------------------------------\n");
                } else {
                    System.out.println("Zła wartość");
                    System.out.println("Nie rozumiem");
                }
            } else if (wpisane.equals("Serwer Minecraft")||wpisane.equals("MC")){
                System.out.println("Podaj IP serwera:");

                String srvURL = "https://api.mcsrvstat.us/2/" + scanner.nextLine();


                //Connect to the URL using java library
                URL url = new URL(srvURL);
                URLConnection request = url.openConnection();
                request.connect();

                //JSON to text (Gson's work)
                JsonParser jp = new JsonParser();
                JsonElement jsonEl = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonObject jsonObj = jsonEl.getAsJsonObject();

                //Strings & JsonElements (All data about server)
                String ip = jsonObj.get("ip").getAsString();
                String hostname = jsonObj.get("hostname").getAsString();
                String port = jsonObj.get("port").getAsString();
                String software;
                String ver = jsonObj.get("version").getAsString();
                String isOnline = jsonObj.get("online").getAsString();
                String prot = jsonObj.get("protocol").getAsString();
                //motd
                JsonElement motdJson = jsonObj.get("motd");
                String motd = new String(String.valueOf(motdJson));
                String motdCl = null;
                //debug
                JsonElement debugJson = jsonObj.get("debug");
                String debug = new String(String.valueOf(debugJson));
                String debugQuery = null;
                //players
                JsonElement playersJson = jsonObj.get("players");
                String players = new String(String.valueOf(playersJson));
                String plOnline;


                //If there is no software info:
                if (jsonObj.has("software")) {
                    software = jsonObj.get("software").getAsString();
                } else {
                    software = "brak danych";
                }
                //If online is true/false / there is no online info:
                if (isOnline.equals("true")) {
                    isOnline = "tak";
                } else if (isOnline.equals("false")) {
                    isOnline = "nie";
                } else {
                    isOnline = "Brak danych";
                }
                //If motd contains clean:
                if (motd.contains("\"clean\":[")) {
                    motd = motd.substring(motd.indexOf("\"clean\""), motd.lastIndexOf("],"));
                    motdCl = motd.replace("\"clean\":[", "");
                }
                //If debug contains query info:
                if (debug.contains("query")) {
                    debug = debug.substring(debug.indexOf("\"query\":"), debug.lastIndexOf(",\"srv"));
                    debugQuery = debug.replace("\"query\":", "");

                    if (debugQuery.equals("false")) {
                        debugQuery = "wyłączone";
                    } else if (debugQuery.equals("true")) {
                        debugQuery = "włączone";
                    } else {
                        debugQuery = "brak danych";
                    }
                }
                //If players contains max & online:
                if (players.contains("online") || players.contains("max")) {
                    players = players.substring(players.indexOf("\"online\":"), players.lastIndexOf(",\"max"));
                    plOnline = players.replace("\"online\":", "");
                } else {
                    plOnline = "brak danych";
                }

                //Show JSON data
                System.out.println("IP: " + hostname + ", " + ip + ":" + port);
                System.out.println(" ");
                System.out.println("Wersja: " + software + " | " + ver);
                System.out.println(" ");
                System.out.println("Online: " + isOnline);
                System.out.println(" ");
                System.out.println("Protokół: " + prot);
                System.out.println(" ");
                System.out.println("MOTD: " + motdCl);
                System.out.println(" ");
                System.out.println("Debug: ");
                System.out.println("   Query: " + debugQuery);
                System.out.println(" ");
                System.out.println("Gracze aktualnie na serwerze: " + plOnline);
                System.out.println(" ");
            } else {
                System.out.println("Zła wartość!");
                System.out.println("Nie rozumiem.");
            }
        }
    }
}
