package pl.andus.mojangstatus;

import com.google.gson.JsonArray;
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
            System.out.println("MojangStatus v1.1");
            System.out.println("Co chcesz zobaczyć?");
            System.out.println("(Status Mojang, Gracz, Serwer Minecraft)");
            System.out.println("(Możesz też napisać to krócej: SM, Gr, MC");

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

                //Strings (All data about server)
                String ip = jsonObj.get("ip").getAsString();
                String hostname = jsonObj.get("hostname").getAsString();
                String port = jsonObj.get("port").getAsString();
                String software;
                String ver = jsonObj.get("version").getAsString();

                //If there is no software/debug info:
                if (jsonObj.has("software")) {
                    software = jsonObj.get("software").getAsString();
                } else {
                    software = "brak danych";
                }

                //Show JSON data
                System.out.println("IP: " + hostname + ", " + ip + ":" + port);
                System.out.println(" ");
                System.out.println("Wersja: " + software + " | " + ver);
                System.out.println(" ");
            } else {
                System.out.println("Zła wartość!");
                System.out.println("Nie rozumiem.");
            }
        }
    }
}
