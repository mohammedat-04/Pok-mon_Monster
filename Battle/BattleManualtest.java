package Battle;

import Battle.Battle;
import Pokemon.Glumanda;
import Pokemon.Pokemon;
import Pokemon.helper.Attacke;
import Pokemon.helper.Element;

import java.util.ArrayList;
import java.util.List;
//TODO Neu schreiben
/*public class BattleManualtest {
    public static void main(String[] args) {
        //testCalculateDamage();
        testHasAvailablePokemon();
        testBattleStartSimulation();
        testMaxThreePokemonRule();
    }

    public static void testCalculateDamage() {
        System.out.println("Test: calculateDamage mit Schwäche (WASSER auf Glumanda)");

        Pokemon attacker = new Pokemon();  // Standard DEVMON mit z. B. BODEN-Attacke
        Glumanda defender = new Glumanda(50);
        Attacke attacke = new Attacke("Aqua Jet", Element.WASSER, 100, 1);

        Battle battle = new Battle(attacker, List.of(attacker), List.of(defender));
        int damage = invokeCalculateDamage(battle, attacke, defender);

        // Glumanda hat x2 Schwäche gegen Wasser
        if (damage == 200) {
            System.out.println("✔ calculateDamage korrekt: " + damage);
        } else {
            System.out.println(" Fehler: Erwartet 200, erhalten " + damage);
        }
    }

    public static void testHasAvailablePokemon() {
        System.out.println("Test: hasAvailablePokemon mit 1 lebenden Pokémon");

        Pokemon alive = new Glumanda(10);
        alive.setHp(10);

        Pokemon fainted = new Glumanda(10);
        fainted.setHp(0);

        List<Pokemon> team = new ArrayList<>();
        team.add(fainted);
        team.add(alive);

        Battle battle = new Battle(alive, fainted, team, team);
        boolean result = invokeHasAvailablePokemon(battle, team);

        if (result) {
            System.out.println("✔ hasAvailablePokemon korrekt: true");
        } else {
            System.out.println(" Fehler: Erwartet true, erhalten false");
        }
    }

    public static void testBattleStartSimulation() {
        System.out.println("Test: Simulierter Kampfstart");

        Pokemon p1 = new Glumanda(100);
        p1.setHp(500);

        Pokemon p2 = new Pokemon(); // DEVMON
        p2.setHp(10); // Schwach machen

        List<Pokemon> team1 = List.of(p1);
        List<Pokemon> team2 = List.of(p2);

        Battle battle = new Battle(p1, p2, team1, team2);

        System.out.println("Bitte wähle sofort eine Attacke (Test)");
        battle.start(p1, p2);

        if (p2.getHp() <= 0) {
            System.out.println("✔ Kampf beendet, Gegner ist besiegt");
        } else {
            System.out.println(" Fehler: Gegner hat noch HP: " + p2.getHp());
        }
    }

    public static void testMaxThreePokemonRule() {
        System.out.println("Test: Mehr als 3 Pokémon in einem Team");

        List<Pokemon> overTeam = List.of(
                new Glumanda(10),
                new Glumanda(20),
                new Glumanda(30),
                new Glumanda(40)
        );

        boolean exceptionThrown = false;
        try {
            new Battle(overTeam.get(0), overTeam.get(1), overTeam, overTeam);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (exceptionThrown) {
            System.out.println("✔ Max 3 Pokémon-Regel korrekt behandelt");
        } else {
            System.out.println(" Fehler: Keine Exception bei > 3 Pokémon");
        }
    }

    // === Hilfsmethoden, um private Methoden aufzurufen ===

    private static int invokeCalculateDamage(Battle battle, Attacke attack, Pokemon defender) {
        try {
            var method = Battle.class.getDeclaredMethod("calculateDamage", Attacke.class, Pokemon.class);
            method.setAccessible(true);
            return (int) method.invoke(battle, attack, defender);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static boolean invokeHasAvailablePokemon(Battle battle, List<Pokemon> team) {
        try {
            var method = Battle.class.getDeclaredMethod("hasAvailablePokemon", List.class);
            method.setAccessible(true);
            return (boolean) method.invoke(battle, team);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}*/
