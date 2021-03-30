package pl.andus.mojangstatus;

import me.kbrewster.mojangapi.MojangAPI;
import me.kbrewster.mojangapi.profile.Profile;

import java.util.Scanner;
import java.util.UUID;


public class Main {

    public static void main(String[] args) throws Exception {
        int i = 0;
        while (i < 5) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Co chcesz zobaczyć?");
            System.out.println("(Status Mojang, Gracz)");

            String wpisane = scanner.nextLine();

            if (wpisane.equals("Status Mojang")) {
                System.out.println("Status Serwerów Mojang: ");
                System.out.println(" ");
                MojangAPI.getStatus().forEach((url, state) ->
                        System.out.println(url + " jest aktualnie w stanie: " + state + "!")
                );
            } else if (wpisane.equals("Gracz")) {
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
            } else {
                System.out.println("Zła wartość");
                System.out.println("Nie rozumiem");
            }
        }
    }
}
