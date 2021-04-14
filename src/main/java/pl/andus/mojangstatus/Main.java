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

    //Colors
    public static final String C_RESET = "\u001B[0m";
    public static final String C_BLACK = "\u001B[30m";
    public static final String C_RED = "\u001B[31m";
    public static final String C_GREEN = "\u001B[32m";
    public static final String C_YELLOW = "\u001B[33m";
    public static final String C_BLUE = "\u001B[34m";
    public static final String C_PURPLE = "\u001B[35m";
    public static final String C_CYAN = "\u001B[36m";
    public static final String C_WHITE = "\u001B[37m";

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        int i = 0;
        while (i < 5) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(C_CYAN + "MojangStatus v1.1.3");
            System.out.println(C_PURPLE + "Co chcesz zobaczyć?");
            System.out.println(C_PURPLE + "(Status Mojang, Gracz, Serwer Minecraft)");
            System.out.println(C_PURPLE + "(Możesz też napisać to krócej: SM, Gr, MC)" + C_GREEN);

            String wpisane = scanner.nextLine();

            if (wpisane.equals("Status Mojang")||wpisane.equals("SM")) {
                final String status = null; //Not used for now
                System.out.println(C_CYAN + "Status Serwerów Mojang: ");
                System.out.println(" ");
                MojangAPI.getStatus().forEach((url, state) ->
                        System.out.println(C_PURPLE + url + " jest aktualnie w stanie: " + state + "!"));
                System.out.println(C_RED + "-(Koniec polecenia)-");
            } else if (wpisane.equals("Gracz")||wpisane.equals("Gr")) {
                System.out.println(C_PURPLE + "Jak chcesz znaleźć gracza?");
                System.out.println(C_PURPLE + "(UUID, Nick)" + C_GREEN);

                String UUIDorName = scanner.nextLine();

                if (UUIDorName.equals("Nick")) {
                    System.out.println(C_PURPLE + "Podaj nazwę gracza:" + C_GREEN);

                    String graczName = scanner.nextLine();

                    System.out.println(C_CYAN + "------Gracz " + graczName + "-----");
                    Profile nickProfile = MojangAPI.getProfile(graczName);
                    System.out.println(C_CYAN + "Nick: " + nickProfile.getName());
                    System.out.println(C_CYAN + "ID: " + nickProfile.getId());
                    System.out.println(C_CYAN + "URL do skórki: " + nickProfile.getTextures().getTextures().getSkin().getUrl());
                    System.out.println(C_CYAN + "-----------------------------------\n");
                    System.out.println(C_RED + "-(Koniec polecenia)-");

                } else if (UUIDorName.equals("UUID")) {
                    System.out.println(C_PURPLE + "Podaj UUID gracza:" + C_GREEN);

                    String graczUUID = scanner.nextLine();

                    System.out.println(C_CYAN + "------UUID " + graczUUID + "-----");
                    Profile uuidProfile = MojangAPI.getProfile(UUID.fromString(graczUUID));
                    System.out.println(C_CYAN + "Nick: " + uuidProfile.getName());
                    System.out.println(C_CYAN + "ID: " + uuidProfile.getId());
                    System.out.println(C_CYAN + "URL do skórki: " + uuidProfile.getTextures().getTextures().getSkin().getUrl());
                    System.out.println(C_CYAN + "-----------------------------------\n");
                    System.out.println(C_RED + "-(Koniec polecenia)-");
                } else {
                    System.out.println(C_RED + "Zła wartość!");
                    System.out.println(C_RED + "Nie rozumiem.");
                }
            } else if (wpisane.equals("Serwer Minecraft")||wpisane.equals("MC")){
                System.out.println(C_PURPLE + "Podaj IP serwera:" + C_GREEN);

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
                //online
                JsonElement playersJson = jsonObj.get("players");
                String players = new String(String.valueOf(playersJson));
                String plOnline;
                //max
                JsonElement playersJson2 = jsonObj.get("players");
                String players2 = new String(String.valueOf(playersJson2));
                String plMax;


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
                if (players.contains("online")) {
                    players = players.substring(players.indexOf("\"online\":"), players.lastIndexOf(",\"max"));
                    plOnline = players.replace("\"online\":", "");
                } else {
                    plOnline = "brak danych";
                }
                if (players2.contains("max")) {
                    players2 = players2.substring(players2.indexOf("\"max\":"), players2.lastIndexOf("}"));
                    plMax = players2.replace("\"max\":", "");
                } else {
                    plMax = "brak danych";
                }

                //Show JSON data
                System.out.println(C_CYAN + "IP: " + C_YELLOW + hostname + C_CYAN + ", " + C_YELLOW + ip + C_CYAN + ":" + C_YELLOW + port);
                System.out.println(" ");
                System.out.println(C_CYAN + "Wersja: " + C_YELLOW + software + C_CYAN + " | " + C_YELLOW + ver);
                System.out.println(" ");
                System.out.println(C_CYAN + "Online: " + C_YELLOW + isOnline);
                System.out.println(" ");
                System.out.println(C_CYAN + "Gracze aktualnie na serwerze: " + C_YELLOW + plOnline + C_CYAN + "/" + C_YELLOW + plMax);
                System.out.println(" ");
                System.out.println(C_CYAN + "MOTD: " + C_YELLOW + motdCl);
                System.out.println(" ");
                System.out.println(C_CYAN + "Protokół: " + C_YELLOW + prot);
                System.out.println(" ");
                System.out.println(C_CYAN + "Debug: ");
                System.out.println(C_CYAN + "   Query: " + C_YELLOW + debugQuery);
                System.out.println(" ");
                System.out.println(C_RED + "-(Koniec polecenia)-");
            } else {
                System.out.println(C_RED + "Zła wartość!");
                System.out.println(C_RED + "Nie rozumiem.");
            }
        }
    }
}
